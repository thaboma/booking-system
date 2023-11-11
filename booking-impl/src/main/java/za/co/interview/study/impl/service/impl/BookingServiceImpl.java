package za.co.interview.study.impl.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import za.co.interview.study.impl.dao.BookingRepository;
import za.co.interview.study.impl.dao.MaintenanceSlotsRepository;
import za.co.interview.study.impl.dto.*;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;
import za.co.interview.study.impl.mapper.BookingMapper;
import za.co.interview.study.impl.service.BookingService;
import za.co.interview.study.impl.util.DateUtil;
import za.co.interview.study.impl.util.HelperUtil;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

	private int maxCapacity;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private MaintenanceSlotsRepository maintenanceSlotsRepository;

	@PostConstruct
	void init() {
		maxCapacity = bookingRepository.getMaxCapacity();
	}


	@Override
	public void bookConferenceRoom(BookingRequest bookingRequest) {
		// * Validation rules
		// Validate number of people allowed for booking conference rooms
		int capacity = bookingRequest.getNumOfAttendees();
		HelperUtil.checkForMinCapacity(capacity,maxCapacity);

		Instant startTime = bookingRequest.getStartTime();
		Instant endTime = bookingRequest.getEndTime();

		// Booking can be done only for the current date
		DateUtil.validateForCurrentDate(startTime, endTime);

		// Booking can be done only in intervals of 15 mins,
		DateUtil.validateInterval(startTime, endTime);

		Date startDate = DateUtil.getDateFromInstant(startTime);
		Date endDate = DateUtil.getDateFromInstant(endTime);

		Collection<ConferenceRoom> conferenceRooms = bookingRepository.findAll().stream().collect(Collectors.toList());
		Collection<ConferenceRoom> bookedConferenceRooms = bookingRepository.findConferenceRoomsByBookingSlotsBetween(startDate, endDate);
		Collection<ConferenceRoomDto> conferenceRoomsDtos = BookingMapper.conferenceRoomsToConferenceRoomDtos(conferenceRooms);
		Collection<ConferenceRoomDto> bookedConferenceRoomDtos = BookingMapper.conferenceRoomsToConferenceRoomDtos(bookedConferenceRooms);

		conferenceRoomsDtos.removeAll(bookedConferenceRoomDtos);
		bookedConferenceRoomDtos = getAvailableConferenceRooms(bookingRequest, conferenceRoomsDtos);
		ConferenceRoomDto eligibleConferenceRoom = getEligibleConferenceRoom(startDate, endDate, bookedConferenceRoomDtos);

		if (ObjectUtils.isEmpty(eligibleConferenceRoom)) {
			// At this point its either the slots are unavailable because the selected period overlaps with a maintenance slot
			// or else all Conference rooms have been booked for this period
			checkForMaintenanceSlot(startTime, endTime, bookedConferenceRooms);
			//At this point it can only mean all conference rooms are reserved
			throw new ValidationException("No conference rooms available for the selected period ");
		} else {
			persistSelectRoom(startDate, endDate, eligibleConferenceRoom);
		}
	}

	private void checkForMaintenanceSlot(Instant startTime, Instant endTime, Collection<ConferenceRoom> bookedConferenceRooms) {
		if (ObjectUtils.isNotEmpty(bookedConferenceRooms)) {
			Set<BookingSlot> maintenanceSlots = maintenanceSlotsRepository.findMaintenanceSlots();
			validateForMaintenenceSlots(startTime, endTime, maintenanceSlots);
		}
	}

	private void persistSelectRoom(Date startDate, Date endDate, ConferenceRoomDto eligibleConferenceRoom) {
		if (ObjectUtils.isNotEmpty(eligibleConferenceRoom.getAvailableSlots())) {
			BookingSlot selectedSlot = BookingMapper.getBookingSlot(startDate, endDate, eligibleConferenceRoom);
			ConferenceRoom dbCandidate = bookingRepository.findById(eligibleConferenceRoom.getRoomId()).get();
			selectedSlot.setConferenceRoom(dbCandidate);
			dbCandidate.getBookingSlots().add(selectedSlot);
			ConferenceRoom fromDb = bookingRepository.saveAndFlush(dbCandidate);
			log.info("Saved candidate is : " + fromDb);
		}
	}

	private void validateForMaintenenceSlots(Instant startTime, Instant endTime, Set<BookingSlot> maintenanceSlots) {
		BookingSlot selectedSlot =HelperUtil.checkForOverlaps(maintenanceSlots, startTime, endTime);
		if (ObjectUtils.isNotEmpty(selectedSlot)) {
		   HelperUtil.checkIfMaintenanceSlot(selectedSlot);
	   }
	}

	private ConferenceRoomDto getEligibleConferenceRoom(Date startDate, Date endDate, Collection<ConferenceRoomDto> inventoryOfRooms) {
		ConferenceRoomDto conferenceRoom= inventoryOfRooms.stream().map(cr -> {
			Set<BookingSlotDto> bookingSlots = cr
					.getAvailableSlots()
					.stream()
					.filter(s -> startDate.after(s.getStartTime()) && endDate.before(s.getEndTime()))
					.collect(Collectors.toSet());
			return cr;
		}).findFirst().orElse(null);

		return ObjectUtils.isNotEmpty(conferenceRoom) ? getAvailableConferenceRooms(conferenceRoom) : conferenceRoom;
	}

	private ConferenceRoomDto getAvailableConferenceRooms(ConferenceRoomDto conferenceRoom) {
		if(conferenceRoom !=null) {
			Set<BookingSlotDto> slots= conferenceRoom.getAvailableSlots();
			Set<BookingSlotDto> bookingSlots =slots.parallelStream().filter(s->s.isMaintenanceSlot()).collect(Collectors.toSet());
			conferenceRoom.setAvailableSlots(bookingSlots);
		}
		return conferenceRoom;
	}

	private Collection<ConferenceRoomDto> getAvailableConferenceRooms(BookingRequest bookingRequest, Collection<ConferenceRoomDto> inventoryOfRooms) {
		Comparator<ConferenceRoomDto> capacityComparator = Comparator.comparing(ConferenceRoomDto::getCapacity, Comparator.nullsFirst(Comparator.naturalOrder()));
		inventoryOfRooms = inventoryOfRooms
				.stream()
				.filter(r -> r != null)
				.filter(cr -> !CollectionUtils.isEmpty(cr.getAvailableSlots()))
				.filter(room -> room.getCapacity() >= bookingRequest.getNumOfAttendees())
				.sorted(capacityComparator)
				.distinct()
				.collect(Collectors.toList());
		return inventoryOfRooms;
	}

	@Override
	public Collection<ListRoomsResponseDto> listConferenceRooms(ListRoomsRequest listRoomsRequest) {
		Collection<ConferenceRoom> conferenceRooms = bookingRepository.findAllConferenceRooms();
		Collection<ConferenceRoom> bookedConferenceRooms = bookingRepository.findConferenceRoomsByBookingSlotsBetween(DateUtil.getDateFromInstant(listRoomsRequest.getStartTime()), DateUtil.getDateFromInstant(listRoomsRequest.getEndTime()));
		conferenceRooms.removeAll(bookedConferenceRooms);

		return BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
	}
}

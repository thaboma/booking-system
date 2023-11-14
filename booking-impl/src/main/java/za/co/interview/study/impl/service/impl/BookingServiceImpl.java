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
import java.util.*;
import java.util.stream.Collectors;

import static za.co.interview.study.impl.util.DateUtil.getCurrentDayInstant;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

	private int maxCapacity;

	private Set<BookingSlotDto> maintenanceSlots;

 	private Set<Interval> bookedIntervals;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private MaintenanceSlotsRepository maintenanceSlotsRepository;

	public BookingServiceImpl(BookingRepository bookingRepository, MaintenanceSlotsRepository maintenanceSlotsRepository) {
		this.bookingRepository = bookingRepository;
		this.maintenanceSlotsRepository = maintenanceSlotsRepository;
	}

	@PostConstruct
	void init() {
		maxCapacity = bookingRepository.getMaxCapacity();
		Set<BookingSlot> maintSlots = maintenanceSlotsRepository.findMaintenanceSlots();
		maintenanceSlots =BookingMapper.bookingSlotsToBookingSlotDtos(maintSlots);
		bookedIntervals = new HashSet<>();
		bookedIntervals.add(new Interval(new Moment(9,0),new Moment(9,15)));
		bookedIntervals.add(new Interval(new Moment(13,0),new Moment(13,15)));
		bookedIntervals.add(new Interval(new Moment(17,0),new Moment(17,15)));
	}

	@Override
	public void bookConferenceRoom(BookingRequestDto bookingRequestDto) {
		// * Validation rules
		// 1. Validate number of people allowed for booking conference rooms
		int capacity = bookingRequestDto.getNumOfAttendees();
		HelperUtil.checkForMaxCapacity(capacity,maxCapacity);

		Instant startTime = getCurrentDayInstant(bookingRequestDto.getStartTime());
		Instant endTime = getCurrentDayInstant(bookingRequestDto.getEndTime());

		// Booking can be done only for the current date
		DateUtil.validateForCurrentDate(startTime, endTime);

		// Booking can be done only in intervals of 15 mins,
		DateUtil.validateInterval(startTime, endTime);

		//Check if the selected period coincides with one of the maintenance slots
		checkForMaintenanceSlot(startTime, endTime);

		//Check if the selected period overlaps with at least one of the maintenance slots
		checkForMaintenanceSlotOverlaps(startTime, endTime);

		Date startDate = DateUtil.getDateFromInstant(startTime);
		Date endDate = DateUtil.getDateFromInstant(endTime);

		Collection<ConferenceRoom> conferenceRooms = bookingRepository.findAllConferenceRoomsByDates(startDate, endDate);
		Collection<ConferenceRoom> bookedConferenceRooms = bookingRepository.findConferenceRoomsByBookingSlotsBetween(startDate, endDate);
		Collection<ConferenceRoomDto> conferenceRoomsDtos = BookingMapper.conferenceRoomsToConferenceRoomDtos(conferenceRooms);
		Collection<ConferenceRoomDto> bookedConferenceRoomDtos = BookingMapper.conferenceRoomsToConferenceRoomDtos(bookedConferenceRooms);

		conferenceRoomsDtos.removeAll(bookedConferenceRoomDtos);
		bookedConferenceRoomDtos = getAvailableConferenceRooms(bookingRequestDto, conferenceRoomsDtos);
		ConferenceRoomDto eligibleConferenceRoom = getEligibleConferenceRoom(startDate, endDate, bookedConferenceRoomDtos);


		if (ObjectUtils.isEmpty(eligibleConferenceRoom)) {
			//At this point it can only mean all conference rooms are reserved
			throw new ValidationException("No conference rooms available for the selected period ");
		} else {
			persistSelectRoom(startDate, endDate, eligibleConferenceRoom);
		}
	}

	private void checkForMaintenanceSlotOverlaps(Instant startTime, Instant endTime) {
		Interval slot=HelperUtil.checkForOverlaps(bookedIntervals, startTime, endTime);
		if (ObjectUtils.isNotEmpty(slot)){
			throw new ValidationException("The selected period overlaps with one of the maintenance slots  ");
		}
	}

	private void checkForMaintenanceSlot(Instant startTime, Instant endTime) {
		BookingSlotDto maintenanceSlot =HelperUtil.checkForMaintenanceSlot(maintenanceSlots, startTime, endTime);
		if (ObjectUtils.isNotEmpty(maintenanceSlot)){
			throw new ValidationException("The selected period coincides with one of the maintenance slots  ");
		}
	}

	private void persistSelectRoom(Date startDate, Date endDate, ConferenceRoomDto eligibleConferenceRoom) {
		if (ObjectUtils.isNotEmpty(eligibleConferenceRoom.getAvailableSlots())) {
			BookingSlot selectedSlot = BookingMapper.getBookingSlot(startDate, endDate, eligibleConferenceRoom);
			ConferenceRoom dbCandidate = bookingRepository.findById(eligibleConferenceRoom.getRoomId()).orElse(null);
			if (dbCandidate==null){
				throw new RuntimeException("Unable to locate a conference room somehow , please again later ");
			}
			selectedSlot.setConferenceRoom(dbCandidate);
			dbCandidate.getBookingSlots().add(selectedSlot);
			ConferenceRoom fromDb = bookingRepository.saveAndFlush(dbCandidate);
			log.info("Saved candidate is : " + fromDb);
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

	private Collection<ConferenceRoomDto> getAvailableConferenceRooms(BookingRequestDto bookingRequestDto, Collection<ConferenceRoomDto> inventoryOfRooms) {
		Comparator<ConferenceRoomDto> capacityComparator = Comparator.comparing(ConferenceRoomDto::getCapacity, Comparator.nullsFirst(Comparator.naturalOrder()));
		inventoryOfRooms = inventoryOfRooms
				.stream()
				.filter(r -> r != null)
				.filter(cr -> !CollectionUtils.isEmpty(cr.getAvailableSlots()))
				.filter(room -> room.getCapacity() >= bookingRequestDto.getNumOfAttendees())
				.sorted(capacityComparator)
				.distinct()
				.collect(Collectors.toList());
		return inventoryOfRooms;
	}

	@Override
	public Collection<ListRoomsResponseDto> listConferenceRooms(ListRoomsRequestDto listRoomsRequestDto) {
		Instant startTime = getCurrentDayInstant(listRoomsRequestDto.getStartTime());
		Instant endTime = getCurrentDayInstant(listRoomsRequestDto.getEndTime());

		Interval slot=HelperUtil.checkForOverlaps(bookedIntervals, startTime, endTime);
		if (ObjectUtils.isNotEmpty(slot)){
			log.warn("Requested slot overlaps with maintenance slot");
			return new HashSet<>();
		}


		Collection<ConferenceRoom> conferenceRooms = bookingRepository.findAllConferenceRooms();
		Collection<ConferenceRoom> bookedConferenceRooms = bookingRepository.findConferenceRoomsByBookingSlotsBetween(DateUtil.getDateFromInstant(listRoomsRequestDto.getStartTime()), DateUtil.getDateFromInstant(listRoomsRequestDto.getEndTime()));
		conferenceRooms.removeAll(bookedConferenceRooms);

		return BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
	}
}

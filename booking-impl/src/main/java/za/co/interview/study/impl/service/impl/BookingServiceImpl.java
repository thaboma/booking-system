package za.co.interview.study.impl.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import za.co.interview.study.impl.dao.BookingRepository;
import za.co.interview.study.impl.dto.BookingRequest;
import za.co.interview.study.impl.dto.ListRoomsRequest;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;
import za.co.interview.study.impl.mapper.BookingMapper;
import za.co.interview.study.impl.service.BookingService;
import za.co.interview.study.impl.util.DateUtil;
import za.co.interview.study.impl.util.HelperUtil;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Override
	public void bookConferenceRoom(BookingRequest bookingRequest) {

		Instant startTime = bookingRequest.getStartTime();
		Instant endTime = bookingRequest.getEndTime();

		// * Validation rules
		// 1. Booking can be done only for the current date
		DateUtil.validateForCurrentDate(startTime, endTime);

		// 2. Booking can be done only in intervals of 15 mins,
		DateUtil.validateInterval(startTime, endTime);

		Date startDate = DateUtil.getDateFromInstant(startTime);
		Date endDate = DateUtil.getDateFromInstant(endTime);

		Collection<ConferenceRoom> inventoryOfRooms = bookingRepository.findAll().stream().collect(Collectors.toList());
		inventoryOfRooms = getAvailableConferenceRooms(bookingRequest, inventoryOfRooms);
		ConferenceRoom selectedConferenceRoom = getEligibleConferenceRoom(startDate, endDate, inventoryOfRooms);

		if (selectedConferenceRoom == null) {
			throw new ValidationException("No conference rooms available for the selected period ");
		}

		BookingSlot selectedSlot = HelperUtil.checkAvailability(selectedConferenceRoom.getBookingSlots(), startTime, endTime);

		// 3. Validates that no booking should be done during a maintenance slot
		if (ObjectUtils.isNotEmpty(selectedSlot)) {
			HelperUtil.checkIfMaintenanceSlot(selectedSlot);
		}

		if (ObjectUtils.isNotEmpty(selectedConferenceRoom)) {
			ConferenceRoom bookingCandidate = BookingMapper.prepareEligibleConferenceRoom(startDate, endDate, selectedConferenceRoom);
			ConferenceRoom dbCandidate = bookingRepository.save(bookingCandidate);
			log.info("Saved candidate is : " + dbCandidate);
		}
	}

	private ConferenceRoom getEligibleConferenceRoom(Date startDate, Date endDate, Collection<ConferenceRoom> inventoryOfRooms) {
		return inventoryOfRooms.stream().map(cr -> {
			Set<BookingSlot> bookingSlots = cr
					.getBookingSlots()
					.stream()
					.filter(s -> startDate.after(s.getStartTime()) && endDate.before(s.getEndTime()))
					.collect(Collectors.toSet());
			return cr;
		}).findFirst().orElse(null);
	}

	private Collection<ConferenceRoom> getAvailableConferenceRooms(BookingRequest bookingRequest, Collection<ConferenceRoom> inventoryOfRooms) {
		Instant startTime = bookingRequest.getStartTime();
		Instant endTime = bookingRequest.getEndTime();
		Comparator<ConferenceRoom> capacityComparator = Comparator.comparing(ConferenceRoom::getCapacity, Comparator.nullsFirst(Comparator.naturalOrder()));

		inventoryOfRooms = inventoryOfRooms.stream().map(r -> {
			BookingSlot bookingSlot = HelperUtil.checkAvailability(r.getBookingSlots(), startTime, endTime);
			return ObjectUtils.isEmpty(bookingSlot) ? r : null;
		}).collect(Collectors.toSet()).stream().collect(Collectors.toList());

		inventoryOfRooms = inventoryOfRooms
				.stream()
				.filter(r -> r != null)
				.filter(cr -> !CollectionUtils.isEmpty(cr.getBookingSlots()))
				.filter(room -> room.getCapacity() >= bookingRequest.getNumOfAttendees())
				.sorted(capacityComparator)
				.distinct()
				.collect(Collectors.toList());
		return inventoryOfRooms;
	}

	@Override
	public Collection<ListRoomsResponseDto> listConferenceRooms(ListRoomsRequest listRoomsRequest) {
		Collection<ConferenceRoom> conferenceRooms = bookingRepository.findAll().stream().collect(Collectors.toSet());
		BookingRequest bookingRequest = new BookingRequest(listRoomsRequest.getStartTime(), listRoomsRequest.getEndTime());
		conferenceRooms = getAvailableConferenceRooms(bookingRequest, conferenceRooms);

		return BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
	}
}

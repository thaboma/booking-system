package za.co.interview.study.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import za.co.interview.study.impl.dto.BookingRequestDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

public class BaseTestCase {

	protected Instant instant;

	protected Date startDate;

	protected Date endDate;

	protected List<ConferenceRoom> conferenceRooms;

	@BeforeEach
	void setUp() {
		instant = Instant.now();
		int hour = instant.atZone(ZoneOffset.UTC).getHour();
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE, 15);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);

		startDate = calender.getTime();
		calender.set(Calendar.MINUTE, 30);
		endDate = calender.getTime();
	    conferenceRooms = getConferenceRooms();
	}

	@SneakyThrows
	protected BookingRequestDto getBookingRequest() {

		BookingRequestDto bookingRequest = new BookingRequestDto();
		bookingRequest.setNumOfAttendees(3);
		bookingRequest.setUserId("12345");
		bookingRequest.setStartTime(startDate.toInstant());
		bookingRequest.setEndTime(endDate.toInstant());

		return bookingRequest;
	}

	@SneakyThrows
	protected List<ConferenceRoom> getConferenceRooms() {

		Set<BookingSlot> bookingSlots = new HashSet<>();
		BookingSlot bookingSlot = new BookingSlot();
		bookingSlot.setMaintenanceSlot(true);
		bookingSlot.setId(1L);
		bookingSlot.setRoomId(1L);
		bookingSlot.setCreatedTime(new Date());
		bookingSlot.setStartTime(startDate);
		bookingSlot.setEndTime(endDate);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);

		Date startTime=c.getTime();
		c.set(Calendar.MINUTE, 30);
		Date endTime=c.getTime();

		BookingSlot bookingSlot2 = new BookingSlot();
		bookingSlot2.setMaintenanceSlot(false);
		bookingSlot2.setId(2L);
		bookingSlot.setRoomId(2L);
		bookingSlot2.setCreatedTime(new Date());
		bookingSlot2.setStartTime(startTime);
		bookingSlot2.setEndTime(endTime);

		bookingSlots.add(bookingSlot);
		bookingSlots.add(bookingSlot2);

		ConferenceRoom cr1 = ConferenceRoom.builder().id(1L).name("Amaze").capacity(3).bookingSlots(bookingSlots).build();
		ConferenceRoom cr2 = ConferenceRoom.builder().id(2L).name("Beauty").capacity(7).bookingSlots(bookingSlots).build();
		ConferenceRoom cr3 = ConferenceRoom.builder().id(1L).name("Inspire").capacity(12).bookingSlots(bookingSlots).build();
		ConferenceRoom cr4 = ConferenceRoom.builder().id(1L).name("Strive").capacity(20).bookingSlots(bookingSlots).build();

		List<ConferenceRoom> conferenceRooms = new ArrayList<>();
		conferenceRooms.add(cr1);
		conferenceRooms.add(cr2);
		conferenceRooms.add(cr3);
		conferenceRooms.add(cr4);

		return conferenceRooms;
	}
}

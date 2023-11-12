package za.co.interview.study.impl.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import za.co.interview.study.impl.BaseTestCase;
import za.co.interview.study.impl.dao.BookingRepository;
import za.co.interview.study.impl.dao.MaintenanceSlotsRepository;
import za.co.interview.study.impl.dto.BookingRequestDto;
import za.co.interview.study.impl.dto.ListRoomsRequestDto;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.ConferenceRoom;
import za.co.interview.study.impl.service.impl.BookingServiceImpl;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest extends BaseTestCase {

	@Autowired
	private BookingServiceImpl bookingService;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private MaintenanceSlotsRepository maintenanceSlotsRepository;

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
 		bookingService = new BookingServiceImpl(bookingRepository,maintenanceSlotsRepository);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void bookConferenceRoom() {

		List<ConferenceRoom> conferenceRooms = getConferenceRooms();
 		ReflectionTestUtils.setField(bookingService, "maxCapacity", 20);
		BookingRequestDto bookingRequestDto =getBookingRequest();
		assertThrows(ValidationException.class, () -> bookingService.bookConferenceRoom(bookingRequestDto));
		assertThrows(RuntimeException.class, () -> bookingService.bookConferenceRoom(bookingRequestDto));
		bookingRequestDto.setNumOfAttendees(1);
		assertThrows(ValidationException.class, () -> bookingService.bookConferenceRoom(bookingRequestDto));
		bookingRequestDto.setNumOfAttendees(30);
		assertThrows(ValidationException.class, () -> bookingService.bookConferenceRoom(bookingRequestDto));
		bookingRequestDto.setNumOfAttendees(3);
		Set<ConferenceRoom> conferenceRums =conferenceRooms.stream().collect(Collectors.toSet());
		when(bookingRepository.findAllConferenceRoomsByDates(any(), any())).thenReturn(conferenceRums);
		when(bookingRepository.findConferenceRoomsByBookingSlotsBetween(any(), any())).thenReturn(conferenceRums);
		assertThrows(ValidationException.class, () -> bookingService.bookConferenceRoom(bookingRequestDto));

	}

	@Test
	void listConferenceRooms() {
		Collection<ConferenceRoom> conferenceRooms = getConferenceRooms();
		ListRoomsRequestDto listRoomsRequestDto = getListRoomsRequestDto();
		when(bookingRepository.findAllConferenceRooms()).thenReturn(conferenceRooms.stream().collect(Collectors.toSet()));
		Collection<ListRoomsResponseDto> confRooms = bookingService.listConferenceRooms(listRoomsRequestDto);
		Assert.isTrue(confRooms.size() == 4, "4 Conference rooms are available for this test ");
	}

	private ListRoomsRequestDto getListRoomsRequestDto() {
		ListRoomsRequestDto listRoomsRequestDto = ListRoomsRequestDto
				.builder()
				.startTime(startDate.toInstant())
				.endTime(endDate.toInstant())
				.build();
		return listRoomsRequestDto;
	}
}
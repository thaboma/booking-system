package za.co.interview.study.impl.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import za.co.interview.study.impl.BaseTestCase;
import za.co.interview.study.impl.dto.ConferenceRoomDto;
import za.co.interview.study.impl.dto.ListRoomsRequestDto;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.ConferenceRoom;
import za.co.interview.study.impl.mapper.BookingMapper;
import za.co.interview.study.impl.service.impl.BookingServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest extends BaseTestCase {

	@InjectMocks
	private BookingController bookingController;

	@Mock
	private BookingServiceImpl bookingService;

	@Test
	void bookConferenceRoom() {
		doNothing().when(bookingService).bookConferenceRoom(getBookingRequest());
		bookingController.bookConferenceRoom(getBookingRequest());
	}

	@Test
	void listConferenceRooms() {
		List<ConferenceRoom> conferenceRooms = getConferenceRooms();
		ListRoomsRequestDto listRoomsRequestDto = getListRoomsRequestDto();
		Collection<ListRoomsResponseDto> confRooms = BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
		when(bookingService.listConferenceRooms(listRoomsRequestDto)).thenReturn(confRooms);
		ResponseEntity<Set<ConferenceRoomDto>> outConfRooms = bookingController.listConferenceRooms(listRoomsRequestDto);
		Set<ConferenceRoomDto> conferenceRoomDtos = outConfRooms.getBody();
		Assert.isTrue(conferenceRoomDtos.size() == 1, "1 Conference rooms is available for this test ");
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
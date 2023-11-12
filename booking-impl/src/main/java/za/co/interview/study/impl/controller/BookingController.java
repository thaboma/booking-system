package za.co.interview.study.impl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import za.co.interview.study.impl.dto.BookingRequestDto;
import za.co.interview.study.impl.dto.ConferenceRoomDto;
import za.co.interview.study.impl.dto.ListRoomsRequestDto;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.service.BookingService;
import za.co.interview.study.impl.util.HelperUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("api/v1/bookings")
@CrossOrigin("*")
@Slf4j
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/reserve-room")
	public ResponseEntity<Void> bookConferenceRoom(@Validated @RequestBody BookingRequestDto bookingRequestDto) {
		try {
			HelperUtil.checkForMinCapacity(bookingRequestDto.getNumOfAttendees());
			bookingService.bookConferenceRoom(bookingRequestDto);
			log.info("Conference room has been successfully reserved by {} for a slot from [{}] to [{}]  ", bookingRequestDto.getUserId(), bookingRequestDto.getStartTime(), bookingRequestDto.getEndTime());
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception ex){
			if (ex instanceof HttpClientErrorException){
				return new ResponseEntity(ex.getMessage(),HttpStatus.BAD_REQUEST);
			}
			log.error("User {} failed to reserve for a slot from [{}] to [{}]  ", bookingRequestDto.getUserId(), bookingRequestDto.getStartTime(), bookingRequestDto.getEndTime());
			return new ResponseEntity(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/list-room")
	public ResponseEntity<Set<ConferenceRoomDto>> listConferenceRooms(@Validated @RequestBody ListRoomsRequestDto listRoomsRequestDto) {
		Collection<ListRoomsResponseDto> conferenceRoomDtos = bookingService.listConferenceRooms(listRoomsRequestDto);

		if (!CollectionUtils.isEmpty(conferenceRoomDtos)) {
			log.info("Successfully retrieved {} conference rooms by for the period between {} and {}  ", conferenceRoomDtos.size(), listRoomsRequestDto.getStartTime(), listRoomsRequestDto.getEndTime());
		} else {
			log.info("No conference rooms found for the given period between {} and {}  ", listRoomsRequestDto.getStartTime(), listRoomsRequestDto.getEndTime());
		}

		return new ResponseEntity(Collections.singleton(conferenceRoomDtos), HttpStatus.OK);
	}

}


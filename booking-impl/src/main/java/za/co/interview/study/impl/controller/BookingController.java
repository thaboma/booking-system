package za.co.interview.study.impl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.interview.study.impl.dto.BookingRequest;
import za.co.interview.study.impl.dto.ConferenceRoomDto;
import za.co.interview.study.impl.dto.ListRoomsRequest;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.service.BookingService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("api/v1/booking")
@CrossOrigin("*")
@Slf4j
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/reserve-room")
	public ResponseEntity<Void> bookConferenceRoom(@Validated @RequestBody BookingRequest bookingRequest) {
		bookingService.bookConferenceRoom(bookingRequest);

		log.info("Conference room has been successfully reserved by {} for a slot from [{}] to [{}]  ", bookingRequest.getUserId(), bookingRequest.getStartTime(), bookingRequest.getEndTime());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/list-room")
	public ResponseEntity<Set<ConferenceRoomDto>> listConferenceRooms(@Validated @RequestBody ListRoomsRequest listRoomsRequest) {
		Collection<ListRoomsResponseDto> conferenceRoomDtos = bookingService.listConferenceRooms(listRoomsRequest);

		if (!CollectionUtils.isEmpty(conferenceRoomDtos)) {
			log.info("Successfully retrieved {} conference rooms by for the period between {} and {}  ", conferenceRoomDtos.size(), listRoomsRequest.getStartTime(), listRoomsRequest.getEndTime());
		} else {
			log.info("No conference rooms found for the given period between {} and {}  ", listRoomsRequest.getStartTime(), listRoomsRequest.getEndTime());
		}

		return new ResponseEntity(Collections.singleton(conferenceRoomDtos), HttpStatus.OK);
	}

}


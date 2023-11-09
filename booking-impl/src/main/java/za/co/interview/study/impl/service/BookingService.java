package za.co.interview.study.impl.service;

import za.co.interview.study.impl.dto.BookingRequest;
import za.co.interview.study.impl.dto.ListRoomsRequest;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;

import java.util.Collection;

public interface BookingService {
	// I added an extra parameter userId to reflect the user that initiated the booking , I know this is not on the spec ,
	// it not neccessary but as part of adding a bit of creativity in case we later need to see who created some of the bookings
	void bookConferenceRoom(BookingRequest bookingRequest);

	Collection<ListRoomsResponseDto> listConferenceRooms(ListRoomsRequest listRoomsRequest);
}

package za.co.interview.study.impl.mapper;

import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;

import java.util.*;
import java.util.stream.Collectors;

public class BookingMapper {

	public static ListRoomsResponseDto conferenceRoomToListRoomResponseDto(ConferenceRoom conferenceRoom) {
		return ListRoomsResponseDto
				.builder()
				.roomId(conferenceRoom.getId())
				.name(conferenceRoom.getName())
				.capacity(conferenceRoom.getCapacity())
				.build();
	}

	public static ConferenceRoom prepareEligibleConferenceRoom(Date startDate, Date endDate, ConferenceRoom selectedConferenceRoom) {
		Set<BookingSlot> selectedSlots = new HashSet<>();
		BookingSlot bookingSlot = BookingSlot
				.builder()
				.id(System.currentTimeMillis())
				.roomId(selectedConferenceRoom.getId())
				.startTime(startDate)
				.endTime(endDate)
				.conferenceRoom(selectedConferenceRoom)
				.maintenanceSlot(false)
				.build();
		selectedSlots.add(bookingSlot);

		ConferenceRoom bookingCandidate = ConferenceRoom
				.builder()
				.id(System.currentTimeMillis())
				.bookingSlots(selectedSlots)
				.capacity(selectedConferenceRoom.getCapacity())
				.name(selectedConferenceRoom.getName())
				.build();

		return bookingCandidate;
	}

	public static Collection<ListRoomsResponseDto> conferenceRoomsToListRoomsRoomDtos(Collection<ConferenceRoom> conferenceRooms) {
		return emptyIfNull(conferenceRooms).parallelStream().map(cr -> conferenceRoomToListRoomResponseDto(cr)).collect(Collectors.toSet());
	}

	public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
		return collection == null ? Collections.emptyList() : collection;
	}
}

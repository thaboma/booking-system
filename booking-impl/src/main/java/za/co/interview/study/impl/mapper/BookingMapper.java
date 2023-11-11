package za.co.interview.study.impl.mapper;

import za.co.interview.study.impl.dto.BookingSlotDto;
import za.co.interview.study.impl.dto.ConferenceRoomDto;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;

import java.util.*;
import java.util.stream.Collectors;

public class BookingMapper {

	public static ConferenceRoomDto conferenceRoomToConferenceRoomDto(ConferenceRoom conferenceRoom) {
		return ConferenceRoomDto
				.builder()
				.roomId(conferenceRoom.getId())
				.availableSlots(bookingSlotsToBookingSlotDtos(conferenceRoom.getBookingSlots()))
				.name(conferenceRoom.getName())
				.capacity(conferenceRoom.getCapacity())
				.build();
	}
	public static BookingSlotDto bookingSlotToBookingSlotDto(BookingSlot bookingSlot) {
		return BookingSlotDto
				.builder()
				.duration(bookingSlot.getDuration())
				.startTime(bookingSlot.getStartTime())
				.endTime(bookingSlot.getEndTime())
				.maintenanceSlot(bookingSlot.isMaintenanceSlot())
				.build();
	}

	public static Set<BookingSlotDto> bookingSlotsToBookingSlotDtos(Set<BookingSlot> bookingSlots) {
		return bookingSlots.parallelStream().map(slot-> bookingSlotToBookingSlotDto(slot)).collect(Collectors.toSet());
	}
	public static Collection<ConferenceRoomDto> conferenceRoomsToConferenceRoomDtos(Collection<ConferenceRoom> conferenceRooms) {
		return emptyIfNull(conferenceRooms).parallelStream().map(BookingMapper::conferenceRoomToConferenceRoomDto).collect(Collectors.toSet());
	}

	public static ListRoomsResponseDto conferenceRoomToListRoomResponseDto(ConferenceRoom conferenceRoom) {
		return ListRoomsResponseDto
				.builder()
				.roomId(conferenceRoom.getId())
				.name(conferenceRoom.getName())
				.capacity(conferenceRoom.getCapacity())
				.build();
	}

	public static Collection<ListRoomsResponseDto> conferenceRoomsToListRoomsRoomDtos(Collection<ConferenceRoom> conferenceRooms) {
		return emptyIfNull(conferenceRooms).parallelStream().map(cr -> conferenceRoomToListRoomResponseDto(cr)).collect(Collectors.toSet());
	}

	public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
		return collection == null ? Collections.emptyList() : collection;
	}

	public static BookingSlot getBookingSlot(Date startDate, Date endDate, ConferenceRoomDto eligibleConferenceRoom) {
		BookingSlot selectedSlot = BookingSlot
				.builder()
				.id(System.currentTimeMillis())
				.roomId(eligibleConferenceRoom.getRoomId())
				.startTime(startDate)
				.endTime(endDate)
				.createdTime(new Date())
				.maintenanceSlot(false)
				.build();
		return selectedSlot;
	}
}

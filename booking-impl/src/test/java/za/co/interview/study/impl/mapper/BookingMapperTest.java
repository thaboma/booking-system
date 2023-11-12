package za.co.interview.study.impl.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import za.co.interview.study.impl.BaseTestCase;
import za.co.interview.study.impl.dto.BookingSlotDto;
import za.co.interview.study.impl.dto.ConferenceRoomDto;
import za.co.interview.study.impl.dto.ListRoomsResponseDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.entity.ConferenceRoom;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class BookingMapperTest extends BaseTestCase {

	private Collection<ListRoomsResponseDto> listRoomDtos;
	private List<ConferenceRoom> conferenceRooms;
	private ConferenceRoom selectedConferenceRoom;

	protected Date startDate;
	protected Date endDate;


	@BeforeEach
	void setUp() {
		conferenceRooms =getConferenceRooms();
		listRoomDtos=BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
		selectedConferenceRoom = conferenceRooms.stream().filter(cr->"Beauty".equals(cr.getName())).findFirst().get();
		this.startDate = super.startDate;
		this.endDate = super.endDate;
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void conferenceRoomToListRoomResponseDto() {

		Assert.isTrue(conferenceRooms.size()==4,"4 Conference rooms are available for this test ");
		ListRoomsResponseDto conferenceRoomsDto =BookingMapper.conferenceRoomToListRoomResponseDto(selectedConferenceRoom);
		Assert.isTrue("Beauty".equals(conferenceRoomsDto.getName()),"Checking if indeed we mapped the right info ");
		Assert.isTrue( conferenceRoomsDto.getRoomId()==2,"Checking if indeed we mapped the right info ");
		Assert.isTrue( conferenceRoomsDto.getCapacity()==7,"Checking if indeed we mapped the right info ");
	}

	@Test
	void conferenceRoomsToListRoomsRoomDtos() {

		List<Integer> roomCapacirties =listRoomDtos.parallelStream().map(l->l.getCapacity()).collect(Collectors.toList());
		Assert.isTrue(listRoomDtos.size() ==4,"4 Conference rooms are available for this test ");
		Assert.isTrue(roomCapacirties.containsAll(List.of(3,7,12,20)),"Do we have the expected capacities of rooms");
	}

	@Test
	void emptyIfNull() {
		Collection<String> nullCollection = null;
		Assert.notNull(BookingMapper.emptyIfNull(nullCollection),"Validate that when a null is threated as an empty collection");
	}

	@Test
	void conferenceRoomToConferenceRoomDto() {
 	ConferenceRoomDto conferenceRoomDto=BookingMapper.conferenceRoomToConferenceRoomDto(selectedConferenceRoom);
	Assert.isTrue("Beauty".equals(conferenceRoomDto.getName()),"Checking if indeed we mapped the right info ");
	Assert.isTrue( conferenceRoomDto.getCapacity()==7,"Checking if indeed we mapped the right info ");
	}

	@Test
	void bookingSlotToBookingSlotDto() {
		Set<BookingSlot> bookingSlots= selectedConferenceRoom.getBookingSlots();
		BookingSlotDto bookingSlot=BookingMapper.bookingSlotsToBookingSlotDtos(bookingSlots).stream().findFirst().get();
		Assert.isTrue(!ObjectUtils.isEmpty(bookingSlot),"Checking if the slot was mapped ");
		Assert.isTrue( bookingSlot.getDuration()==0,"Checking if indeed we mapped the right info ");
	}


	@Test
	void bookingSlotsToBookingSlotDtos() {
		Set<BookingSlot> bookingSlots= selectedConferenceRoom.getBookingSlots();
		Set<BookingSlotDto> bookingSlotDtos=BookingMapper.bookingSlotsToBookingSlotDtos(bookingSlots.stream().collect(Collectors.toSet()));

		Assert.isTrue(bookingSlotDtos.size()==2,"Checking if indeed all slots were mapped ");

	}

	@Test
	void conferenceRoomsToConferenceRoomDtos() {

		Assert.isTrue(conferenceRooms.size()==4,"4 Conference rooms are available for this test ");
		ListRoomsResponseDto conferenceRoomsDto =BookingMapper.conferenceRoomToListRoomResponseDto(selectedConferenceRoom);
		Assert.isTrue("Beauty".equals(conferenceRoomsDto.getName()),"Checking if indeed we mapped the right info ");
		Assert.isTrue( conferenceRoomsDto.getRoomId()==2,"Checking if indeed we mapped the right info ");
		Assert.isTrue( conferenceRoomsDto.getCapacity()==7,"Checking if indeed we mapped the right info ");
		Collection<ListRoomsResponseDto> conferenceRoomsDtos =BookingMapper.conferenceRoomsToListRoomsRoomDtos(conferenceRooms);
		Assert.isTrue( conferenceRoomsDtos.size()==4,"Checking all 4 confence rooms have been mapped ");
	}


	@Test
	void getBookingSlot() {
		BookingSlot slot =	BookingMapper.getBookingSlot(startDate,endDate,	BookingMapper.conferenceRoomToConferenceRoomDto(selectedConferenceRoom));
		Assert.isTrue( slot.getRoomId()==2,"Checking if indeed we mapped the right info ");
	}
}
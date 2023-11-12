package za.co.interview.study.impl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import za.co.interview.study.impl.dto.BookingSlotDto;
import za.co.interview.study.impl.entity.BookingSlot;
import za.co.interview.study.impl.mapper.BookingMapper;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HelperUtilTest {

	private Instant startTime;
	private Instant endTime;

	@BeforeEach
	void setUp() {
		startTime = Instant.now();
		endTime = startTime.plus(20, ChronoUnit.MINUTES);
	}

	@Test
	void checkForOverlaps() {
	}

	@Test
	void checkForMaintenanceSlot() {
		BookingSlotDto bookingSlot = BookingMapper.bookingSlotToBookingSlotDto(getBookingSlot(startTime, endTime));
		HelperUtil.checkForMaintenanceSlot(Set.of(bookingSlot),startTime, endTime);
		bookingSlot.setMaintenanceSlot(true);

		Assert.isTrue(!ObjectUtils.isEmpty(bookingSlot), "The booking spot we looking for has been found");
	}

	@Test
	void checkForMinCapacity() {
		int minCapacity = 1;
		assertThrows(HttpClientErrorException.class, () -> HelperUtil.checkForMinCapacity(minCapacity));
	}

	@Test
	void checkForMaxCapacity() {
		int maxCapacity = 10;
		int numOfAttendees=12;
		assertThrows(ValidationException.class, () -> HelperUtil.checkForMaxCapacity(numOfAttendees, maxCapacity));
	}

	private static BookingSlot getBookingSlot(Instant startTime, Instant endTime) {
		Date startDate = DateUtil.getDateFromInstant(startTime);
		Date endDate = DateUtil.getDateFromInstant(endTime);
		BookingSlot bookingSlot = BookingSlot.builder().maintenanceSlot(false).id(5L).roomId(11L)
				.duration(5).startTime(startDate).endTime(endDate).createdTime(new Date()).build();
		return bookingSlot;
	}
}
package za.co.interview.study.impl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import za.co.interview.study.impl.BaseTestCase;
import za.co.interview.study.impl.dto.BookingSlotDto;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DateUtilTest extends BaseTestCase {

	private Instant startTime;
	private Instant endTime;

	@BeforeEach
	void setUp() {
		startTime = Instant.now();
		endTime = startTime.plus(20, ChronoUnit.MINUTES);
	}

	@Test
	void getDateFromInstant() {
		Date startDate = DateUtil.getDateFromInstant(startTime);
		Assert.isTrue(startDate.getTime() > 0, "The returned time is a numeral greater than 0 ");
	}

	@Test
	void getInstantDiff() {
		int dateDiff = DateUtil.getInstantDiff(startTime, endTime);
		Assert.isTrue(dateDiff == 0, "The difference = 0 since its the same day ");
	}

	@Test
	void getDiffInMinutes() {
		int minutes = DateUtil.getDiffInMinutes(startTime, endTime);
		int hoursDiff = endTime.atZone(ZoneOffset.UTC).getHour() - startTime.atZone(ZoneOffset.UTC).getHour();
		DateUtil.validateForCurrentDate(startTime, endTime);
		Assert.isTrue(hoursDiff <= 1, "The difference in hours for the 2 Instants ");
		Assert.isTrue(Math.abs(minutes) <= 60, "The difference in minutes for the 2 Instants ");
	}

	@Test
	void validateInterval() {
		assertThrows(ValidationException.class, () -> DateUtil.validateInterval(startTime, endTime));
	}

	@Test
	void validateForCurrentDate() {
		endTime = startTime.plus(20, ChronoUnit.DAYS);
		assertThrows(ValidationException.class, () -> DateUtil.validateForCurrentDate(startTime, endTime));
	}

	@Test
	void testGetDateFromInstant() {
		Date convertedDate = DateUtil.getDateFromInstant(startTime);
		Assert.notNull(convertedDate, "The date was converted successfully ");
	}

	@Test
	void testGetInstantDiff() {
		int diff = DateUtil.getInstantDiff(startTime,endTime);
		Assert.notNull(diff <0, "The value is negative since startTime < endTime");
	}

	@Test
	void testGetDiffInMinutes() {
		int diff = DateUtil.getInstantDiff(startTime,endTime);
		Assert.notNull(diff <0, "The value is negative since startTime < endTime");
	}

	@Test
	void isThereOverlap() {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.MINUTE, 30);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);

		Date st=calender.getTime();
		calender.set(Calendar.MINUTE,  45);
		Date ed=calender.getTime();

		BookingSlotDto slotDto =BookingSlotDto.builder().startTime(st).endTime(ed).build();

		boolean overlap =DateUtil.isThereOverlap(slotDto,st.toInstant(),ed.toInstant());
		Assert.isTrue(overlap, "This should is an overlapping period ");

		calender.set(Calendar.MINUTE, 0);
		st=calender.getTime();
		boolean overLaps=DateUtil.isThereOverlap(slotDto,st.toInstant(),ed.toInstant());
		Assert.isTrue(overLaps, "This should is overlapping period ");
	}

	@Test
	void isMantenanaceSlot() {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.MINUTE, 15);
		calender.set(Calendar.SECOND, 0);
		calender.set(Calendar.MILLISECOND, 0);

		Date st=calender.getTime();
		calender.set(Calendar.MINUTE, 30);
		Date ed=calender.getTime();

		BookingSlotDto slotDto =BookingSlotDto.builder().startTime(st).endTime(ed).build();
		boolean coincides=DateUtil.isMaintenanceSlot(slotDto,st.toInstant(),ed.toInstant());
		Assert.isTrue(coincides, "This should be the same slot ");
	}
}
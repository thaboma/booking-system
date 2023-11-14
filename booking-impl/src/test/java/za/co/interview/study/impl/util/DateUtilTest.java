package za.co.interview.study.impl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import za.co.interview.study.impl.BaseTestCase;
import za.co.interview.study.impl.dto.Interval;
import za.co.interview.study.impl.dto.Moment;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

		String startDateSt= "2009-04-22 09:00";
		String endDateSt= "2009-04-22 09:15";

		Instant startDate=null;
		Instant endDate=null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			startDate = DateUtil.getCurrentDayInstant(formatter.parse(startDateSt).toInstant());
			endDate= DateUtil.getCurrentDayInstant(formatter.parse(endDateSt).toInstant());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Interval slotDto =new Interval(new Moment(9,00),new Moment(9,15));

		boolean overlap =DateUtil.isThereOverlap(slotDto,startDate,endDate);
		Assert.isTrue(!overlap, "This should is an overlapping period ");
	}
}
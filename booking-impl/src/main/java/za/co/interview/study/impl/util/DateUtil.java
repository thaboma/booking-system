package za.co.interview.study.impl.util;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getDateFromInstant(Instant instant) {

		int hour = instant.atZone(ZoneOffset.UTC).getHour();
		int min = instant.atZone(ZoneOffset.UTC).getMinute();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	public static int getInstantDiff(Instant instant, Instant compareTo) {
		return instant.truncatedTo(ChronoUnit.DAYS).compareTo(compareTo.truncatedTo(ChronoUnit.DAYS));
	}

	public static int getDiffInMinutes(Instant instant, Instant compareTo) {
		return compareTo.atZone(ZoneOffset.UTC).getMinute() - instant.atZone(ZoneOffset.UTC).getMinute();
	}

	public static void validateInterval(Instant instant, Instant compareTo) {
		int minutes = DateUtil.getDiffInMinutes(instant, compareTo);
		int interval = (Math.abs(minutes) % 15);

		if (interval != 0 || minutes == 0) {
			throw new ValidationException("Booking can be done only in intervals of 15 mins ");
		}
	}

	public static void validateForCurrentDate(Instant instant, Instant compareTo) {
		int dateDiff = DateUtil.getInstantDiff(instant, compareTo);
		dateDiff += DateUtil.getInstantDiff(Instant.now(), compareTo);

		if (dateDiff != 0) {
			throw new ValidationException("Booking can be done only for the current date ");
		}
	}

}

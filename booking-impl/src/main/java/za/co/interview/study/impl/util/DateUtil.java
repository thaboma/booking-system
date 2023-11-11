package za.co.interview.study.impl.util;

import za.co.interview.study.impl.dto.BookingSlotDto;
import za.co.interview.study.impl.dto.Interval;
import za.co.interview.study.impl.dto.Moment;
import za.co.interview.study.impl.entity.BookingSlot;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class DateUtil {

	public static Date getDateFromInstant(Instant instant) {

		int year = instant.atZone(ZoneOffset.UTC).getYear();
		int hour = instant.atZone(ZoneOffset.UTC).getHour();
		int min = instant.atZone(ZoneOffset.UTC).getMinute();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
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

	public static int getDiffInHors(Instant instant, Instant compareTo) {
		return compareTo.atZone(ZoneOffset.UTC).getHour() - instant.atZone(ZoneOffset.UTC).getHour();
	}

	public static void validateInterval(Instant instant, Instant compareTo) {
		int hours = DateUtil.getDiffInHors(instant, compareTo);
		int minutes = DateUtil.getDiffInMinutes(instant, compareTo);
		int interval = (Math.abs(minutes) % 15);
		boolean isSameTime= (hours ==0 && minutes ==0);

		if (isSameTime) {
			throw new ValidationException("The start time cannot be the same as the end time");
		}

		if (interval != 0) {
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

	public static boolean isThereOverlap(BookingSlotDto bookingSlot, Instant startDate ,Instant endDate) {

		Interval firstInterval = new Interval();
		Moment firstBookedMoment = getMomentFromDate(bookingSlot.getStartTime());
		Moment secondBookedMoment = getMomentFromDate(bookingSlot.getEndTime());
		firstInterval.setBegin(firstBookedMoment);
		firstInterval.setEnd(secondBookedMoment);

		Interval secondInterval = new Interval();
		Moment firstRequestedMoment = getMomentFromInstant(startDate);
		Moment secondRequestedMoment = getMomentFromInstant(endDate);
		secondInterval.setBegin(firstRequestedMoment);
		secondInterval.setEnd(secondRequestedMoment);

		Comparator<Moment> capacityComparator = Comparator.comparing(Moment::toString, Comparator.nullsFirst(Comparator.naturalOrder()));

		boolean endsBeforeStart = capacityComparator.compare(firstInterval.getEnd(),secondInterval.getBegin()) > 0;
		boolean startBeforeEnd  = capacityComparator.compare(firstInterval.getBegin(),secondInterval.getEnd()) <0;
		boolean result  =  !endsBeforeStart || startBeforeEnd;

		return !result;
	}

	public static boolean isThereOverlap(BookingSlot bookingSlot, Instant startDate ,Instant endDate) {

		Interval firstInterval = new Interval();
		Moment firstBookedMoment = getMomentFromDate(bookingSlot.getStartTime());
		Moment secondBookedMoment = getMomentFromDate(bookingSlot.getEndTime());
		firstInterval.setBegin(firstBookedMoment);
		firstInterval.setEnd(secondBookedMoment);

		Interval secondInterval = new Interval();
		Moment firstRequestedMoment = getMomentFromInstant(startDate);
		Moment secondRequestedMoment = getMomentFromInstant(endDate);
		secondInterval.setBegin(firstRequestedMoment);
		secondInterval.setEnd(secondRequestedMoment);

		Comparator<Moment> capacityComparator = Comparator.comparing(Moment::toString, Comparator.nullsFirst(Comparator.naturalOrder()));

		boolean endsBeforeStart = capacityComparator.compare(firstInterval.getEnd(),secondInterval.getBegin()) > 0;
		boolean startBeforeEnd  = capacityComparator.compare(firstInterval.getBegin(),secondInterval.getEnd()) <0;
		boolean result  =  !endsBeforeStart || startBeforeEnd;
		return !result;
	}

		private static Moment getMomentFromDate(Date date){
		int hours = date.toInstant().atZone(ZoneOffset.systemDefault()).getHour();
		int minutes = date.toInstant().atZone(ZoneOffset.systemDefault()).getMinute();

		return Moment
			.builder()
			.hour(hours)
			.min(minutes)
			.build();
	}

	private static Moment getMomentFromInstant(Instant instant){

		int hours = instant.atZone(ZoneOffset.UTC).getHour();
		int minutes = instant.atZone(ZoneOffset.UTC).getMinute();

		return Moment
				.builder()
				.hour(hours)
				.min(minutes)
				.build();
	}
}

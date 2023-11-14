package za.co.interview.study.impl.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import za.co.interview.study.impl.dto.Interval;
import za.co.interview.study.impl.mapper.BookingMapper;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Set;

public class HelperUtil {

	public static Interval checkForOverlaps(Set<Interval> bookingSlots, Instant startTime, Instant endTime) {
		return BookingMapper.emptyIfNull(bookingSlots).parallelStream().filter(s -> DateUtil.isThereOverlap(s, startTime, endTime)).findFirst().orElse(null);
	}

	public static void checkForMinCapacity(int capacity) {
		if (capacity <= 1) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"The number of people allowed for booking a room should be greater than 1 ");
		}
	}

	public static void checkForMaxCapacity(int capacity, int maxCapacity) {
		if (capacity > maxCapacity) {
			throw new ValidationException(String.format("The number of people allowed for booking a room should be  less than maximum available capacity of %s ", maxCapacity));
		}
	}
}

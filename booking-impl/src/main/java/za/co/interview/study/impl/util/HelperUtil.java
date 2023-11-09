package za.co.interview.study.impl.util;

import za.co.interview.study.impl.entity.BookingSlot;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class HelperUtil {

	public static BookingSlot checkAvailability(Set<BookingSlot> bookingSlots, Instant startTime, Instant endTime) {
		BookingSlot slot = bookingSlots.parallelStream().filter(s -> isSlotAvailable(s, startTime, endTime)).findFirst().orElse(null);
		return slot;
	}

	public static void checkIfMaintenanceSlot(BookingSlot bookingSlot) {
		boolean isMaintenanceSlot = (bookingSlot != null) && bookingSlot.isMaintenanceSlot();

		if (isMaintenanceSlot) {
			throw new ValidationException("The selected period overlaps with one of the maintenance slots  ");
		}
	}

	public static boolean isSlotAvailable(BookingSlot slot, Instant startTime, Instant endTime) {

		// check if the slot has not been reserved yet
		Date searchStartDate = DateUtil.getDateFromInstant(startTime);
		Date searchEndDate = DateUtil.getDateFromInstant(endTime);

		Date bookedStartDate = slot.getStartTime();
		Date bookedEndDate = slot.getEndTime();

		int startDateComparison = searchStartDate.compareTo(bookedStartDate);
		int endDateComparison = searchEndDate.compareTo(bookedEndDate);
		boolean bookedSlot = (startDateComparison == 0) && (endDateComparison == 0);

		return bookedSlot;
	}
}

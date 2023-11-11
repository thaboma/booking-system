package za.co.interview.study.impl.util;

import za.co.interview.study.impl.entity.BookingSlot;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Set;

public class HelperUtil {

//	public static BookingSlot checkAvailability(Set<BookingSlot> bookingSlots, Instant startTime, Instant endTime) {
//		BookingSlot slot = bookingSlots.parallelStream().filter(s -> isSlotAvailable(s, startTime, endTime)).findFirst().orElse(null);
//		return slot;
//	}

	public static BookingSlot checkForOverlaps(Set<BookingSlot> bookingSlots, Instant startTime, Instant endTime) {
		BookingSlot slot = bookingSlots.parallelStream().filter(s -> DateUtil.isThereOverlap(s, startTime, endTime)).findFirst().orElse(null);
		return slot;
	}

//	public static BookingSlotDto checkForOverlaps(Set<BookingSlotDto> bookingSlots, Instant startTime, Instant endTime) {
//		BookingSlotDto slot = bookingSlots.parallelStream().filter(s -> DateUtil.isThereOverlap(s, startTime, endTime)).findFirst().orElse(null);
//		return slot;
//	}
	public static void checkIfMaintenanceSlot(BookingSlot bookingSlot) {
		boolean isMaintenanceSlot = (bookingSlot != null) && bookingSlot.isMaintenanceSlot();

		if (isMaintenanceSlot) {
			throw new ValidationException("The selected period overlaps with one of the maintenance slots  ");
		}
	}

	public static void checkForMinCapacity(int capacity,int maxCapacity) {

		if (capacity <=1) {
			throw new ValidationException("The number of people allowed for booking should be greater than 1 ");
		}

		if (capacity > maxCapacity) {
			throw new ValidationException(String.format("The number of people allowed for booking should be  less than maximum available capacity of %s ",maxCapacity));
		}
	}

//	public static boolean checkIfMaintenanceSlot(BookingSlot slot, Instant startTime, Instant endTime) {
//
//		// check if the slot has not been reserved yet
////		Date searchStartDate = DateUtil.getDateFromInstant(startTime);
////		Date searchEndDate = DateUtil.getDateFromInstant(endTime);
////
////		Date bookedStartDate = slot.getStartTime();
////		Date bookedEndDate = slot.getEndTime();
////
////		int startDateComparison = searchStartDate.compareTo(bookedStartDate);
////		int endDateComparison = searchEndDate.compareTo(bookedEndDate);
////		boolean bookedSlot = (startDateComparison == 0) && (endDateComparison == 0);
//
//		Date bookedStartDate = slot.getStartTime();
//		Date bookedEndDate = slot.getEndTime();
//
//		int bookedHour = bookedStartDate.toInstant().atZone(ZoneOffset.systemDefault()).getHour();
//		int bookedMin = bookedEndDate.toInstant().atZone(ZoneOffset.systemDefault()).getMinute();
//
//		int searchHour = startTime.atZone(ZoneOffset.UTC).getHour();
//		int searchMin = endTime.atZone(ZoneOffset.UTC).getMinute();
//
//		int startDateComparison = bookedHour-searchHour;
//		int endDateComparison = bookedMin-searchMin;
//		boolean bookedSlot =(startDateComparison-endDateComparison)==0;
//
//		return bookedSlot;
//	}


//	public static boolean isSlotAvailable(BookingSlot slot, Instant startTime, Instant endTime) {
//
//		// check if the slot has not been reserved yet
////		Date searchStartDate = DateUtil.getDateFromInstant(startTime);
////		Date searchEndDate = DateUtil.getDateFromInstant(endTime);
////
////		Date bookedStartDate = slot.getStartTime();
////		Date bookedEndDate = slot.getEndTime();
////
////		int startDateComparison = searchStartDate.compareTo(bookedStartDate);
////		int endDateComparison = searchEndDate.compareTo(bookedEndDate);
////		boolean bookedSlot = (startDateComparison == 0) && (endDateComparison == 0);
//
////		if (slot.)
//
//		Date bookedStartDate = slot.getStartTime();
//		Date bookedEndDate = slot.getEndTime();
//
//		int bookedHour = bookedStartDate.toInstant().atZone(ZoneOffset.systemDefault()).getHour();
//		int bookedMin = bookedEndDate.toInstant().atZone(ZoneOffset.systemDefault()).getMinute();
//
//		int searchHour = startTime.atZone(ZoneOffset.UTC).getHour();
//		int searchMin = endTime.atZone(ZoneOffset.UTC).getMinute();
//
//		int startDateComparison = bookedHour-searchHour;
//		int endDateComparison = bookedMin-searchMin;
//		boolean bookedSlot =(startDateComparison-endDateComparison)==0;
//
//		return bookedSlot;
//	}
}

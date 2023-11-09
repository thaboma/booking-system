package za.co.interview.study.booking.api;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public interface Slot {
	Instant getStartTime();
}

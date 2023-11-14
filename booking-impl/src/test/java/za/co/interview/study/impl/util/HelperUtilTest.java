package za.co.interview.study.impl.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
}
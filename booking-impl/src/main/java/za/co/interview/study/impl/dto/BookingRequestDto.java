package za.co.interview.study.impl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto extends ListRoomsRequestDto {

	String userId;
	int numOfAttendees;

	public BookingRequestDto(@NotNull Instant startTime, @NotNull Instant endTime) {
		super(startTime, endTime);
	}

	public Instant getStartTime() {
		return startTime.truncatedTo(ChronoUnit.MINUTES);
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
	}

	public Instant getEndTime() {
		return endTime.truncatedTo(ChronoUnit.MINUTES);
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
	}
}

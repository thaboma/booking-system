package za.co.interview.study.impl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListRoomsRequestDto {
	@NotNull
	Instant startTime;
	@NotNull
	Instant endTime;
}

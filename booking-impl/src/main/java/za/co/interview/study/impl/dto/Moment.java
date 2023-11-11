package za.co.interview.study.impl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moment {
	int hour;
	int min;

	@Override public String toString() {
		return "momentDto{" +
				"hour='" + hour + '\'' +
				", min='" + min + '\'' +
				'}';
	}
}

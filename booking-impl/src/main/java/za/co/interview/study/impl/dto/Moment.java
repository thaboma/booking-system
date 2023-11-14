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

	public int getValue() {
		String hr= (hour <10) ? "0"+hour : String.valueOf(hour);
		String minute= (min <10) ? "0"+min : String.valueOf(min);
		return Integer.parseInt(String.valueOf(String.format("%s%s",hr,minute)));
	}
}

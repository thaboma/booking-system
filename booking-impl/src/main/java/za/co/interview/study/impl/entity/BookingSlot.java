package za.co.interview.study.impl.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "booking_slot")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSlot implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "room_id", referencedColumnName = "id", updatable = false, insertable = false

	)
	ConferenceRoom conferenceRoom;
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "room_id")
	private Long roomId;
	@Column(name = "duration", nullable = false)
	private int duration;
	@Column(name = "created_time")
	private Date createdTime;
	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "end_time")
	private Date endTime;
	@Column(name = "maintenance_slot")
	private boolean maintenanceSlot;

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BookingSlot that = (BookingSlot) o;
		return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
	}

	@Override public int hashCode() {
		return Objects.hash(startTime, endTime);
	}
}

package za.co.interview.study.impl.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "conference_room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceRoom implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "capacity")
	private int capacity;

	@OneToMany(mappedBy = "conferenceRoom", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	private Set<BookingSlot> bookingSlots = new HashSet<>();

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ConferenceRoom that = (ConferenceRoom) o;
		return capacity == that.capacity && Objects.equals(id, that.id) && Objects.equals(name, that.name);
	}

	@Override public int hashCode() {
		return Objects.hash(id, name, capacity);
	}

//	@Override public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		ConferenceRoom that = (ConferenceRoom) o;
//		return capacity == that.capacity;
//	}
//
//	@Override public int hashCode() {
//		return Objects.hash(capacity);
//	}
}

package za.co.interview.study.impl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.interview.study.impl.entity.ConferenceRoom;

import java.util.Date;
import java.util.Set;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<ConferenceRoom, Long> {

	@Query("FROM ConferenceRoom AS c "
		+ " LEFT JOIN FETCH c.bookingSlots AS s "
		+ " WHERE s.startTime >= ?1 AND s.endTime <= ?2 ")
	Set<ConferenceRoom> findConferenceRoomsByBookingSlotsBetween(Date startDate, Date endDate);

	@Query("FROM ConferenceRoom AS c "
			+ " LEFT JOIN FETCH c.bookingSlots AS s ")
	Set<ConferenceRoom> findAllConferenceRooms();


	@Query("FROM ConferenceRoom AS c "
			+ " LEFT JOIN FETCH c.bookingSlots AS s "
			+ " WHERE s.startTime >= ?1 AND s.endTime <= ?2 "
	       + " OR s.maintenanceSlot =true ")
	Set<ConferenceRoom> findAllConferenceRoomsByDates(Date startDate, Date endDate);

	@Query(value="select max(capacity) from conference_room ", nativeQuery = true)
	int getMaxCapacity();

}

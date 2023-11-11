package za.co.interview.study.impl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.interview.study.impl.entity.BookingSlot;

import java.util.Date;
import java.util.Set;

@Repository("maintenanceSlots")
public interface MaintenanceSlotsRepository extends JpaRepository<BookingSlot, Long> {

	@Query("FROM BookingSlot AS b "
		+ " WHERE b.maintenanceSlot =true"
		+ " ORDER BY b.startTime ")
	Set<BookingSlot> findMaintenanceSlots();

	@Query("FROM BookingSlot AS b "
			+  "WHERE b.startTime >= ?1 AND b.endTime <= ?2 "
			+  "OR b.maintenanceSlot =true "
	)
	Set<BookingSlot> findBookedSlots(Date startDate , Date endDate);



}

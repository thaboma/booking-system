package za.co.interview.study.impl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.interview.study.impl.entity.BookingSlot;

@Repository("maintenanceSlots")
public interface MaintenanceSlotsRepository extends JpaRepository<BookingSlot, Long> {
}

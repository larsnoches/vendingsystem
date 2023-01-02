package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	Optional<Seat> findByName(String name);

	Page<Seat> findAllByBusTripId(Long id, Pageable pageable);

	List<Seat> findAllByBusTripId(Long id);

	List<Seat> findAllByBusTripArrivalDateTimeGreaterThanAndBusTripBusId(Timestamp dateTime, Long busId);

}

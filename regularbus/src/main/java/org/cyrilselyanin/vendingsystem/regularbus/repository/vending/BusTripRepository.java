package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

public interface BusTripRepository extends JpaRepository<BusTrip, Long> {

	Page<BusTrip> findAllByBusCarrierId(Long id, Pageable pageable);
	Page<BusTrip> findAllByArrivalBusPointNameContainsIgnoreCaseAndDepartureDateTimeGreaterThanEqual(
		String busPointName, Timestamp departureDateTime, Pageable pageable
	);

}

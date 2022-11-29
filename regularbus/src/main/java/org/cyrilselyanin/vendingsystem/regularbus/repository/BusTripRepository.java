package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.BusTrip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusTripRepository extends JpaRepository<BusTrip, Long> {
}

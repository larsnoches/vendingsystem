package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}

package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusPointRepository extends JpaRepository<BusPoint, Long> {
}

package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
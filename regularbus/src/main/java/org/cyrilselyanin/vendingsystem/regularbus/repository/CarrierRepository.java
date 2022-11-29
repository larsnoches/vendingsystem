package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
}
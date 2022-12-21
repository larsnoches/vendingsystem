package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
}
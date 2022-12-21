package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Fare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareRepository extends JpaRepository<Fare, Long> {

	Page<Fare> findAllByCarrierId(Long id, Pageable pageable);

}

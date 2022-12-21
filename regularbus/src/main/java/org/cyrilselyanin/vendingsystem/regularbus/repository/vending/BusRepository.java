package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {

	Optional<Bus> findByCarrierId(Long id);
	Page<Bus> findAllByCarrierId(Long id, Pageable pageable);

}
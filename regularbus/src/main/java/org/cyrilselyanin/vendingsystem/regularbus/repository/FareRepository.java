package org.cyrilselyanin.vendingsystem.regularbus.repository;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareRepository extends JpaRepository<Fare, Long> {
}

package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

	Optional<Ticket> findByIdAndUserEmail(Long ticketId, String email);
	Optional<Ticket> findByQrCode(String qrCode);
	Page<Ticket> findAllByUserEmail(String email, Pageable pageable);

}

package org.cyrilselyanin.vendingsystem.regularbus.repository.vending;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

	Optional<Ticket> findByIdAndEmail(Long ticketId, String email);
	Optional<Ticket> findByQrCode(String qrCode);
	Optional<Ticket> findByIdAndQrCode(Long ticketId, String qrCode);
	Page<Ticket> findAllByEmail(String email, Pageable pageable);

}

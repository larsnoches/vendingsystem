package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetPayedTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.awt.image.BufferedImage;

public interface TicketService {

    String createTicket(BasicTicketRequestDto dto) throws JsonProcessingException;

    void updateTicketStatusAsPayed(String qrCode);
    GetPayedTicketResponseDto getPayedTicket(String qrCode);

    @PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == email)")
    GetTicketResponseDto getTicket(Long ticketId, String email);

    @PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == email)")
    Page<GetTicketResponseDto> getAllTickets(String email, Pageable pageable);

    @PreAuthorize("hasRole('MANAGER') or (hasRole('USER') and principal.username == dto.email)")
    void updateTicket(Long ticketId, BasicTicketRequestDto dto);

    @PreAuthorize("hasRole('MANAGER')")
    void removeTicket(Long id);

    void regCash(Ticket ticket);

    BufferedImage generateQrCode(Long ticketId, String qrCode) throws Exception;

}

package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketCacheDto;

public interface CashRegisterService {
    void regCash(TicketCacheDto ticketCacheDto);
}

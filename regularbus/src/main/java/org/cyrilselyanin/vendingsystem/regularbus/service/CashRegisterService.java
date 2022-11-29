package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;

public interface CashRegisterService {
    void regCash(Ticket ticket);
}

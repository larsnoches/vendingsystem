package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;

public interface CashRegisterService {
    void regCash(Ticket ticket);
}

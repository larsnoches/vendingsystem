package org.cyrilselyanin.vendingsystem.regularbus.validation.notfound;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Couldn't find ticket {}".formatted(id));
    }
}

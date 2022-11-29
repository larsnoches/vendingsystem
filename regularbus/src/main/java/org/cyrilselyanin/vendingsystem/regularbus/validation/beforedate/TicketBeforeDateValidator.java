package org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate;

import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TicketBeforeDateValidator implements ConstraintValidator<TicketValidateBeforeDate, Ticket> {

    @Override
    public void initialize(TicketValidateBeforeDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Ticket ticket, ConstraintValidatorContext context) {
        if (ticket == null) {
            return true;
        }

        return ticket.getDepartureDateTime().compareTo(
                ticket.getArrivalDatetime()
        ) < 0;
    }
}

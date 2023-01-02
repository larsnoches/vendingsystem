package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.TicketDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Cash register communication service.
 * It sends via rabbitMQ a dto to the cashreg subsystem.
 */
@Service
public class CashRegisterServiceImpl implements CashRegisterService {
    private final RabbitTemplate rabbitTemplate;

    public CashRegisterServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Got ticket, adapt it and send for registering
     * @param ticket Ticket instance
     */
    @Override
    public void regCash(Ticket ticket) {
        TicketDtoAdapter ticketDtoAdapter = new TicketDtoAdapter(ticket);
        TicketDto ticketDto = ticketDtoAdapter.adapt();
        // rabbit
        rabbitTemplate.convertAndSend(ticketDto);
    }

    /**
     * Ticket dto adapter
     */
    public static class TicketDtoAdapter {
        private final Ticket adaptee;

        public TicketDtoAdapter(Ticket adaptee) {
            this.adaptee = adaptee;
        }
        public TicketDto adapt() {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setPassengerLastname(adaptee.getPassengerLastname());
            ticketDto.setPassengerFirstname(adaptee.getPassengerFirstname());
            ticketDto.setPassengerMiddlename(adaptee.getPassengerMiddlename());
            ticketDto.setBusRouteNumber(adaptee.getBusRouteNumber());
            ticketDto.setDepartureBuspointName(adaptee.getDepartureBuspointName());
            ticketDto.setDepartureDateTime(adaptee.getDepartureDateTime());
            ticketDto.setPrice(adaptee.getPrice());
            return ticketDto;
        }
    }
}

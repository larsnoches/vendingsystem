package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketCacheDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.TimeUtil;
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
     * @param ticketCacheDto Ticket cach dto instance
     */
    @Override
    public void regCash(TicketCacheDto ticketCacheDto) {
        TicketDtoAdapter ticketDtoAdapter = new TicketDtoAdapter(ticketCacheDto);
        TicketDto ticketDto = ticketDtoAdapter.adapt();
        // rabbit
        rabbitTemplate.convertAndSend(ticketDto);
    }

    /**
     * Ticket dto adapter
     */
    public static class TicketDtoAdapter {
        private final TicketCacheDto adaptee;

        public TicketDtoAdapter(TicketCacheDto adaptee) {
            this.adaptee = adaptee;
        }
        public TicketDto adapt() {
            TicketDto ticketDto = new TicketDto();
            Ticket ticket = adaptee.getTicket();
            GetBusTripResponseDto busTripDto = adaptee.getBusTripDto();

            ticketDto.setPassengerLastname(ticket.getPassengerLastname());
            ticketDto.setPassengerFirstname(ticket.getPassengerFirstname());
            ticketDto.setPassengerMiddlename(ticket.getPassengerMiddlename());
            ticketDto.setBusRouteNumber(busTripDto.getBusRouteNumber());
            ticketDto.setDepartureBuspointName(busTripDto.getDepartureBusPoint().getName());
            ticketDto.setDepartureDateTime(TimeUtil.createTimestamp(
                    String.format("%s %s", busTripDto.getDepartureDate(), busTripDto.getDepartureTime()))
            );
            ticketDto.setPrice(ticket.getPrice());
            return ticketDto;
        }
    }
}

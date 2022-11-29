package org.cyrilselyanin.vendingsystem.regularbus.service;

import org.cyrilselyanin.vendingsystem.regularbus.domain.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.repository.BusTripRepository;
import org.cyrilselyanin.vendingsystem.regularbus.repository.TicketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final BusTripRepository busTripRepository;
    private final CashRegisterService cashRegisterService;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            BusTripRepository busTripRepository,
            CashRegisterService cashRegisterService
    ) {
        this.ticketRepository = ticketRepository;
        this.busTripRepository = busTripRepository;
        this.cashRegisterService = cashRegisterService;
    }

    @Override
    public Ticket create(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> getTicket(Long ticketId) {
        if (isAdmin()) return ticketRepository.findById(ticketId);
        if (isUser()) {
            String userId = getUserId();
            return ticketRepository.findByIdAndUserId(ticketId, userId);
        }

        return Optional.empty();
    }

    @Override
    public List<Ticket> getAllTickets() {
        if (isAdmin()) return ticketRepository.findAll();
        if (isUser()) {
            String userId = getUserId();
            return ticketRepository.findAllByUserId(userId);
        }

        return List.of();
    }

    @Override
    public Ticket update(Ticket ticket, Long id) {
        return ticketRepository.findById(id)
                .map(t -> {
                   t.setIssueDateTime(ticket.getIssueDateTime());
                   t.setPassengerLastname(ticket.getPassengerLastname());
                   t.setPassengerFirstname(ticket.getPassengerFirstname());
                   t.setPassengerMiddlename(ticket.getPassengerMiddlename());
                   t.setBusRouteNumber(ticket.getBusRouteNumber());
                   t.setQrCode(ticket.getQrCode());
                   t.setSeatName(ticket.getSeatName());
                   t.setCarrierName(ticket.getCarrierName());
                   t.setDepartureBuspointName(ticket.getDepartureBuspointName());
                   t.setArrivalBuspointName(ticket.getArrivalBuspointName());
                   t.setDepartureDateTime(ticket.getDepartureDateTime());
                   t.setArrivalDatetime(ticket.getArrivalDatetime());
                   t.setPrice(ticket.getPrice());
                   t.setBusTrip(ticket.getBusTrip());
                   t.setUserId(ticket.getUserId());
                   return ticketRepository.save(t);
                })
                .orElseGet(() -> {
                    ticket.setId(id);
                    return ticketRepository.save(ticket);
                });
    }

    @Override
    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Optional<BusTrip> getTicketBusTrip(Long ticketId) {
        if (isAdmin()) {
            return ticketRepository.findById(ticketId)
                    .map(Ticket::getBusTrip);
        }
        if (isUser()) {
            String userId = getUserId();
            return ticketRepository.findByIdAndUserId(ticketId, userId)
                    .map(Ticket::getBusTrip);
        }

        return Optional.empty();
    }

    private boolean isUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }

    private String getUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean isAdmin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Reg cash via cashRegister service
     * @param ticket Ticket instance
     */
    @Override
    public void regCash(Ticket ticket) {
        cashRegisterService.regCash(ticket);
    }
}

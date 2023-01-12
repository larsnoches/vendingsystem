package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.UserRole;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.TicketStatus;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetPayedTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketCacheDto;
import org.cyrilselyanin.vendingsystem.regularbus.helper.QrCodeUtil;
import org.cyrilselyanin.vendingsystem.regularbus.helper.TicketDataMapper;
import org.cyrilselyanin.vendingsystem.regularbus.helper.TimeUtil;
import org.cyrilselyanin.vendingsystem.regularbus.repository.vending.TicketRepository;
import org.cyrilselyanin.vendingsystem.regularbus.service.CashRegisterService;
import org.cyrilselyanin.vendingsystem.regularbus.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_NOT_FOUND_MESSAGE = "Билет %d не найден в базе данных.";
    private static final String TICKET_QRCODE_NOT_FOUND_MESSAGE = "Билет с кодом %s не найден в базе данных.";
    private static final String TICKET_NOT_FOUND_LOG_MESSAGE = "Ticket {} not found in the database.";
    private static final String WRONG_EMAIL_MESSAGE = "Неверный адрес электронной почты %s.";
    private static final String WRONG_EMAIL_LOG_MESSAGE = "Wrong user email {}.";

    private final TicketRepository ticketRepo;
    private final TicketDataMapper ticketDataMapper;
    private final UserService userService;
    private final BusTripService busTripService;
    private final SeatService seatService;
    private final PaymentService paymentService;
    private final Map<String, TicketCacheDto> ticketMap;

    private final CashRegisterService cashRegisterService;

    @Override
    public String createTicket(BasicTicketRequestDto dto) throws JsonProcessingException {
        log.info("Create ticket for {} {} {}, bustrip {}",
                dto.getPassengerFirstname(),
                dto.getPassengerMiddlename(),
                dto.getPassengerLastname(),
                dto.getBusTrip()
        );
        Ticket ticket = ticketDataMapper.fromBasicTicketRequestDto(dto);
        Timestamp issueDatetime = Timestamp.from(
                ZonedDateTime.now().toInstant()
        );
        ticket.setIssueDateTime(issueDatetime);

        if (isUser()) {
            String email = dto.getEmail();
            if (!email.equals(getUserEmail())) {
                log.error(WRONG_EMAIL_LOG_MESSAGE, email);
                throw new IllegalStateException(
                        String.format(WRONG_EMAIL_MESSAGE, email)
                );
            }
            GetUserResponseDto userDto = userService.getUser(email);
            ticketDataMapper.fromGetUserResponseDto(userDto, ticket);
        }

        GetBusTripResponseDto busTripDto = busTripService.getBusTrip(dto.getBusTrip());

        ticket.setStatus(TicketStatus.WAITING_TO_PAY);

        BigDecimal price = busTripDto.getFare().getPrice().multiply(busTripDto.getDistance());
        ticket.setPrice(price);
        ticket.setQrCode(String.valueOf(ticket.hashCode()));

        ticketMap.put(
                ticket.getQrCode(),
                new TicketCacheDto(ticket, busTripDto)
        );
        return paymentService.createPayment("test", ticket.getQrCode());
    }

    @Override
    public void updateTicketStatusAsPayed(String qrCode) {
        log.info("Update ticket with qrCode {} as payed", qrCode);

        try {
            TicketCacheDto ticketCacheDto = ticketMap.get(qrCode);
            Ticket ticket = ticketCacheDto.getTicket();

            ticket.setStatus(TicketStatus.PAYED);
            ticketRepo.save(ticket);

            Seat seat = seatService.getSeatByNameAndBusTripId(
                    ticket.getSeatName(), ticket.getBusTrip().getId()
            );
            seat.setSeatIsOccupied(true);
            regCash(ticketCacheDto);
            removeOldTicketsFromMap();
        } catch (NullPointerException | ClassCastException ex) {
            log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
            log.error("Update ticket as payed error", ex);
            throw new IllegalStateException(
                    String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
            );
        }
    }

    public void updateTicketStatusAsWaitingToReturn(Long id, String email) {
        log.info("Update ticket with id {} as waiting to return", id);

        Ticket ticket;
        if (isManager()) {
            ticket = ticketRepo.findById(id)
                    .orElseThrow(() -> {
                        log.error(TICKET_NOT_FOUND_LOG_MESSAGE, id);
                        throw new IllegalStateException(
                                String.format(TICKET_NOT_FOUND_MESSAGE, id)
                        );
                    });
        } else {
            ticket = ticketRepo.findByIdAndEmail(id, email)
                    .orElseThrow(() -> {
                        log.error(TICKET_NOT_FOUND_LOG_MESSAGE, id);
                        throw new IllegalStateException(
                                String.format(TICKET_NOT_FOUND_MESSAGE, id)
                        );
                    });
        }
        ticket.setStatus(TicketStatus.WAITING_TO_RETURN);
    }

    public void updateTicketStatusAsReturned(Long id) {
        log.info("Update ticket with id {} as returned", id);

        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> {
                    log.error(TICKET_NOT_FOUND_LOG_MESSAGE, id);
                    throw new IllegalStateException(
                            String.format(TICKET_NOT_FOUND_MESSAGE, id)
                    );
                });
        ticket.setStatus(TicketStatus.RETURNED);

        Seat seat = seatService.getSeatByNameAndBusTripId(
                ticket.getSeatName(), ticket.getBusTrip().getId()
        );
        boolean isSeatIsCoveredByAnother = seatService.isSeatIsCoveredByAnother(seat);
        if (!isSeatIsCoveredByAnother && seat.getSeatIsOccupied()) {
            seat.setSeatIsOccupied(false);
        }
    }

    @Override
    public GetPayedTicketResponseDto getPayedTicket(String qrCode) {
        log.info("Get payed ticket with qrCode {}", qrCode);
        try {
            TicketCacheDto ticketCacheDto = ticketMap.get(qrCode);
            return ticketDataMapper.toGetPayedTicketResponseDto(ticketCacheDto);
        } catch (NullPointerException | ClassCastException ex) {
            log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
            throw new IllegalStateException(
                    String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
            );
        }
    }

    @Override
    public BufferedImage generateQrCodeImage(Long ticketId, String qrCode) throws Exception {
        try {
            ticketMap.get(qrCode);
        } catch (NullPointerException | ClassCastException ex) {
            ticketRepo.findByIdAndQrCode(ticketId, qrCode)
                    .orElseThrow(() -> {
                        log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
                        throw new IllegalStateException(
                                String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
                        );
                    });
        }
        return QrCodeUtil.generateQrCodeImage(qrCode);
    }

    @Override
    public GetTicketResponseDto getTicket(Long ticketId, String email) {
        log.info("Fetching ticket {} as {}", ticketId, email);
        Optional<Ticket> optionalTicket;
        if (isManager()) {
            optionalTicket = ticketRepo.findById(ticketId);
        } else {
            optionalTicket = ticketRepo.findByIdAndEmail(ticketId, email);
        }
        return optionalTicket
                .map(ticketDataMapper::toGetTicketResponseDto)
                .orElseThrow(() -> {
                    log.error(TICKET_NOT_FOUND_LOG_MESSAGE, ticketId);
                    throw new IllegalStateException(
                            String.format(TICKET_NOT_FOUND_MESSAGE, ticketId)
                    );
                });
    }

    @Override
    public Page<GetTicketResponseDto> getAllTickets(String email, Pageable pageable) {
        log.info("Fetching all tickets as {}", email);
        Page<Ticket> ticketPage;
        if (isManager()) {
            ticketPage = ticketRepo.findAll(pageable);
        } else {
            ticketPage = ticketRepo.findAllByEmail(email, pageable);
        }
        List<GetTicketResponseDto> list = ticketPage.stream()
                .map(ticketDataMapper::toGetTicketResponseDto)
                .toList();
        return new PageImpl<>(list, pageable, ticketPage.getTotalElements());
    }

    @Override
    public void updateTicket(Long ticketId, BasicTicketRequestDto dto) {
        log.info("Updating ticket {} with {}", ticketId, dto);
        Optional<Ticket> optionalExist;
        if (isManager()) {
            optionalExist = ticketRepo.findById(ticketId);
        } else {
            optionalExist = ticketRepo.findByIdAndEmail(ticketId, dto.getEmail());
        }
        Ticket exist = optionalExist.orElseThrow(() -> {
            log.error(TICKET_NOT_FOUND_LOG_MESSAGE, ticketId);
            throw new IllegalStateException(
                    String.format(TICKET_NOT_FOUND_MESSAGE, ticketId)
            );
        });

        Timestamp issueDatetime = Timestamp.from(
                ZonedDateTime.now().toInstant()
        );
        exist.setIssueDateTime(issueDatetime);

        boolean shouldUpdateBusTrip = !exist.getBusTrip().getId().equals(dto.getBusTrip());
        ticketDataMapper.fromBasicTicketRequestDto(dto, exist);

        if (shouldUpdateBusTrip) {
            GetBusTripResponseDto busTripDto = busTripService.getBusTrip(dto.getBusTrip());

            BigDecimal price = busTripDto.getFare().getPrice().multiply(busTripDto.getDistance());
            exist.setPrice(price);
        }
        exist.setQrCode(String.valueOf(exist.hashCode()));
    }

    @Override
    public void removeTicket(Long id) {
        log.info("Removing ticket {}", id);
        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> {
                    log.error(TICKET_NOT_FOUND_LOG_MESSAGE, id);
                    throw new IllegalStateException(
                            String.format(TICKET_NOT_FOUND_MESSAGE, id)
                    );
                });
        ticketRepo.deleteById(id);
        Seat seat = seatService.getSeatByNameAndBusTripId(
                ticket.getSeatName(), ticket.getBusTrip().getId()
        );
        if (!seatService.isSeatIsCoveredByAnother(seat) && seat.getSeatIsOccupied()) {
            seat.setSeatIsOccupied(false);
        }
    }

    @Override
    public void regCash(TicketCacheDto ticketCacheDto) {
        cashRegisterService.regCash(ticketCacheDto);
    }

    private boolean isManager() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_MANAGER.name()));
    }

    private boolean isUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_USER.name()));
    }

    private String getUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private void removeOldTicketsFromMap() {
        Timestamp waitingDatetime = TimeUtil.createTimestampGreater(1);
        Timestamp payedDatetime = TimeUtil.createTimestampGreater(2);
        List<String> list = ticketMap.values().stream()
                .filter(ticketCacheDto -> {
                    try {
                        Ticket ticket = ticketCacheDto.getTicket();
                        if (ticket.getIssueDateTime().after(payedDatetime)) {
                            return true;
                        }
                        if (
                                ticket.getStatus().equals(TicketStatus.WAITING_TO_PAY) &&
                                        ticket.getIssueDateTime().after(waitingDatetime)
                        ) {
                            return true;
                        }
                    } catch (NullPointerException | ClassCastException ex) {
                        return false;
                    }
                    return false;
                })
                .map(t -> t.getTicket().getQrCode())
                .toList();
        list.forEach(ticketMap::remove);
    }
}

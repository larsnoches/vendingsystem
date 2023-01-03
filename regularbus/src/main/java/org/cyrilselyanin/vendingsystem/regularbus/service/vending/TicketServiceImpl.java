package org.cyrilselyanin.vendingsystem.regularbus.service.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.domain.auth.UserRole;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Seat;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Ticket;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.TicketStatus;
import org.cyrilselyanin.vendingsystem.regularbus.dto.auth.GetUserResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetPayedTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
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
    private final Map<String, Optional<Ticket>> ticketMap;

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
        ticketDataMapper.fromGetBusTripResponseDto(busTripDto, ticket);
        ticket.setId(null);
        ticket.setBusTrip(
                BusTrip.builder()
                        .id(dto.getBusTrip())
                        .build()
        );

        ticket.setDepartureDateTime(TimeUtil.createTimestamp(
                String.format("%s %s", busTripDto.getDepartureDate(), busTripDto.getDepartureTime())
        ));
        ticket.setArrivalDateTime(TimeUtil.createTimestamp(
                String.format("%s %s", busTripDto.getArrivalDate(), busTripDto.getArrivalTime())
        ));
        ticket.setQrCode(String.valueOf(ticket.hashCode()));
        ticket.setStatus(TicketStatus.WAITING_TO_PAY);

        BigDecimal price = busTripDto.getFare().getPrice().multiply(busTripDto.getDistance());
        ticket.setPrice(price);

//        ticketRepo.save(ticket);
        ticketMap.put(ticket.getQrCode(), Optional.ofNullable(ticket));
        return paymentService.createPayment("test", ticket.getQrCode());
    }

    @Override
    public void updateTicketStatusAsPayed(String qrCode) {
        log.info("Update ticket with qrCode {} as payed", qrCode);

//        Ticket ticket = ticketRepo.findByQrCode(qrCode)
        Ticket ticket = ticketMap.get(qrCode)
                .orElseThrow(() -> {
                    log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
                    throw new IllegalStateException(
                            String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
                    );
                });
        ticket.setStatus(TicketStatus.PAYED);
        ticketRepo.save(ticket);
//        ticketMap.remove(qrCode);

        Seat seat = seatService.getSeatByNameAndBusTripId(
                ticket.getSeatName(), ticket.getBusTrip().getId()
        );
        seat.setSeatIsOccupied(true);

        regCash(ticket);

//        return ticketDataMapper.toGetTicketResponseDto(ticket);
    }

    @Override
    public GetPayedTicketResponseDto getPayedTicket(String qrCode) {
        log.info("Get payed ticket with qrCode {}", qrCode);

        Ticket ticket = ticketMap.get(qrCode)
                .orElseThrow(() -> {
                    log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
                    throw new IllegalStateException(
                            String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
                    );
                });
//        ticketMap.remove(qrCode);
        return ticketDataMapper.toGetPayedTicketResponseDto(ticket);
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
            ticketDataMapper.fromGetBusTripResponseDto(busTripDto, exist);

            exist.setDepartureDateTime(TimeUtil.createTimestamp(
                    String.format("%s %s", busTripDto.getDepartureDate(), busTripDto.getDepartureTime())
            ));
            exist.setArrivalDateTime(TimeUtil.createTimestamp(
                    String.format("%s %s", busTripDto.getArrivalDate(), busTripDto.getArrivalTime())
            ));
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
    public void regCash(Ticket ticket) {
        cashRegisterService.regCash(ticket);
    }

    public BufferedImage generateQrCode(Long ticketId, String qrCode) throws Exception {
        if (!isUser() && !isManager()) {
            ticketMap.get(qrCode)
                    .orElseThrow(() -> {
                        log.error(TICKET_NOT_FOUND_LOG_MESSAGE, qrCode);
                        throw new IllegalStateException(
                                String.format(TICKET_QRCODE_NOT_FOUND_MESSAGE, qrCode)
                        );
                    });
        } else {
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

//    public TicketServiceImpl(
//            TicketRepository ticketRepository,
////            BusTripRepository busTripRepository,
//            CashRegisterService cashRegisterService
//    ) {
//        this.ticketRepository = ticketRepository;
////        this.busTripRepository = busTripRepository;
//        this.cashRegisterService = cashRegisterService;
//    }
//
//    @Override
//    public Ticket create(Ticket ticket) {
//        return ticketRepository.save(ticket);
//    }
//
//    @Override
//    public Optional<Ticket> getTicket(Long ticketId) {
//        if (isAdmin()) return ticketRepository.findById(ticketId);
//        if (isUser()) {
//            String userId = getUserId();
//            return ticketRepository.findByIdAndUserId(ticketId, userId);
//        }
//
//        return Optional.empty();
//    }
//
//    @Override
//    public List<Ticket> getAllTickets() {
//        if (isAdmin()) return ticketRepository.findAll();
//        if (isUser()) {
//            String userId = getUserId();
//            return ticketRepository.findAllByUserId(userId);
//        }
//
//        return List.of();
//    }
//
//    @Override
//    public Ticket update(Ticket ticket, Long id) {
//        return ticketRepository.findById(id)
//                .map(t -> {
//                   t.setIssueDateTime(ticket.getIssueDateTime());
//                   t.setPassengerLastname(ticket.getPassengerLastname());
//                   t.setPassengerFirstname(ticket.getPassengerFirstname());
//                   t.setPassengerMiddlename(ticket.getPassengerMiddlename());
//                   t.setBusRouteNumber(ticket.getBusRouteNumber());
//                   t.setQrCode(ticket.getQrCode());
//                   t.setSeatName(ticket.getSeatName());
//                   t.setCarrierName(ticket.getCarrierName());
//                   t.setDepartureBuspointName(ticket.getDepartureBuspointName());
//                   t.setArrivalBuspointName(ticket.getArrivalBuspointName());
//                   t.setDepartureDateTime(ticket.getDepartureDateTime());
//                   t.setArrivalDatetime(ticket.getArrivalDatetime());
//                   t.setPrice(ticket.getPrice());
//                   t.setBusTrip(ticket.getBusTrip());
//                   t.setUserId(ticket.getUserId());
//                   return ticketRepository.save(t);
//                })
//                .orElseGet(() -> {
//                    ticket.setId(id);
//                    return ticketRepository.save(ticket);
//                });
//    }
//
//    @Override
//    public void delete(Long id) {
//        ticketRepository.deleteById(id);
//    }
//
//    @Override
//    public Optional<BusTrip> getTicketBusTrip(Long ticketId) {
//        if (isAdmin()) {
//            return ticketRepository.findById(ticketId)
//                    .map(Ticket::getBusTrip);
//        }
//        if (isUser()) {
//            String userId = getUserId();
//            return ticketRepository.findByIdAndUserId(ticketId, userId)
//                    .map(Ticket::getBusTrip);
//        }
//
//        return Optional.empty();
//    }
//
//    private boolean isUser() {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
//    }
//
//    private String getUserId() {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getName();
//    }
//
//    private boolean isAdmin() {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//    }
//
//    /**
//     * Reg cash via cashRegister service
//     * @param ticket Ticket instance
//     */
//    @Override
//    public void regCash(Ticket ticket) {
//        cashRegisterService.regCash(ticket);
//    }
}

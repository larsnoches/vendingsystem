package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class TicketController {

    private static final String WRONG_TICKET_ID_ERR_MESSAGE = "Недопустимый id билета.";
    private static final String WRONG_EMAIL_ERR_MESSAGE = "Адрес электронной почты обязателен.";

    private final TicketService ticketService;

    @PostMapping("/tickets/create")
    public ResponseEntity<?> createTicket(
            @RequestBody @Valid BasicTicketRequestDto dto
    ) {
        try {
            URI uri = URI.create(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/v1/tickets/create")
                            .toUriString()
            );
            /*
            String paymentUrl = ticketService.createTicket(dto);
            return String.format("redirect:%s", paymentUrl);
            */
            ticketService.createTicket(dto);
            return ResponseEntity
                    .created(uri)
                    .build();
        } catch (RuntimeException | JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/tickets/updatePaymentStatus")
    public GetTicketResponseDto updateTicketStatusAsPayed(
            @NotBlank(message = "Неверный запрос билета")
            @RequestParam("qrcode")
            String qrCode
    ) {
        try {
            return ticketService.updateTicketStatusAsPayed(qrCode);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/tickets/{ticketId}")
    public GetTicketResponseDto getTicket(
            @NotNull
            @Min(value = 0L, message = WRONG_TICKET_ID_ERR_MESSAGE)
            @PathVariable
            Long ticketId,
            @NotBlank(message = WRONG_EMAIL_ERR_MESSAGE)
            @RequestParam
            String email
    ) {
        try {
            return ticketService.getTicket(ticketId, email);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/tickets")
    public Page<GetTicketResponseDto> getAllTickets(
            @NotBlank(message = WRONG_EMAIL_ERR_MESSAGE)
            @RequestParam
            String email,
            Pageable pageable
    ) {
        try {
            return ticketService.getAllTickets(email, pageable);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("/tickets/{ticketId}")
    public void updateTicket(
            @NotNull
            @Min(value = 0L, message = WRONG_TICKET_ID_ERR_MESSAGE)
            @PathVariable
            Long ticketId,
            @RequestBody
            @Valid
            BasicTicketRequestDto dto) {
        try {
            ticketService.updateTicket(ticketId, dto);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/tickets/{id}")
    public void removeTicket(
            @NotNull
            @Min(value = 0L, message = WRONG_TICKET_ID_ERR_MESSAGE)
            @PathVariable
            Long id
    ) {
        try {
            ticketService.removeTicket(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

}

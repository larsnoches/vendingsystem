package org.cyrilselyanin.vendingsystem.regularbus.controller.vending;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.regularbus.dto.payment.UpdateTicketStatusAsPayedResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.BasicTicketRequestDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetPayedTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.GetTicketResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.service.vending.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class TicketController {

    private static final String WRONG_TICKET_ID_ERR_MESSAGE = "Недопустимый id билета.";
    private static final String WRONG_EMAIL_ERR_MESSAGE = "Адрес электронной почты обязателен.";
    private static final String WRONG_QRCODE_ERR_MESSAGE = "Недопустимый QR-код.";

    private final TicketService ticketService;

    @PostMapping("/tickets/create")
    public void createTicket(
            @RequestBody @Valid BasicTicketRequestDto dto,
            HttpServletResponse httpServletResponse
    ) {
        try {
            String paymentUrl = ticketService.createTicket(dto);
            httpServletResponse.sendRedirect(paymentUrl);
//            return paymentUrl;
        } catch (RuntimeException | JsonProcessingException ex) {
            log.error("There is a create ticket error.", ex.getCause());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error("There is a create ticket redirect error.", ex.getCause());
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/tickets/updatePaymentStatus")
    public UpdateTicketStatusAsPayedResponseDto updateTicketStatusAsPayed(
            @NotBlank(message = "Неверный запрос билета")
            @RequestParam("qrcode")
            String qrCode,
            HttpServletResponse httpServletResponse
    ) {
        try {
            ticketService.updateTicketStatusAsPayed(qrCode);
            String urlString = String.format(
                    "http://127.0.0.1:4200/tickets/viewPayed?qrcode=%s",
                    qrCode
            );
            UpdateTicketStatusAsPayedResponseDto responseDto = UpdateTicketStatusAsPayedResponseDto.builder()
                    .path(urlString)
                    .build();
//            httpServletResponse.sendRedirect(urlString);
            return responseDto;
        } catch (RuntimeException ex) {
            log.error("There is an update ticket as payed error.", ex.getCause());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } /*catch (IOException ex) {
            log.error("There is an update ticket as payed redirect error.", ex.getCause());
            throw new RuntimeException(ex);
        }*/
    }

    @GetMapping("/tickets/payedTicket")
    public GetPayedTicketResponseDto getPayedTicket(
            @NotBlank(message = "Неверный запрос билета")
            @RequestParam("qrcode")
            String qrCode
    ) {
        try {
            return ticketService.getPayedTicket(qrCode);
        } catch (RuntimeException ex) {
            log.error("There is a get payed ticket error.", ex.getCause());
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
            log.error("There is a get ticket error.", ex.getCause());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping(value = "/tickets/{ticketId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQrCode(
            @NotNull
            @Min(value = 0L, message = WRONG_TICKET_ID_ERR_MESSAGE)
            @PathVariable
            Long ticketId,
            @NotBlank(message = WRONG_QRCODE_ERR_MESSAGE)
            @RequestParam
            String value
    ) {
        try {
            return ResponseEntity.ok(ticketService.generateQrCode(ticketId, value));
        } catch (Exception ex) {
            log.error("There is a generate qrcode image error.", ex.getCause());
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
            log.error("There is a get all tickets error.", ex.getCause());
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
            log.error("There is an update ticket error.", ex.getCause());
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
            log.error("There is a remove ticket error.", ex.getCause());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

}

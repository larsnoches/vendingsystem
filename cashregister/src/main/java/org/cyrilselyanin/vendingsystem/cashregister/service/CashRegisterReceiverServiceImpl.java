package org.cyrilselyanin.vendingsystem.cashregister.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cyrilselyanin.vendingsystem.cashregister.config.ConfigProps;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.exception.RegCashException;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketDto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Cash register receiver, annotated as rabbit listener.
 */
@Service
@RabbitListener(queues = "#{autoDeletingQueue.name}")
@RequiredArgsConstructor
@Slf4j
public class CashRegisterReceiverServiceImpl implements CashRegisterReceiverService {
    private final SbisService sbisService;
    private final ConfigProps configProps;

    /**
     * Receive method
     * @param in Some ticketDto object
     */
    @RabbitHandler(isDefault = true)
    public void receive(TicketDto in) {
        log.debug("Incoming ticket dto");
        log.debug("{}", in);
        log.debug("...printed ticket dto");

        // reg cash with old token props
        Optional<String> token = Optional.ofNullable(sbisService.getToken());
        Optional<String> sid = Optional.ofNullable(sbisService.getSid());
        if (token.isPresent() && sid.isPresent()) {
            try {
                sbisService.regCash(in);
                log.debug("token and sid are present, regcash is called");
            } catch (RegCashException ex) {
                log.error("With already set auth props regCash throws exception.", ex);
            }
            return;
        }

        // new dto for token request
        SbisTokenRequestDto requestDto = SbisTokenRequestDto.create(
                configProps.getSbisAuthAppClientId(),
                configProps.getSbisAuthAppSecret(),
                configProps.getSbisAuthSecretKey()
        );

        try {
            sbisService.requestToken(requestDto);
            token = Optional.ofNullable(sbisService.getToken());
            sid = Optional.ofNullable(sbisService.getSid());
            if (token.isPresent() && sid.isPresent()) {
                sbisService.regCash(in);
                log.debug("token and sid are present, regcash is called");
            }
        } catch (RegCashException ex) {
            log.error("With new auth props regCash throws exception.", ex);
        } catch (IOException | RuntimeException ex) {
            log.error("Trying to auth and got exception.", ex);
        }
    }
}

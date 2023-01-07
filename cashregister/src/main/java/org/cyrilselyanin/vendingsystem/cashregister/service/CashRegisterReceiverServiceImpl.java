package org.cyrilselyanin.vendingsystem.cashregister.service;

import org.cyrilselyanin.vendingsystem.cashregister.dto.TicketDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.exception.RegCashException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Cash register receiver, annotated as rabbit listener.
 */
@Service
@RabbitListener(queues = "#{autoDeletingQueue.name}")
public class CashRegisterReceiverServiceImpl implements CashRegisterReceiverService {
    private final SbisService sbisService;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange direct;
    private static final Logger logger = LoggerFactory.getLogger(CashRegisterReceiverServiceImpl.class);

    /**
    * Properties for token requesting
    */
    @Value("${cashregister.sbis-auth.appClientId}")
    private String appClientId;
    @Value("${cashregister.sbis-auth.appSecret}")
    private String appSecret;
    @Value("${cashregister.sbis-auth.secretKey}")
    private String secretKey;

    public CashRegisterReceiverServiceImpl(
            SbisService sbisService,
            RabbitTemplate rabbitTemplate,
            DirectExchange direct
    ) {
        this.sbisService = sbisService;
        this.rabbitTemplate = rabbitTemplate;
        this.direct = direct;
    }

    /**
     * Receive method
     * @param in Some ticketDto object
     */
    @RabbitHandler
    public void receive(TicketDto in) {
        logger.debug("Incoming ticket dto");
        logger.debug("{}", in);
        logger.debug("...printed ticket dto");

        // reg cash with old token props
        Optional<String> token = Optional.ofNullable(sbisService.getToken());
        Optional<String> sid = Optional.ofNullable(sbisService.getSid());
        if (token.isPresent() && sid.isPresent()) {
            try {
                sbisService.regCash(in);
                logger.debug("token and sid are present, regcash is called");
            } catch (RegCashException ex) {
                logger.error("With already set auth props regCash throws exception.", ex);
            }
            return;
        }

        // new dto for token request
        SbisTokenRequestDto requestDto = SbisTokenRequestDto.create(
                appClientId,
                appSecret,
                secretKey
        );

        try {
            sbisService.requestToken(requestDto);
            token = Optional.ofNullable(sbisService.getToken());
            sid = Optional.ofNullable(sbisService.getSid());
            if (token.isPresent() && sid.isPresent()) {
                sbisService.regCash(in);
                logger.debug("token and sid are present, regcash is called");
            }
        } catch (RegCashException ex) {
            logger.error("With new auth props regCash throws exception.", ex);
        } catch (IOException ex) {
            logger.error("Trying to auth and got exception.", ex);
        }
    }
}

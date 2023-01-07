package org.cyrilselyanin.vendingsystem.cashregister.service;

import lombok.Getter;
import org.cyrilselyanin.vendingsystem.cashregister.dto.*;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisRegCashRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.exception.RegCashException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Sbis service implementation
 */
@Service
public class SbisServiceImpl implements SbisService {
    private final String authUrl = "https://online.sbis.ru/oauth/service";
    private final String regCashUrl = "https://api.sbis.ru/retail/sale/create";

    private final SbisAuthService sbisAuthService;

    private final SbisRetailService sbisRetailService;

    @Getter
    private String token;

    @Getter
    private String sid;

    public SbisServiceImpl(
            SbisAuthService sbisAuthService,
            SbisRetailService sbisRetailService
    ) {
        this.sbisAuthService = sbisAuthService;
        this.sbisRetailService = sbisRetailService;
    }

    @Override
    public void requestToken(SbisTokenRequestDto requestDto) throws IOException {
        SbisTokenResponseDto responseDto = sbisAuthService.getToken(authUrl, requestDto);
        TokenResponseDtoAdapter tokenResponseDtoAdapter = new TokenResponseDtoAdapter(responseDto);
        tokenResponseDtoAdapter.adapt();
    }

    @Override
    public void regCash(TicketDto ticketDto) {
        RegCashRequestDtoAdapter regCashRequestDtoAdapter = new RegCashRequestDtoAdapter(ticketDto);
        SbisRegCashRequestDto sbisRegCashRequestDto = regCashRequestDtoAdapter.adapt();
        try {
            SbisRegCashResponseDto sbisRegCashResponseDto = sbisRetailService.regCash(
                    regCashUrl, token, sid, sbisRegCashRequestDto
            );
        } catch (IOException | NullPointerException ex) {
            throw new RegCashException(ex.getMessage());
        }

    }

//    @Override
//    public String getToken() {
//        return token;
//    }

    /**
     * Adapter for token response dto
     */
    public class TokenResponseDtoAdapter {
        private final SbisTokenResponseDto adaptee;
        public TokenResponseDtoAdapter(SbisTokenResponseDto adaptee) {
            this.adaptee = adaptee;
        }
        public void adapt() {
            token = adaptee.getToken();
            sid = adaptee.getSid();
        }
    }

    /**
     * Adapter for cash reg request dto
     */
    public static class RegCashRequestDtoAdapter {
        private final TicketDto adaptee;
        public RegCashRequestDtoAdapter(TicketDto adaptee) {
            this.adaptee = adaptee;
        }
        public SbisRegCashRequestDto adapt() {
            SbisRegCashRequestDto dto = new SbisRegCashRequestDto();
            dto.setCompanyID(1L);
            dto.setCashierFIO("Wow wow wow");
            dto.setOperationType(1);
            dto.setCashSum(null);
            dto.setBankSum(adaptee.getPrice());
            dto.setInternetSum(adaptee.getPrice());
            dto.setAccountSum(adaptee.getPrice());
            dto.setPostpaySum(null);
            dto.setPrepaySum(adaptee.getPrice());

            var vat20 = adaptee.getPrice().divide(BigDecimal.valueOf(100));
            dto.setVatNone(adaptee.getPrice().subtract(vat20));
            dto.setVatSum0(null);
            dto.setVatSum10(null);
            dto.setVatSum20(vat20);

            dto.setNameNomenclature(String.format(
                    "Маршрут %s, %s - %s, %s",
                    adaptee.getBusRouteNumber(),
                    adaptee.getDepartureBuspointName(),
                    adaptee.getDepartureBuspointName(),
                    adaptee.getDepartureDateTime().toString()
            ));

            //

            dto.setBarcodeNomenclature(BigDecimal.valueOf(12345));
            dto.setPriceNomenclature(adaptee.getPrice());
            dto.setQuantityNomenclature(1);
            dto.setMeasureNomenclature("шт");
            dto.setKindNomenclature("у");
            dto.setTotalPriceNomenclature(adaptee.getPrice());
            dto.setTaxRateNomenclature(vat20);
            dto.setTotalVat(vat20);
            dto.setCustomerFIO(
                    String.format(
                            "%s %s %s",
                            adaptee.getPassengerLastname(),
                            adaptee.getPassengerFirstname(),
                            adaptee.getPassengerMiddlename()
                    )
            );
            dto.setCustomerEmail(null);
            dto.setCustomerPhone(null);
            dto.setCustomerExtId(null);
            dto.setTaxSystem(4);
            dto.setSendEmail(null);
            dto.setSendPhone(null);
            dto.setPropName(null);
            dto.setPropVa(null);
            dto.setComment("");
            dto.setPayMethod(4);
            dto.setExternalId("123-123-123");

            return dto;

        }
    }
}

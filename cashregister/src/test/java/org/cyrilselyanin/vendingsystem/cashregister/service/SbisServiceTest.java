package org.cyrilselyanin.vendingsystem.cashregister.service;

import okhttp3.*;
import org.cyrilselyanin.vendingsystem.regularbus.dto.ticket.TicketDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenRequestDto;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;

@SpringBootTest
class SbisServiceTest {
    @Autowired
    SbisServiceImpl sbisService;

    @MockBean
    OkHttpClient okHttpClient;

    String token;
    String sid;

    @Test
    public void getToken_thenCorrect() throws IOException {
        // stabs
        SbisTokenRequestDto requestDto = SbisTokenRequestDto.create(
                "appClientId_123",
                "appSecret_123",
                "secretKey_123"
        );

        final Response response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("").body(
                        ResponseBody.create(
                            "{" +
                                "\"token\": \"val\"," +
                                "\"sid\": \"val\"" +
                            "}",
                            MediaType.parse("application/json")
                        ))
                .build();
        // mock
        when(okHttpClient.newCall(any())).thenReturn(mock(Call.class));
        when(okHttpClient.newCall(any()).execute()).thenReturn(response);

        try {
            sbisService.requestToken(requestDto);
            token = sbisService.getToken();
            sid = sbisService.getSid();

            assertNotNull(token);
            assertNotNull(sid);
            assertThat(token, instanceOf(String.class));
            assertThat(sid, instanceOf(String.class));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void regCash_thenCorrect() throws IOException {
        // stabs
        TicketDto ticketDto = new TicketDto(
                "Petrov",
                "Ivan",
                "Ivanovich",
                "105",
                "Center",
                Timestamp.valueOf("2022-07-07 10:09:21.61"),
                BigDecimal.valueOf(500)
        );

        final Response response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("").body(
                        ResponseBody.create(
                            "{" +
                                "\"Result\":" +
                                    "{" +
                                        "\"payId\": \"val\"" +
                                    "}" +
                            "}",
                            MediaType.parse("application/json")
                        ))
                .build();

        // mock, okHttpClient resets after first test ran
        when(okHttpClient.newCall(any())).thenReturn(mock(Call.class));
        when(okHttpClient.newCall(any()).execute()).thenReturn(response);

        assertDoesNotThrow(() -> {
            sbisService.regCash(ticketDto);
        });
    }

}
package org.cyrilselyanin.vendingsystem.cashregister.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisRegCashRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisRegCashResponseDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Retail service of the Sbis-adapter
 * author Cyril Selyanin
 */
@Service
@RequiredArgsConstructor
public class SbisRetailServiceImpl implements SbisRetailService {
    private final OkHttpClient okHttpClient;
    private final MediaType JSON_MEDIA = MediaType.get(
            "application/json; charset=utf-8"
    );

    /**
     * Reistering cash
     * @param regCashUrl Sbis retail string url
     * @param token Sbis service token
     * @param sid Sbis session id
     * @param sbisRegCashRequestDto Request dto
     * @return Response dto
     * @throws NullPointerException
     * @throws IOException
     */
    @Override
    public SbisRegCashResponseDto regCash(
            String regCashUrl,
            String token,
            String sid,
            SbisRegCashRequestDto sbisRegCashRequestDto
    ) throws NullPointerException, IOException {
        String json = new ObjectMapper().writeValueAsString(sbisRegCashRequestDto);
        RequestBody requestBody = RequestBody.create(json, JSON_MEDIA);
        Request request = new Request.Builder()
                .url(regCashUrl)
                .post(requestBody)
                .addHeader("X-SBISAccessToken", token)
                .addHeader("X-SBISSessionId", sid)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String respJson = response.body().string();
            return new ObjectMapper()
                    .readerFor(SbisRegCashResponseDto.class)
                    .readValue(respJson);
        }
    }
}
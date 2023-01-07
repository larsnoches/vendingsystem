package org.cyrilselyanin.vendingsystem.cashregister.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenResponseDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Auth service of the Sbis-adapter
 * author Cyril Selyanin
 */
@Service
public class SbisAuthServiceImpl implements SbisAuthService {
    private final OkHttpClient okHttpClient;
    private final MediaType JSON_MEDIA = MediaType.get("application/json; charset=utf-8");

    public SbisAuthServiceImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * Get token from the Sbis service
     * @param tokenUrl Sbis auth service url string
     * @param requestDto Request dto
     * @return Token response
     * @throws IOException
     */
    public SbisTokenResponseDto getToken(
            String tokenUrl,
            SbisTokenRequestDto requestDto
    ) throws IOException {
        String json = new ObjectMapper().writeValueAsString(requestDto);
        RequestBody requestBody = RequestBody.create(json, JSON_MEDIA);
        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String respJson = response.body().string();
            SbisTokenResponseDto sbisTokenResponseDto = new ObjectMapper()
                    .readerFor(SbisTokenResponseDto.class)
                    .readValue(respJson);
            return sbisTokenResponseDto;
        }
    }

}

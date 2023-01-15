package org.cyrilselyanin.vendingsystem.cashregister.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisTokenResponseDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.error.SbisErrorDto;
import org.cyrilselyanin.vendingsystem.cashregister.exception.RegCashException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Auth service of the Sbis-adapter
 * author Cyril Selyanin
 */
@Service
@RequiredArgsConstructor
public class SbisAuthServiceImpl implements SbisAuthService {
    private final OkHttpClient okHttpClient;
    private final MediaType JSON_MEDIA = MediaType.get("application/json; charset=utf-8");

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
        String respJson = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            respJson = response.body().string();
            return new ObjectMapper()
                    .readerFor(SbisTokenResponseDto.class)
                    .readValue(respJson);
        } catch (JsonProcessingException ex) {
            if (respJson != null) {
                SbisErrorDto errorDto = new ObjectMapper()
                        .readerFor(SbisErrorDto.class)
                        .readValue(respJson);
                throw new RegCashException(errorDto.getError().getDetails());
            }
        }
        return null;
    }

}

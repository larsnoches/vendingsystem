package org.cyrilselyanin.vendingsystem.cashregister.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class SbisTokenRequestDto {
    private String appClientId;
    private String appSecret;
    private String secretKey;

    public static SbisTokenRequestDto create(
            String appClientId,
            String appSecret,
            String secretKey
    ) {
        SbisTokenRequestDto dto = new SbisTokenRequestDto();
        dto.setAppClientId(appClientId);
        dto.setAppSecret(appSecret);
        dto.setSecretKey(secretKey);
        return dto;
    }

    @JsonGetter("app_client_id")
    public String getAppClientId() {
        return this.appClientId;
    }

    @JsonGetter("app_secret")
    public String getAppSecret() {
        return this.appSecret;
    }

    @JsonGetter("secret_key")
    public String getSecretKey() {
        return this.secretKey;
    }
}

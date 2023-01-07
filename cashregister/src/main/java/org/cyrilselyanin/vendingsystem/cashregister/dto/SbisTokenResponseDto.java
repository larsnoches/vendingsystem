package org.cyrilselyanin.vendingsystem.cashregister.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SbisTokenResponseDto {
    private String token;
    private String sid;
}

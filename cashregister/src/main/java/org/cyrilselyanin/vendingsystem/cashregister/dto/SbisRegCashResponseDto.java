package org.cyrilselyanin.vendingsystem.cashregister.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class SbisRegCashResponseDto {
    private RegCashResponseResultDto result;

    @JsonGetter("Result")
    public RegCashResponseResultDto getResult() {
        return this.result;
    }

    @JsonSetter("Result")
    public void setResult(RegCashResponseResultDto result) {
        this.result = result;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class RegCashResponseResultDto {
        public String payId;
    }
}

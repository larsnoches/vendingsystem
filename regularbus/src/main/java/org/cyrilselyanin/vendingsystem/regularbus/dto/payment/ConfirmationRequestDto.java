package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ConfirmationRequestDto {

	private String type = "redirect";

	private String redirectUrl = "http://127.0.0.1/api/v1/tickets/updatePaymentStatus?qrcode=%s";

	@JsonIgnore
	private String qrCode;

	@JsonProperty("return_url")
	public String getRedirectUrl() {
		return String.format(redirectUrl, qrCode);
	}

}

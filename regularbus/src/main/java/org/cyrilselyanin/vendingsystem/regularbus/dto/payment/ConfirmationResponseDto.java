package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ConfirmationResponseDto {

	private String type = "redirect";
	@JsonProperty("confirmation_url")
	private String confirmationUrl;

}

package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RecipientResponseDto {

	@JsonProperty("account_id")
	private String accountId;
	@JsonProperty("gateway_id")
	private String gatewayId;

}

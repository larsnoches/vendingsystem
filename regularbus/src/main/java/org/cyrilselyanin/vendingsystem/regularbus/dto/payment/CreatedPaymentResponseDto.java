package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreatedPaymentResponseDto {

	private String id;
	private String status;
	private Boolean paid;
	private AmountDto amount;
	private ConfirmationResponseDto confirmation;
	@JsonProperty("created_at")
	private String createdAt;
	private String description;
	private Object metadata;
	private RecipientResponseDto recipient;
	private Boolean refundable;
	private Boolean test;

}

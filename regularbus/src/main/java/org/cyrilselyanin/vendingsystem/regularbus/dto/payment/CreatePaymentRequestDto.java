package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreatePaymentRequestDto {

	private AmountDto amount;
	private Boolean capture = true;
	private ConfirmationRequestDto confirmation;
	private String description;

}

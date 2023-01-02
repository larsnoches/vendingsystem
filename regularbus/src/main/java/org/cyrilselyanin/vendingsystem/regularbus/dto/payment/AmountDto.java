package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AmountDto {

	private String value;
	private String currency = "RUB";

}

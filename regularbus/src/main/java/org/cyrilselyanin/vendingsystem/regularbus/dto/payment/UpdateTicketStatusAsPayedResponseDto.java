package org.cyrilselyanin.vendingsystem.regularbus.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTicketStatusAsPayedResponseDto {

	private String path;

}

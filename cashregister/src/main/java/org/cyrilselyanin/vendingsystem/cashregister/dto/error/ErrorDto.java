package org.cyrilselyanin.vendingsystem.cashregister.dto.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {

	private Integer code;
	private String message;
	private String details;
	private String type;
	private DataDto data;

}

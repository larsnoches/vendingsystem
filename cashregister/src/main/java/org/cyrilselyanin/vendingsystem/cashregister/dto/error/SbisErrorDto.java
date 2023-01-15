package org.cyrilselyanin.vendingsystem.cashregister.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SbisErrorDto {

	private Integer id;

	@JsonProperty("jsonrpc")
	private String jsonRpc;

	private ErrorDto error;

}

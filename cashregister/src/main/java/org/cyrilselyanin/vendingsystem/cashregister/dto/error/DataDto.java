package org.cyrilselyanin.vendingsystem.cashregister.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDto {

	private String classid;

	@JsonProperty("error_code")
	private Integer errorCode;
	private String addinfo;

}

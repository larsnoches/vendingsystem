package org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "name", "address", "busPointType"
})
public class GetBusPointResponseDto extends BasicBusPointRequestDto {

	private Long id;

}

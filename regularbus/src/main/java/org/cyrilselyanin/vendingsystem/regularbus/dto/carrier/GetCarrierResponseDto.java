package org.cyrilselyanin.vendingsystem.regularbus.dto.carrier;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "name", "inn", "address"
})
public class GetCarrierResponseDto extends BasicCarrierRequestDto {

	private Long id;

}

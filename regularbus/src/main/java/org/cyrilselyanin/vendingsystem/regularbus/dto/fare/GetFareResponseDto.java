package org.cyrilselyanin.vendingsystem.regularbus.dto.fare;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "name", "price", "carrier"
})
public class GetFareResponseDto extends BasicFareRequestDto {

	private Long id;

}

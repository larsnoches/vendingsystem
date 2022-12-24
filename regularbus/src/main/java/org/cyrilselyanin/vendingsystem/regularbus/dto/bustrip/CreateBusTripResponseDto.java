package org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
		"id", "departureBusPoint", "arrivalBusPoint", "distance",
		"busRouteNumber", "departureDate", "departureTime", "averageBusSpeed",
		"bus", "fare", "carrier"
})
public class CreateBusTripResponseDto extends BasicBusTripRequestDto {

	private Long id;

}

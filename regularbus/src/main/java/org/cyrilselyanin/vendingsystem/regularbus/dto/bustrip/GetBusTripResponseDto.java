package org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bus.GetBusResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.buspoint.GetBusPointResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.carrier.GetCarrierResponseDto;
import org.cyrilselyanin.vendingsystem.regularbus.dto.fare.GetFareResponseDto;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class GetBusTripResponseDto {

	private Long id;
	private GetBusPointResponseDto departureBusPoint;
	private GetBusPointResponseDto arrivalBusPoint;
	private BigDecimal distance;
	private String busRouteNumber;
	private String departureDate;
	private String departureTime;
	private String arrivalDate;
	private String arrivalTime;
//	private Integer averageBusSpeed;
	private GetBusResponseDto bus;
	private GetFareResponseDto fare;
	private GetCarrierResponseDto carrier;

}

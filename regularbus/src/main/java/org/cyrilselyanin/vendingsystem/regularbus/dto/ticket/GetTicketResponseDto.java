package org.cyrilselyanin.vendingsystem.regularbus.dto.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cyrilselyanin.vendingsystem.regularbus.dto.bustrip.GetBusTripResponseDto;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class GetTicketResponseDto {

	private Long id;
	private String issueDateTime;
	private String passengerLastname;
	private String passengerFirstname;
	private String passengerMiddlename;
	private String busRouteNumber;
	private String qrCode;
	private String seatName;
	private String carrierName;
	private String departureBusPointName;
	private String arrivalBusPointName;
	private String departureDateTime;
	private String arrivalDateTime;
	private BigDecimal price;
	private String status;
	private GetBusTripResponseDto busTrip;
	private String email;

}

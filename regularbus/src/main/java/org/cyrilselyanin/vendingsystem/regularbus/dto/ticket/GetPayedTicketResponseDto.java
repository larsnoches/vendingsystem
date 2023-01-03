package org.cyrilselyanin.vendingsystem.regularbus.dto.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class GetPayedTicketResponseDto {

	private Long id;
	private String issueDateTime;
	private String passengerLastname;
	private String passengerFirstname;
	private String passengerMiddlename;
	private String busRouteNumber;
	private String qrCode;
	private String seatName;
	private String carrierName;
	private String departureBuspointName;
	private String arrivalBuspointName;
	private String departureDateTime;
	private String arrivalDateTime;
	private BigDecimal price;
	private String status;
	private Long busTrip;
	private String email;

}

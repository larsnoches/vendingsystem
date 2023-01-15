package org.cyrilselyanin.vendingsystem.regularbus.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Dto of request to this service in communication between services.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class TicketDto implements Serializable {
	/**
	 * Passenger last name
	 */
	private String passengerLastname;

	/**
	 * Passenger first name
	 */
	private String passengerFirstname;

	/**
	 * Passenger middle name
	 */
	private String passengerMiddlename;

	/**
	 * Number of a bus route
	 */
	private String busRouteNumber;

	/**
	 * Name of a departure bus point
	 */
	private String departureBuspointName;

	/**
	 * Time stamp of a bus departure
	 */
	private Timestamp departureDateTime;

	/**
	 * Ticket price
	 */
//    @JsonProperty("ticketPrice")
	private BigDecimal price;
}

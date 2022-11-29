package org.cyrilselyanin.vendingsystem.regularbus.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.*;

/**
 * Dto of request to this service in communication between services.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class TicketDto implements Serializable {
    /**
     * Passenger last name
     */
//    @JsonProperty("passengerLastname")
    private String passengerLastname;

    /**
     * Passenger first name
     */
//    @JsonProperty("passengerFirstname")
    private String passengerFirstname;

    /**
     * Passenger middle name
     */
//    @JsonProperty("passengerMiddlename")
    private String passengerMiddlename;

    /**
     * Number of a bus route
     */
//    @JsonProperty("busRouteNumber")
    private String busRouteNumber;

    /**
     * Name of a departure bus point
     */
//    @JsonProperty("departureBuspointName")
    private String departureBuspointName;

    /**
     * Time stamp of a bus departure
     */
//    @JsonProperty("departureDateTime")
    private Timestamp departureDateTime;

    /**
     * Ticket price
     */
//    @JsonProperty("ticketPrice")
    private BigDecimal price;
}

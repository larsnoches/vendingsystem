package org.cyrilselyanin.vendingsystem.regularbus.domain;

import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusTrip;
import org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate.TicketValidateBeforeDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TicketValidateBeforeDate
public class Ticket {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tickets_gen")
    @SequenceGenerator(
            name = "tickets_gen",
            sequenceName = "tickets_seq",
            allocationSize = 1)
    @Column(name = "ticket_id", nullable = false)
    private Long id;

    @NotNull(message = "Issue date time isn't set")
    @FutureOrPresent
    @Column(name = "issue_datetime", nullable = false)
    private Timestamp issueDateTime;

    @NotBlank(message = "Passenger lastname cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Passenger lastname must be between 2 and 255 characters")
    @Column(name = "passenger_lastname", length = 255)
    private String passengerLastname;

    @NotBlank(message = "Passenger firstname cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Passenger firstname must be between 2 and 255 characters")
    @Column(name = "passenger_firstname", length = 255)
    private String passengerFirstname;

    @NotBlank(message = "Passenger middlename cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Passenger middlename must be between 2 and 255 characters")
    @Column(name = "passenger_middlename", length = 255)
    private String passengerMiddlename;

    @NotBlank(message = "Bus route number cannot be empty")
    @Size(
            min = 2,
            max = 15,
            message = "Bus route number must be between 2 and 15 characters")
    @Column(name = "bus_route_number", length = 15, nullable = false)
    private String busRouteNumber;

    @NotBlank(message = "QR-code cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "QR-code must be between 2 and 255 characters")
    @Column(name = "qr_code", length = 255, nullable = false)
    private String qrCode;

    @NotBlank(message = "Seat name cannot be empty")
    @Size(
            min = 2,
            max = 10,
            message = "Seat name must be between 2 and 10 characters")
    @Column(name = "seat_name", length = 10, nullable = false)
    private String seatName;

    @NotBlank(message = "Name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Name must be between 2 and 255 characters")
    @Column(name = "carrier_name", length = 255, nullable = false)
    private String carrierName;

    @NotBlank(message = "Departure bus point name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Departure bus point name must be between 2 and 255 characters")
    @Column(name = "departure_buspoint_name", length = 255, nullable = false)
    private String departureBuspointName;

    @NotBlank(message = "Arrival bus point name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Arrival bus point name must be between 2 and 255 characters")
    @Column(name = "arrival_buspoint_name", length = 255, nullable = false)
    private String arrivalBuspointName;

    @NotNull(message = "Departure date time isn't set")
    @Future
    @Column(name = "departure_datetime", nullable = false)
    private Timestamp departureDateTime;

    @Future
    @Column(name = "arrival_datetime")
    private Timestamp arrivalDatetime;

    @NotNull(message = "Price isn't set")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Value must be greater")
    @Column(name = "ticket_price", nullable = false)
    private BigDecimal price;

    @NotNull(message = "Bus trip isn't set")
    @ManyToOne
    @JoinColumn(name = "bustrip_id", nullable = false)
    private BusTrip busTrip;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    @Size(
            max = 255,
            message = "Departure bus point name must be less than 255 characters")
    @Column(name = "user_id", length = 255)
    private String userId;
}

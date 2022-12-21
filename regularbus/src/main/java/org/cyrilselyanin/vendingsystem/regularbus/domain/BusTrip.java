package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.*;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Bus;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.BusPoint;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Fare;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "bustrips")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusTrip {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bustrips_gen")
    @SequenceGenerator(
            name = "bustrips_gen",
            sequenceName="bustrips_seq",
            allocationSize = 1)
    @Column(name = "bustrip_id", nullable = false)
    private Long id;

    @NotNull(message = "Departure bus point isn't set")
    @ManyToOne
    @JoinColumn(name = "departure_buspoint_id", nullable = false)
    private BusPoint departureBusPoint;

    @NotNull(message = "Arrival bus point isn't set")
    @ManyToOne
    @JoinColumn(name = "arrival_buspoint_id", nullable = false)
    private BusPoint arrivalBusPoint;

    @NotNull(message = "Distance isn't set")
    @DecimalMin(
            value = "0.1",
            inclusive = true,
            message = "Value must be greater")
    @Column(name = "distance", nullable = false)
    private BigDecimal distance;

    @NotBlank(message = "Bus route number cannot be empty")
    @Size(
            min = 2,
            max = 15,
            message = "Bus route number must be between 2 and 15 characters")
    @Column(name = "bus_route_number", length = 15, nullable = false)
    private String busRouteNumber;

    @NotNull(message = "Departure date time isn't set")
    @Future
    @Column(name = "departure_datetime", nullable = false)
    private Timestamp departureDateTime;

    @NotNull(message = "averageBusSpeed isn't set")
    @Min(value = 5, message = "Average bus speed must be greater than 5")
    @Max(value = 130, message = "Average bus speed must be less than 130")
    @Column(name = "average_bus_speed", nullable = false)
    private Integer averageBusSpeed;

    @NotNull(message = "Bus isn't set")
    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @NotNull(message = "Fare isn't set")
    @ManyToOne
    @JoinColumn(name = "fare_id", nullable = false)
    private Fare fare;

    @OneToMany(mappedBy = "busTrip")
    private Set<Seat> seats;

    @OneToMany(mappedBy = "busTrip")
    private Set<Ticket> tickets;
}

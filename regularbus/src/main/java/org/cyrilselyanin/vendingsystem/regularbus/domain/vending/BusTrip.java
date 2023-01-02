package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.*;
import org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate.ValidateBeforeDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bustrips")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ValidateBeforeDate
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

    @NotNull(message = "Пункт отправления не указан")
    @ManyToOne
    @JoinColumn(name = "departure_buspoint_id", nullable = false)
    private BusPoint departureBusPoint;

    @NotNull(message = "Пункт прибытия не указан")
    @ManyToOne
    @JoinColumn(name = "arrival_buspoint_id", nullable = false)
    private BusPoint arrivalBusPoint;

    @NotNull(message = "Расстояние не указано")
    @DecimalMin(
            value = "0.1",
            inclusive = true,
            message = "Расстояние должно быть больше")
    @Column(name = "distance", nullable = false)
    private BigDecimal distance;

    @NotBlank(message = "Номер маршрута не указан")
    @Size(
            min = 2,
            max = 15,
            message = "Номер маршрута должен быть от 2 до 15 символов")
    @Column(name = "bus_route_number", length = 15, nullable = false)
    private String busRouteNumber;

    @NotNull(message = "Дата и время отправления не указаны")
    @Future
    @Column(name = "departure_datetime", nullable = false)
    private Timestamp departureDateTime;

//    @NotNull(message = "Средняя скорость на маршруте не указана")
//    @Min(value = 5, message = "Средняя скорость на рейсе должна быть более 5")
//    @Max(value = 130, message = "Средняя скорость на рейсе должна быть меньше 130")
//    @Column(name = "average_bus_speed", nullable = false)
//    private Integer averageBusSpeed;

    @NotNull(message = "Дата и время прибытия не указаны")
    @Future
    @Column(name = "arrival_datetime", nullable = false)
    private Timestamp arrivalDateTime;

    @NotNull(message = "Автобус не указан")
    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @NotNull(message = "Тариф не указан")
    @ManyToOne
    @JoinColumn(name = "fare_id", nullable = false)
    private Fare fare;

    @NotNull(message = "Перевозчик не указан")
    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "busTrip")
    private Set<Seat> seats = new HashSet<>();

    @OneToMany(mappedBy = "busTrip")
    private Set<Ticket> tickets = new HashSet<>();
}

package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.*;
import org.cyrilselyanin.vendingsystem.regularbus.validation.beforedate.ValidateBeforeDate;

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
@EqualsAndHashCode
@ValidateBeforeDate
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

    @NotNull(message = "Дата и время оформления не заполнены")
//    @FutureOrPresent
    @Column(name = "issue_datetime", nullable = false)
    private Timestamp issueDateTime;

    @NotBlank(message = "Фамилия пассажира не заполнена")
    @Size(
            min = 2,
            max = 255,
            message = "Фамилия пассажира должна быть от 2 до 255 символов")
    @Column(name = "passenger_lastname", length = 255)
    private String passengerLastname;

    @NotBlank(message = "Имя пассажира не заполнено")
    @Size(
            min = 2,
            max = 255,
            message = "Имя пассажира должно быть от 2 до 255 символов")
    @Column(name = "passenger_firstname", length = 255)
    private String passengerFirstname;

    @Size(
            min = 2,
            max = 255,
            message = "Отчество пассажира должно быть от 2 до 255 символов")
    @Column(name = "passenger_middlename", length = 255)
    private String passengerMiddlename;

    @NotBlank(message = "Номер маршрута автобуса не заполнен")
    @Size(
            min = 2,
            max = 15,
            message = "омер маршрута автобуса должен быть от 2 до 15 символов")
    @Column(name = "bus_route_number", length = 15, nullable = false)
    private String busRouteNumber;

    @NotBlank(message = "QR-код не заполнен")
    @Size(
            min = 2,
            max = 255,
            message = "QR-код должен быть от 2 до 255 символов")
    @Column(name = "qr_code", length = 255, nullable = false)
    private String qrCode;

    @NotBlank(message = "Место не заполнено")
    @Size(
            min = 1,
            max = 10,
            message = "Место должно быть от 1 до 10 символов")
    @Column(name = "seat_name", length = 10, nullable = false)
    private String seatName;

    @NotBlank(message = "Имя перевозчика не заполнено")
    @Size(
            min = 2,
            max = 255,
            message = "Имя перевозчика должно быть от 2 до 255 символов")
    @Column(name = "carrier_name", length = 255, nullable = false)
    private String carrierName;

    @NotBlank(message = "Пункт отправления не указан")
    @Size(
            min = 2,
            max = 255,
            message = "Пункт отправления должен быть от 2 до 255 символов")
    @Column(name = "departure_buspoint_name", length = 255, nullable = false)
    private String departureBuspointName;

    @NotBlank(message = "Пункт прибытия не указан")
    @Size(
            min = 2,
            max = 255,
            message = "Пункт прибытия должен быть от 2 до 255 символов")
    @Column(name = "arrival_buspoint_name", length = 255, nullable = false)
    private String arrivalBuspointName;

    @NotNull(message = "Дата и время отправления не указаны")
//    @Future
    @Column(name = "departure_datetime", nullable = false)
    private Timestamp departureDateTime;

    @NotNull(message = "Дата и время прибытия не указаны")
//    @Future
    @Column(name = "arrival_datetime", nullable = false)
    private Timestamp arrivalDateTime;

    @NotNull(message = "Цена не указана")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Значение цены должно быть больше 0")
    @Column(name = "ticket_price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    private TicketStatus status = TicketStatus.FREE;

    @NotNull(message = "Маршрут не указан")
    @ManyToOne
    @JoinColumn(name = "bustrip_id", nullable = false)
    private BusTrip busTrip;

    @Email(message = "Такой адрес электронной почты недопустим.")
    private String email;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = true)
//    private User user;
}

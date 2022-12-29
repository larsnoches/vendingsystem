package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "seats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Seat {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seats_gen")
    @SequenceGenerator(
            name = "seats_gen",
            sequenceName="seats_seq",
            allocationSize = 1)
    @Column(name = "seat_id", nullable = false)
    private Long id;

    @NotBlank(message = "Название места не заполнено")
    @Size(
            min = 2,
            max = 10,
            message = "Название места должно быть от 2 до 10 символов")
    @Column(name = "seat_name", length = 10, nullable = false)
    private String name;

    @NotNull(message = "Состояние места не заполнено")
//    @ManyToOne
//    @JoinColumn(name = "seat_state_id", nullable = false)
//    private SeatState seatState;
    @Column(name = "seat_is_occupied", nullable = false)
    private Boolean seatIsOccupied = false;

    @NotNull(message = "Маршрут не указан")
    @ManyToOne
    @JoinColumn(name = "bustrip_id", nullable = false)
    private BusTrip busTrip;
}

package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank(message = "Seat name cannot be empty")
    @Size(
            min = 2,
            max = 10,
            message = "Seat name must be between 2 and 10 characters")
    @Column(name = "seat_name", length = 10, nullable = false)
    private String name;

//    @NotNull(message = "Seat state isn't set")
//    @ManyToOne
//    @JoinColumn(name = "seat_state_id", nullable = false)
//    private SeatState seatState;
    @Column(name = "seat_is_occupied", nullable = false)
    private Boolean seatIsOccupied;

    @NotNull(message = "Bus trip isn't set")
    @ManyToOne
    @JoinColumn(name = "bustrip_id", nullable = false)
    private BusTrip busTrip;
}

package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "buses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Bus {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "buses_gen"
    )
    @SequenceGenerator(
            name = "buses_gen",
            sequenceName = "buses_seq",
            allocationSize = 1
    )
    @Column(name = "bus_id", nullable = false)
    private Long id;

    @NotBlank(message = "Make model cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Make model must be between 2 and 255 characters")
    @Column(name = "bus_make_model", length = 255, nullable = false)
    private String makeModel;

    @NotBlank(message = "Manufacturer country cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Manufacturer country must be between 2 and 255 characters")
    @Column(name = "manufacturer_country", length = 255, nullable = false)
    private String manufacturerCountry;

    @NotNull(message = "Year of manufacture isn't set")
    @Min(value = 1920, message = "Year of manufacture must be more")
    @Digits(
            integer = 4,
            fraction = 0,
            message = "Year of manufacture must be with 4 digit length"
    )
    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @NotNull(message = "Seat count isn't set")
    @Min(value = 2, message = "Seat count must be more than 1")
    @Max(value = 200, message = "Seat count must be less than 200")
    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;

    @NotBlank(message = "Reg number cannot be empty")
    @Size(
            min = 4,
            max = 16,
            message = "Reg number must be between 4 and 16 characters")
    @Column(name = "bus_reg_number", length = 50, nullable = false)
    private String regNumber;

    @NotNull(message = "Carrier isn't set")
    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "bus")
    private Set<BusTrip> busTrips;
}
package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.*;
import org.cyrilselyanin.vendingsystem.regularbus.domain.vending.Carrier;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "fares")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fare {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fares_gen")
    @SequenceGenerator(
            name = "fares_gen",
            sequenceName="fares_seq",
            allocationSize = 1)
    @Column(name = "fare_id", nullable = false)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Name must be between 2 and 255 characters")
    @Column(name = "fare_name", length = 255, nullable = false)
    private String name;

    @NotNull(message = "Price isn't set")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Value must be greater")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message = "Carrier isn't set")
    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "fare")
    private Set<BusTrip> busTrips;
}

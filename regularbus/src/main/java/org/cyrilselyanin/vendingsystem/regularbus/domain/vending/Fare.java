package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
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

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(
            min = 2,
            max = 255,
            message = "Название тарифа должно быть от 2 до 255 символов")
    @Column(name = "fare_name", length = 255, nullable = false)
    private String name;

    @NotNull(message = "Цена тарифа не указана")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Цена тарифа должна быть больше")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message = "Перевозчик не указан")
    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "fare")
    private Set<BusTrip> busTrips = new HashSet<>();
}

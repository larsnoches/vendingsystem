package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
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

    @NotBlank(message = "Марка и модель не могу быть пустыми")
    @Size(
            min = 2,
            max = 255,
            message = "Марка и модель должны быть от 2 до 255 символов")
    @Column(name = "bus_make_model", length = 255, nullable = false)
    private String makeModel;

    @NotBlank(message = "Страна-изготовитель не может быть пустой")
    @Size(
            min = 2,
            max = 255,
            message = "Страна-изготовитель должна быть от 2 до 255 символов")
    @Column(name = "manufacturer_country", length = 255, nullable = false)
    private String manufacturerCountry;

    @NotNull(message = "Год выпуска не указан")
    @Min(value = 1920, message = "Год выпуска должен быть больше")
    @Digits(
            integer = 4,
            fraction = 0,
            message = "Год выпуска должен быть длиной в 4 цифры"
    )
    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;

    @NotNull(message = "Количество мест не указано")
    @Min(value = 2, message = "Количество мест должно быть больше 1")
    @Max(value = 200, message = "Количество мест должно быть меньше 200")
    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;

    @NotBlank(message = "Государственный номер не указан")
    @Size(
            min = 4,
            max = 16,
            message = "Государственный номер должен быть от 4 до 16 символов")
    @Column(name = "bus_reg_number", length = 50, nullable = false)
    private String regNumber;

    @NotNull(message = "Перевозчик не указан")
    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @OneToMany(mappedBy = "bus")
    private Set<BusTrip> busTrips = new HashSet<>();
}
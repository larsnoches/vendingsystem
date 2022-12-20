package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "carriers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Carrier {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "carriers_gen")
    @SequenceGenerator(
            name = "carriers_gen",
            sequenceName="carriers_seq",
            allocationSize = 1)
    @Column(name = "carrier_id", nullable = false)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(
            min = 2,
            max = 255,
            message = "Имя должно быть от 2 до 255 символов")
    @Column(name = "carrier_name", length = 255, nullable = false)
    private String name;

    @NotBlank(message = "ИНН не указан")
    @Size(
            min = 6,
            max = 16,
            message = "Некорректный ИНН"
    )
    @Column(name = "carrier_inn", length = 16, nullable = false)
    @Pattern(regexp = "^[0-9]{1,16}$") // digits in a string
    private String inn;

    @NotBlank(message = "Адрес не может быть пустым")
    @Size(
            min = 7,
            max = 255,
            message = "Адрес должен быть от 7 до 255 символов")
    @Column(name = "carrier_address", length = 255, nullable = false)
    private String address;

    @OneToMany(mappedBy = "carrier")
    private Set<Bus> buses;

    @OneToMany(mappedBy = "carrier")
    private Set<Fare> fares;
}

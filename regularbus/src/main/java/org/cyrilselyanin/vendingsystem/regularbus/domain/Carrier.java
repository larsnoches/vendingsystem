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

    @NotBlank(message = "Name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Name must be between 2 and 255 characters")
    @Column(name = "carrier_name", length = 255, nullable = false)
    private String name;

    @NotBlank(message = "INN isn't set")
    @Size(
            min = 6,
            max = 16,
            message = "Wrong size of INN"
    )
    @Column(name = "carrier_inn", length = 16, nullable = false)
    @Pattern(regexp = "^[0-9]{1,16}$") // digits in a string
    private String inn;

    @NotBlank(message = "Address cannot be empty")
    @Size(
            min = 7,
            max = 255,
            message = "Address must be between 7 and 255 characters")
    @Column(name = "carrier_address", length = 255, nullable = false)
    private String address;

    @OneToMany(mappedBy = "carrier")
    private Set<Bus> buses;

    @OneToMany(mappedBy = "carrier")
    private Set<Fare> fares;
}

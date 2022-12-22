package org.cyrilselyanin.vendingsystem.regularbus.domain.vending;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "buspoints")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusPoint {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "buspoints_gen")
    @SequenceGenerator(
            name = "buspoints_gen",
            sequenceName="buspoints_seq",
            allocationSize = 1)
    @Column(name = "buspoint_id", nullable = false)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(
            min = 2,
            max = 255,
            message = "Имя должно быть от 2 до 255 символов")
    @Column(name = "buspoint_name", length = 255, nullable = false)
    private String name;

    @NotBlank(message = "Адрес не может быть пустым")
    @Size(
            min = 2,
            max = 255,
            message = "Адрес должен быть от 2 до 255 символов")
    @Column(name = "buspoint_address", length = 255, nullable = false)
    private String address;

    @NotNull(message = "Тип остановочного пункта не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "buspoint_type", nullable = false)
    private BusPointType busPointType;

    @OneToMany(mappedBy = "departureBusPoint")
    private Set<BusTrip> departureBusTrips = new HashSet<>();

    @OneToMany(mappedBy = "arrivalBusPoint")
    private Set<BusTrip> arrivalBusTrips = new HashSet<>();
}

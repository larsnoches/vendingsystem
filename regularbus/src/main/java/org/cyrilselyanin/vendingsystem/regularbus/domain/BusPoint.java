package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "Name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Name must be between 2 and 255 characters")
    @Column(name = "buspoint_name", length = 255, nullable = false)
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Address must be between 2 and 255 characters")
    @Column(name = "buspoint_address", length = 255, nullable = false)
    private String address;

    @NotNull(message = "Bus point type isn't set")
    @ManyToOne
    @JoinColumn(name = "buspoint_type_id", nullable = false)
    private BusPointType busPointType;

    @OneToMany(mappedBy = "departureBusPoint")
    private Set<BusTrip> departureBusTrips;

    @OneToMany(mappedBy = "arrivalBusPoint")
    private Set<BusTrip> arrivalBusTrips;
}

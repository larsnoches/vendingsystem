package org.cyrilselyanin.vendingsystem.regularbus.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "buspoints_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusPointType {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "buspoints_types_gen"
    )
    @SequenceGenerator(
            name = "buspoints_types_gen",
            sequenceName = "buspoints_types_seq",
            allocationSize = 1
    )
    @Column(name = "buspoint_type_id", nullable = false)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(
            min = 2,
            max = 255,
            message = "Name must be between 2 and 255 characters")
    @Column(name = "buspoint_type_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "busPointType")
    private Set<BusPoint> busPoints;
}

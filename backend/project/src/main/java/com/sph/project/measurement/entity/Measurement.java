package com.sph.project.measurement.entity;

import com.sph.project.location.entity.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_id")
    private Long systemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_system_id", nullable = false)
    private Location location;

    @Column(name = "max_val")
    private Double maxVal;

    @Column(name = "min_val")
    private Double minVal;

    @Column(name = "avg_val")
    private Double avgVal;

}

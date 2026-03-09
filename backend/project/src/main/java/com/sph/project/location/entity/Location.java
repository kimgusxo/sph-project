package com.sph.project.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @Column(name = "system_id")
    private Long systemId;

    @Column(name = "x", nullable = false)
    private Double x;

    @Column(name = "y", nullable = false)
    private Double y;

    @Column(name = "address")
    private String address;

    @Column(name = "light_source_count", nullable = false)
    private Integer lightSourceCount;

    // Point 4326: 위도/경도를 사용하는 시스템
    @Column(name = "geometry", columnDefinition = "geometry(Point,4326)")
    private Point geometry;

}

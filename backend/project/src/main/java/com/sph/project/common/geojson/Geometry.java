package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Geometry {
    private String type;
    private List<Double> coordinates;

    public static Geometry from(Location location) {
        return Geometry.builder()
                .type("Point")
                .coordinates(List.of(location.getX(), location.getY()))
                .build();
    }
}
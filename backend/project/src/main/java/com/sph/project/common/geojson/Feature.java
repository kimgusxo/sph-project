package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Feature {
    private String type;
    private Geometry geometry;
    private Properties properties;

    public static Feature from(Location location) {
        return Feature.builder()
                .type("Feature")
                .geometry(Geometry.from(location))
                .properties(Properties.from(location))
                .build();
    }
}

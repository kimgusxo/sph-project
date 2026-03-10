package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "GeoJSON Feature 속성")
public class Feature {

    @Schema(description = "Feature 타입", example = "Feature")
    private String type;

    @Schema(description = "Geometry 정보")
    private Geometry geometry;

    @Schema(description = "속성 정보")
    private Properties properties;

    public static Feature from(Location location) {
        return Feature.builder()
                .type("Feature")
                .geometry(Geometry.from(location))
                .properties(Properties.from(location))
                .build();
    }
}

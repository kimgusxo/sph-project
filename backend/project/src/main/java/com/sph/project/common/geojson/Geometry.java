package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "GeoJSON Geometry 속성")
public class Geometry {

    @Schema(description = "Geometry 타입", example = "Point")
    private String type;

    @ArraySchema(schema = @Schema(description = "[x, y] 좌표", example = "[127.1234, 37.1234]"))
    private List<Double> coordinates;

    public static Geometry from(Location location) {
        return Geometry.builder()
                .type("Point")
                .coordinates(List.of(location.getX(), location.getY()))
                .build();
    }
}
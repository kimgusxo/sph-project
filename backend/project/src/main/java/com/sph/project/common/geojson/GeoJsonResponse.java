package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "GeoJSON")
public class GeoJsonResponse {

    @Schema(description = "GeoJSON 타입", example = "FeatureCollection")
    private String type;

    @Schema(description = "GeoJSON Feature 리스트")
    private List<Feature> features;

    public static GeoJsonResponse from(List<Location> locations) {
        List<Feature> features = locations.stream()
                .map(Feature::from)
                .toList();

        return GeoJsonResponse.builder()
                .type("FeatureCollection")
                .features(features)
                .build();
    }
}
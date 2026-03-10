package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GeoJsonResponse {

    private String type;
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
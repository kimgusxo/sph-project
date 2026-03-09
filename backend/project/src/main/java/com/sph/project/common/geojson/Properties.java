package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Properties {
    private Long systemId;
    private String address;
    private Integer lightSourceCount;

    public static Properties from(Location location) {
        return Properties.builder()
                .systemId(location.getSystemId())
                .address(location.getAddress())
                .lightSourceCount(location.getLightSourceCount())
                .build();
    }
}

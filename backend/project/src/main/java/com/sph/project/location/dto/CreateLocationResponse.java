package com.sph.project.location.dto;

import com.sph.project.location.entity.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateLocationResponse {
    private Long systemId;
    private String address;
    private Double x;
    private Double y;
    private Integer lightSourceCount;

    public static CreateLocationResponse from(Location location) {
        return CreateLocationResponse.builder()
                .systemId(location.getSystemId())
                .address(location.getAddress())
                .x(location.getX())
                .y(location.getY())
                .lightSourceCount(location.getLightSourceCount())
                .build();
    }
}

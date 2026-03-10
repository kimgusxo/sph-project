package com.sph.project.common.geojson;

import com.sph.project.location.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "GeoJSON Properties 속성")
public class Properties {

    @Schema(description = "위치 시스템 ID", example = "16000")
    private Long systemId;

    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로")
    private String address;

    @Schema(description = "광원 수", example = "3")
    private Integer lightSourceCount;

    public static Properties from(Location location) {
        return Properties.builder()
                .systemId(location.getSystemId())
                .address(location.getAddress())
                .lightSourceCount(location.getLightSourceCount())
                .build();
    }
}

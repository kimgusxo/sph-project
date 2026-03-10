package com.sph.project.location.dto;

import com.sph.project.location.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "측정 위치 생성 응답")
public class CreateLocationResponse {

    @Schema(description = "위치 시스템 ID", example = "16000")
    private Long systemId;

    @Schema(description = "측정 위치 주소", example = "서울특별시 강남구 테헤란로")
    private String address;

    @Schema(description = "경도(X)", example = "127.1234")
    private Double x;

    @Schema(description = "위도(Y)", example = "37.1234")
    private Double y;

    @Schema(description = "광원 수", example = "3")
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

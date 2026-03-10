package com.sph.project.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "측정 위치 생성 요청")
public class CreateLocationRequest {

    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로")
    private String address;

    @Schema(description = "경도(X)", example = "127.1234")
    private Double x;

    @Schema(description = "위도(Y)", example = "37.1234")
    private Double y;
}

package com.sph.project.measurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "측정값 생성 요청")
public class CreateMeasurementRequest {

    @Schema(description = "측정 위치 시스템 ID", example = "16000")
    private Long locationSystemId;

    @Schema(description = "최대값", example = "100.1")
    private Double maxVal;

    @Schema(description = "최소값", example = "0.1")
    private Double minVal;

    @Schema(description = "평균값", example = "50.1")
    private Double avgVal;
}
package com.sph.project.measurement.dto;

import com.sph.project.measurement.entity.Measurement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "측정값 생성 응답")
public class CreateMeasurementResponse {

    @Schema(description = "측정값 시스템 ID", example = "160000")
    private Long systemId;

    @Schema(description = "측정 위치 시스템 ID", example = "101")
    private Long locationSystemId;

    @Schema(description = "최대값", example = "100.1")
    private Double maxVal;

    @Schema(description = "최소값", example = "0.1")
    private Double minVal;

    @Schema(description = "평균값", example = "50.1")
    private Double avgVal;

    public static CreateMeasurementResponse from(Measurement measurement) {
        return CreateMeasurementResponse.builder()
                .systemId(measurement.getSystemId())
                .locationSystemId(measurement.getLocation().getSystemId())
                .maxVal(measurement.getMaxVal())
                .minVal(measurement.getMinVal())
                .avgVal(measurement.getAvgVal())
                .build();
    }
}
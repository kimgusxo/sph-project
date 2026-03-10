package com.sph.project.measurement.dto;

import com.sph.project.measurement.entity.Measurement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMeasurementResponse {
    private Long systemId;
    private Long locationSystemId;
    private Double maxVal;
    private Double minVal;
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
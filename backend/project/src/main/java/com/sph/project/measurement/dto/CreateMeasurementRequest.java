package com.sph.project.measurement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMeasurementRequest {
    private Long locationSystemId;
    private Double maxVal;
    private Double minVal;
    private Double avgVal;
}
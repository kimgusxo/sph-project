package com.sph.project.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementExcel {
    private Long systemId;
    private Long locationSystemId;
    private Double maxVal;
    private Double minVal;
    private Double avgVal;
}

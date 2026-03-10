package com.sph.project.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationExcel {
    private Long systemId;
    private Double x;
    private Double y;
    private String address;
    private Integer lightSourceCount;
}

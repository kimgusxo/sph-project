package com.sph.project.location.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateLocationRequest {
    private String address;
    private Double x;
    private Double y;
}

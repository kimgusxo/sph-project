package com.sph.project.measurement.controller;

import com.sph.project.measurement.dto.CreateMeasurementRequest;
import com.sph.project.measurement.dto.CreateMeasurementResponse;
import com.sph.project.measurement.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @PostMapping
    public ResponseEntity<CreateMeasurementResponse> createMeasurement(@RequestBody CreateMeasurementRequest req) {
        return new ResponseEntity<>(measurementService.createMeasurement(req), HttpStatus.CREATED);
    }
}

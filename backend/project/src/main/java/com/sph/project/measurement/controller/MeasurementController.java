package com.sph.project.measurement.controller;

import com.sph.project.common.error.ErrorResponse;
import com.sph.project.measurement.dto.CreateMeasurementRequest;
import com.sph.project.measurement.dto.CreateMeasurementResponse;
import com.sph.project.measurement.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
@Tag(name = "Measurement", description = "측정값 API")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Operation(summary = "측정값 등록", description = "특정 위치에 대한 측정값(max/min/avg)을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = CreateMeasurementResponse.class))),
            @ApiResponse(responseCode = "400", description = "측정 위치 시스템 ID 또는 측정값 전부 누락", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "측정 위치가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CreateMeasurementResponse> createMeasurement(@RequestBody CreateMeasurementRequest req) {
        return new ResponseEntity<>(measurementService.createMeasurement(req), HttpStatus.CREATED);
    }
}

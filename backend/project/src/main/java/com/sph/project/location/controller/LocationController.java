package com.sph.project.location.controller;

import com.sph.project.common.error.CustomException;
import com.sph.project.common.error.ErrorCode;
import com.sph.project.common.error.ErrorResponse;
import com.sph.project.common.geojson.GeoJsonResponse;
import com.sph.project.location.dto.CreateLocationRequest;
import com.sph.project.location.dto.CreateLocationResponse;
import com.sph.project.location.service.LocationService;
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
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Location", description = "위치 정보 API")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "측정 위치 등록", description = "주소 또는 좌표를 기반으로 새로운 측정 위치를 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = CreateLocationResponse.class))),
            @ApiResponse(responseCode = "400", description = "주소 또는 좌표 누락", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "지오코딩 결과 없음 또는 좌표 범위 초과", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "지오코딩 API 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "504", description = "지오코딩 API 타임아웃", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CreateLocationResponse> createLocation(@RequestBody CreateLocationRequest req) {
        return new ResponseEntity<>(locationService.createLocation(req), HttpStatus.CREATED);
    }

    @Operation(summary = "반경 내 위치 조회", description = "주소 또는 좌표를 기준으로 주변 측정 위치를 GeoJSON 형식으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = GeoJsonResponse.class))),
            @ApiResponse(responseCode = "400", description = "주소/좌표 파라미터 누락", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "지오코딩 결과 없음 또는 좌표 범위 초과", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "지오코딩 API 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "504", description = "지오코딩 API 타임아웃", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/around")
    public ResponseEntity<GeoJsonResponse> searchAround(@RequestParam(required = false) String address,
                                                        @RequestParam(required = false) Double x,
                                                        @RequestParam(required = false) Double y) {
        if(address == null && x == null && y == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        } else if(address == null) {
            return new ResponseEntity<>(locationService.searchAroundByCoordinate(x, y), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(locationService.searchAroundByAddress(address), HttpStatus.OK);
        }
    }

}

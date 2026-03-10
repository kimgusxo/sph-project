package com.sph.project.location.controller;

import com.sph.project.common.error.CustomException;
import com.sph.project.common.error.ErrorCode;
import com.sph.project.common.geojson.GeoJsonResponse;
import com.sph.project.location.dto.CreateLocationRequest;
import com.sph.project.location.dto.CreateLocationResponse;
import com.sph.project.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<CreateLocationResponse> createLocation(@RequestBody CreateLocationRequest req) {
        return new ResponseEntity<>(locationService.createLocation(req), HttpStatus.CREATED);
    }

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

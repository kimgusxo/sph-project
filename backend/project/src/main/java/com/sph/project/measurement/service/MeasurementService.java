package com.sph.project.measurement.service;

import com.sph.project.common.error.CustomException;
import com.sph.project.common.error.ErrorCode;
import com.sph.project.location.entity.Location;
import com.sph.project.location.repository.LocationRepository;
import com.sph.project.measurement.dto.CreateMeasurementRequest;
import com.sph.project.measurement.dto.CreateMeasurementResponse;
import com.sph.project.measurement.entity.Measurement;
import com.sph.project.measurement.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public CreateMeasurementResponse createMeasurement(CreateMeasurementRequest req) {
        Location location = locationRepository.findById(req.getLocationSystemId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEASUREMENT_LOCATION_NOT_FOUND));

        Measurement measurement = Measurement.builder()
                .location(location)
                .maxVal(req.getMaxVal())
                .minVal(req.getMinVal())
                .avgVal(req.getAvgVal())
                .build();

        Measurement savedMeasurement = measurementRepository.save(measurement);
        location.increaseLightSourceCount();

        return CreateMeasurementResponse.from(savedMeasurement);
    }
}
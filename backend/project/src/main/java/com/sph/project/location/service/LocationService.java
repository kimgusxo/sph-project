package com.sph.project.location.service;

import com.sph.project.common.error.CustomException;
import com.sph.project.common.error.ErrorCode;
import com.sph.project.common.geojson.GeoJsonResponse;
import com.sph.project.common.util.GeoCodingResponse;
import com.sph.project.common.util.GeoCodingService;
import com.sph.project.location.dto.CreateLocationRequest;
import com.sph.project.location.dto.CreateLocationResponse;
import com.sph.project.location.entity.Location;
import com.sph.project.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private static final double RADIUS = 3000.0;

    private final LocationRepository locationRepository;
    private final GeoCodingService geoCodingService;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public CreateLocationResponse createLocation(CreateLocationRequest createLocationRequest) {
        String address = createLocationRequest.getAddress();
        Double x = createLocationRequest.getX();
        Double y = createLocationRequest.getY();

        if (x == null && y == null) {
            GeoCodingResponse res = geoCodingService.geocode(address);

            if (res == null) {
                throw new CustomException(ErrorCode.GEOCODING_NOT_FOUND);
            }

            x = res.getX();
            y = res.getY();
        } else {
            GeoCodingResponse res = geoCodingService.reverseGeocode(x, y);

            if (res == null) {
                throw new CustomException(ErrorCode.REVERSE_GEOCODING_NOT_FOUND);
            }

            address = res.getAddress();
        }

        Point point = geometryFactory.createPoint(new Coordinate(x, y));

        Location location = Location.builder()
                .address(address)
                .x(x)
                .y(y)
                .lightSourceCount(0)
                .geometry(point)
                .build();

        Location savedLocation = locationRepository.save(location);

        return CreateLocationResponse.from(savedLocation);
    }

    @Transactional
    public GeoJsonResponse searchAroundByCoordinate(Double x, Double y) {
        List<Location> locations = locationRepository.findAround(x, y, RADIUS);
        return GeoJsonResponse.from(locations);
    }

    @Transactional
    public GeoJsonResponse searchAroundByAddress(String address) {
        if (address == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        GeoCodingResponse res = geoCodingService.geocode(address);

        if (res == null) {
            throw new CustomException(ErrorCode.GEOCODING_NOT_FOUND);
        }

        List<Location> locations = locationRepository.findAround(res.getX(), res.getY(), RADIUS);

        return GeoJsonResponse.from(locations);
    }
}

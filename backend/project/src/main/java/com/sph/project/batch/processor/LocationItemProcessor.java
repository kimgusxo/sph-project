package com.sph.project.batch.processor;

import com.sph.project.batch.dto.LocationExcel;
import com.sph.project.common.util.GeoCodingResponse;
import com.sph.project.common.util.GeoCodingService;
import com.sph.project.location.entity.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationItemProcessor implements ItemProcessor<LocationExcel, Location> {

    private final GeoCodingService geoCodingService;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public Location process(LocationExcel item) throws Exception {
        GeoCodingResponse response;

        if (item.getAddress() == null || item.getAddress().isEmpty()) {
            log.info("systemId: {} -> reverse geocoding 수행", item.getSystemId());
            response = geoCodingService.reverseGeocode(item.getX(), item.getY());
        } else if (item.getX() == null && item.getY() == null) {
            log.info("systemId: {} -> geocoding 수행", item.getSystemId());
            response = geoCodingService.geocode(item.getAddress());
        } else {
            response = GeoCodingResponse.builder()
                    .x(item.getX())
                    .y(item.getY())
                    .address(item.getAddress())
                    .build();
        }

        // Point geometry 생성
        Point point = geometryFactory.createPoint(new Coordinate(response.getX(), response.getY()));
            return Location.builder()
                    .systemId(item.getSystemId())
                    .address(response.getAddress())
                    .x(response.getX())
                    .y(response.getY())
                    .geometry(point)
                    .lightSourceCount(item.getLightSourceCount())
                    .build();
    }
}

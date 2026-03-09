package com.sph.project.batch.processor;

import com.sph.project.batch.dto.MeasurementExcel;
import com.sph.project.location.entity.Location;
import com.sph.project.location.repository.LocationRepository;
import com.sph.project.measurement.entity.Measurement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeasurementItemProcessor implements ItemProcessor<MeasurementExcel, Measurement> {

    private final LocationRepository locationRepository;

    @Override
    public Measurement process(MeasurementExcel item) {
        Optional<Location> location = locationRepository.findById(item.getLocationSystemId());

        if(location.isEmpty()) {
            throw new RuntimeException("나중에 예외 처리");
        }

        Measurement measurement = Measurement.builder()
                .systemId(item.getSystemId())
                .location(location.get())
                .maxVal(item.getMaxVal())
                .minVal(item.getMinVal())
                .avgVal(item.getAvgVal())
                .build();

        log.info("measurement systemId={} -> 변환 완료", item.getSystemId());
        return measurement;
    }
}

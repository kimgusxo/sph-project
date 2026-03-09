package com.sph.project.batch.writer;

import com.sph.project.measurement.entity.Measurement;
import com.sph.project.measurement.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeasurementItemWriter implements ItemWriter<Measurement> {

    private final MeasurementRepository measurementRepository;

    @Override
    public void write(Chunk<? extends Measurement> chunk) throws Exception {
        measurementRepository.saveAll(chunk.getItems());
        log.info("measurement {}건 저장 완료", chunk.size());

    }
}

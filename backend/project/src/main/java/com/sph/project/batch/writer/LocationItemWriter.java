package com.sph.project.batch.writer;

import com.sph.project.location.entity.Location;
import com.sph.project.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationItemWriter implements ItemWriter<Location> {

    private final LocationRepository locationRepository;

    @Override
    public void write(Chunk<? extends Location> chunk) {
        locationRepository.saveAll(chunk.getItems());
        log.info("location {}건 저장 완료", chunk.size());
    }
}
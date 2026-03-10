package com.sph.project.batch.writer;

import com.sph.project.location.entity.Location;
import com.sph.project.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationItemWriter implements ItemWriter<Location> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Location> chunk) {
        String sql = """
                INSERT INTO location (
                    system_id,
                    address,
                    x,
                    y,
                    light_source_count,
                    geometry
                ) VALUES (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ST_SetSRID(ST_MakePoint(?, ?), 4326)
                )
                """;

        jdbcTemplate.batchUpdate(
                sql,
                chunk.getItems(),
                chunk.size(),
                (ps, location) -> {
                    ps.setLong(1, location.getSystemId());
                    ps.setString(2, location.getAddress());
                    ps.setDouble(3, location.getX());
                    ps.setDouble(4, location.getY());
                    ps.setInt(5, location.getLightSourceCount());
                    ps.setDouble(6, location.getX());
                    ps.setDouble(7, location.getY());
                }
        );

        log.info("location {}건 저장 완료", chunk.size());
    }
}
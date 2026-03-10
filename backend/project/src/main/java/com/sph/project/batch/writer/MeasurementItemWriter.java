package com.sph.project.batch.writer;

import com.sph.project.measurement.entity.Measurement;
import com.sph.project.measurement.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeasurementItemWriter implements ItemWriter<Measurement> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends Measurement> chunk) {
        String sql = """
                INSERT INTO measurement (
                    system_id,
                    max_val,
                    min_val,
                    avg_val,
                    location_system_id
                ) VALUES (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
                """;

        jdbcTemplate.batchUpdate(
                sql,
                chunk.getItems(),
                chunk.size(),
                (ps, measurement) -> {
                    ps.setLong(1, measurement.getSystemId());
                    ps.setObject(2, measurement.getMaxVal(), Types.DOUBLE);
                    ps.setObject(3, measurement.getMinVal(), Types.DOUBLE);
                    ps.setObject(4, measurement.getAvgVal(), Types.DOUBLE);
                    ps.setLong(5, measurement.getLocation().getSystemId());
                }
        );

        log.info("measurement {}건 저장 완료", chunk.size());
    }
}
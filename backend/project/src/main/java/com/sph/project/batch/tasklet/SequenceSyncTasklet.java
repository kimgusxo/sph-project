package com.sph.project.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SequenceSyncTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        jdbcTemplate.execute("""
                SELECT setval(
                    pg_get_serial_sequence('location', 'system_id'),
                    COALESCE((SELECT MAX(system_id) FROM location), 0) + 1,
                    false
                )
                """);

        jdbcTemplate.execute("""
                SELECT setval(
                    pg_get_serial_sequence('measurement', 'system_id'),
                    COALESCE((SELECT MAX(system_id) FROM measurement), 0) + 1,
                    false
                )
                """);

        log.info("location, measurement 시퀀스 동기화 완료");
        return RepeatStatus.FINISHED;
    }
}
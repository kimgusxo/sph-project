package com.sph.project.batch.config;

import com.sph.project.batch.dto.MeasurementExcel;
import com.sph.project.batch.processor.MeasurementItemProcessor;
import com.sph.project.batch.writer.MeasurementItemWriter;
import com.sph.project.measurement.entity.Measurement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MeasurementJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PoiItemReader<MeasurementExcel> reader;
    private final MeasurementItemProcessor processor;
    private final MeasurementItemWriter writer;

    @Bean
    public Step measurementStep() {
        return new StepBuilder("measurementStep", jobRepository)
                .<MeasurementExcel, Measurement>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

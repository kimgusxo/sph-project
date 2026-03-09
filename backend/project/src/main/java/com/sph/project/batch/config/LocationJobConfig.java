package com.sph.project.batch.config;

import com.sph.project.batch.dto.LocationExcel;
import com.sph.project.batch.processor.LocationItemProcessor;
import com.sph.project.batch.writer.LocationItemWriter;
import com.sph.project.location.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LocationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PoiItemReader<LocationExcel> reader;
    private final LocationItemProcessor processor;
    private final LocationItemWriter writer;

    @Bean
    public Job locationJob() {
        return new JobBuilder("locationJob", jobRepository)
                .start(locationStep())
                .build();
    }

    @Bean
    public Step locationStep() {
        return new StepBuilder("locationStep", jobRepository)
                .<LocationExcel, Location>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

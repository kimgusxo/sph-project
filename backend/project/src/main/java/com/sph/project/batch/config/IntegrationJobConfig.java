package com.sph.project.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class IntegrationJobConfig {

    private final JobRepository jobRepository;

    private final Step locationStep;
    private final Step measurementStep;
    private final Step sequenceSyncStep;

    @Bean
    public Job integrationJob() {
        return new JobBuilder("integrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(locationStep)
                .next(measurementStep)
                .next(sequenceSyncStep)
                .build();
    }

}

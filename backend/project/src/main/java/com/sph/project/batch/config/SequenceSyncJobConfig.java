package com.sph.project.batch.config;

import com.sph.project.batch.tasklet.SequenceSyncTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SequenceSyncJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SequenceSyncTasklet  sequenceSyncTasklet;

    @Bean
    public Step sequenceSyncStep() {
        return new StepBuilder("sequenceSyncStep", jobRepository)
                .tasklet(sequenceSyncTasklet, transactionManager)
                .build();
    }
}

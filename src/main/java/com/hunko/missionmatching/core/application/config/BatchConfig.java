package com.hunko.missionmatching.core.application.config;

import com.hunko.missionmatching.core.domain.ReviewRequest;
import com.hunko.missionmatching.storage.ReviewRequestEntity;
import com.hunko.missionmatching.storage.ReviewRequestEntityMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    @Value("${chunkSize:1000}")
    private int chunkSize; // Parameter로 chunkSize를 던지면 해당 값으로, 없으면 1000을 기본으로

    @Bean
    public Job updateRejectJob() {
        return new JobBuilder("updateRejectJob", jobRepository)
                .start(reviewMatchingStep())
                .build();
    }

    @Bean
    public Step reviewMatchingStep() {
        return new StepBuilder("rejectStep", jobRepository)
                .<ReviewRequestEntity, ReviewRequestEntity>chunk(chunkSize, transactionManager)
                .reader(reviewEntityreader(null))
                .processor(rejectProcess())
                .writer(writer())
                .build();
    }

    @Bean
    public ExecutorService batchMatchExcutorService(){
        return Executors.newFixedThreadPool(4);
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ReviewRequestEntity> reviewEntityreader(
            @Value("#{jobParameters['missionId']}") Long missionId) {

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("missionId", missionId);

        JpaPagingItemReader<ReviewRequestEntity> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select r From ReviewRequestEntity r where r.missionId = :missionId and r.reviewRequestType = REQUEST");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);

        return reader;
    }

    @Bean
    public ItemProcessor<ReviewRequestEntity, ReviewRequestEntity> rejectProcess() {
        return item -> {
            ReviewRequest domain = ReviewRequestEntityMapper.toReviewRequest(item);
            if(domain.getGithubUri() == null) domain.reject();
            return ReviewRequestEntityMapper.toEntity(domain);
        };
    }

    @Bean
    public ItemWriter<ReviewRequestEntity> writer () {
        JpaItemWriter<ReviewRequestEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}

package com.hunko.missionmatching.core.application.config;

import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.storage.ReviewAssignmentEntity;
import com.hunko.missionmatching.storage.ReviewAssignmentEntityMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
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
public class ReviewCloseBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean(name = "assignmentCloseJob")
    public Job assignmentCloseJob() {
        return new JobBuilder("assignmentCloseJob", jobRepository)
                .start(assignmentCloseStep())
                .build();
    }

    @Bean
    public Step assignmentCloseStep() {
        return new StepBuilder("closeStep", jobRepository)
                .<ReviewAssignmentEntity, ReviewAssignmentEntity>chunk(chunkSize, transactionManager)
                .reader(reviewAssignmentBatchReader(null))
                .processor(closeProcess())
                .writer(reviewAssignmentWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ReviewAssignmentEntity> reviewAssignmentBatchReader(
            @Value("#{jobParameters['missionId']}") Long missionId) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("missionId", missionId);

        JpaPagingItemReader<ReviewAssignmentEntity> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select r from ReviewAssignmentEntity r where r.missionId = :missionId and r.reviewAssignmentStatus = NOT_CLEARED");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        return reader;
    }

    @Bean
    public ItemProcessor<ReviewAssignmentEntity, ReviewAssignmentEntity> closeProcess() {
        return item -> {
            ReviewAssignment reviewAssignment = ReviewAssignmentEntityMapper.toReviewAssignment(item);
            reviewAssignment.timeOut();
            return ReviewAssignmentEntityMapper.toEntity(reviewAssignment);
        };
    }

    @Bean
    public ItemWriter<ReviewAssignmentEntity> reviewAssignmentWriter() {
        JpaItemWriter<ReviewAssignmentEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}

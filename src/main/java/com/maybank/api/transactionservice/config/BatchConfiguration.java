package com.maybank.api.transactionservice.config;

import com.maybank.api.transactionservice.model.UserTransaction;
import com.maybank.api.transactionservice.util.TransactionFieldSetMapper;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    @Bean
    public Resource fileResource() {
        return new ClassPathResource("dataSource.txt");
    }

    @Bean
    public FlatFileItemReader<UserTransaction> userTransactionReader() {
        return new FlatFileItemReaderBuilder<UserTransaction>()
                .name("userTransactionReader")
                .resource(fileResource())
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer("|") {{
                        setNames("ACCOUNT_NUMBER", "TRX_AMOUNT", "DESCRIPTION", "TRX_DATE", "TRX_TIME", "CUSTOMER_ID");
                    }});
                    setFieldSetMapper(new TransactionFieldSetMapper());
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<UserTransaction> userTransactionWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<UserTransaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step userTransactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, EntityManagerFactory entityManagerFactory) {
        return new StepBuilder("userTransactionStep", jobRepository)
                .<UserTransaction, UserTransaction>chunk(10, transactionManager)
                .reader(userTransactionReader())
                .writer(userTransactionWriter(entityManagerFactory))
                .build();
    }

    @Bean
    public Job importUserTransactionJob(JobRepository jobRepository, Step userTransactionStep) {
        return new JobBuilder("importUserTransactionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(userTransactionStep)
                .build();
    }
}
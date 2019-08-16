package com.labs.beanio.xml.withoutintegration;

import com.labs.beanio.xml.domain.Register;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfigurationDelimitedToFixedLengthWithoutIntegration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationDelimitedToFixedLengthWithoutIntegration(JobBuilderFactory jobBuilderFactory,
                                                                      StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    /* Job configuration */

    @Bean
    public Job jobDelimitedToFixedLengthWithoutIntegration(Step stepDelimitedToFixedLengthWithoutIntegration) {
        return jobBuilderFactory
                .get("jobDelimitedToFixedLengthWithoutIntegration")
                .start(stepDelimitedToFixedLengthWithoutIntegration)
                .build();
    }

    /* Step configuration */

    @Bean
    public Step stepDelimitedToFixedLengthWithoutIntegration(@Qualifier("itemReaderDelimitedNoIntegration") ItemReader<Register> delimitedReader,
                                                             @Qualifier("itemWriterFixedLengthNoIntegration") ItemWriter<Register> fixedLengthWriter) {
        return stepBuilderFactory
                .get("stepDelimitedToFixedLengthWithoutIntegration")
                .<Register, Register>chunk(1)
                .reader(delimitedReader)
                .writer(fixedLengthWriter)
                .build();
    }

}
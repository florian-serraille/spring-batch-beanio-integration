package com.labs.beanio.xml.withoutintegration;

import com.labs.beanio.xml.domain.Register;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.spring.BeanIOFlatFileItemReader;
import org.beanio.spring.BeanIOFlatFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

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
    public Step stepDelimitedToFixedLengthWithoutIntegration(@Qualifier("beanIODelimitedReaderWithoutIntegration") ItemReader<Register> delimitedReader,
                                                        @Qualifier("beanIOFixedLengthWriterWithoutIntegration") ItemWriter<Register> fixedLengthWriter) {
        return stepBuilderFactory
                .get("stepDelimitedToFixedLengthWithoutIntegration")
                .<Register, Register>chunk(1)
                .reader(delimitedReader)
                .writer(fixedLengthWriter)
                .build();
    }

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIODelimitedReaderWithoutIntegration(
            @Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory,
            @Value("${input.delimited}") String inputFile) throws Exception {


        BeanReader in = streamFactory.createReader("employeeFileDelimited", new File(inputFile));

        return () -> {
            Register register;
            while ((register = (Register) in.read()) != null) {
                return register;
            }
            in.close();
            return null;
        };
    }

    @Bean
    public ItemWriter<Register> beanIOFixedLengthWriterWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory) throws Exception {

        return (items) -> {
            BeanWriter out = streamFactory.createWriter("employeeFileFixedLength", new File("output/outputByFileMapping.fl"));
            items.forEach(out::write);
            out.flush();
            out.close();
        };
    }
}
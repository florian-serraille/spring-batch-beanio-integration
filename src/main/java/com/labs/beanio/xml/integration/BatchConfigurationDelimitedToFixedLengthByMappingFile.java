package com.labs.beanio.xml.integration;

import com.labs.beanio.xml.domain.Register;
import org.beanio.StreamFactory;
import org.beanio.spring.BeanIOFlatFileItemReader;
import org.beanio.spring.BeanIOFlatFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchConfigurationDelimitedToFixedLengthByMappingFile {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationDelimitedToFixedLengthByMappingFile(JobBuilderFactory jobBuilderFactory,
                                                                 StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobDelimitedToFixedLengthByMappingFile(Step stepDelimitedToFixedLengthByMappingFile) {
        return jobBuilderFactory
                .get("jobDelimitedToFixedLengthByMappingFile")
                .incrementer(new RunIdIncrementer())
                .start(stepDelimitedToFixedLengthByMappingFile)
                .build();
    }

    /* Step configuration */
    @Bean
    public Step stepDelimitedToFixedLengthByMappingFile(@Qualifier("beanIODelimitedReaderByMappingFile") ItemReader<Register> delimitedReader,
                                                        @Qualifier("beanIOFixedLengthWriterByMappingFile") ItemWriter<Register> fixedLengthWriter) {
        return stepBuilderFactory
                .get("stepDelimitedToFixedLengthByMappingFile")
                .<Register, Register>chunk(1)
                .reader(delimitedReader)
                .writer(fixedLengthWriter)
                .build();
    }

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIODelimitedReaderByMappingFile(
            @Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
            @Value("${input.delimited}") String inputFile) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource(inputFile));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileDelimited");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOFixedLengthWriterByMappingFile(
            @Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
            @Value("${output.fixedlength.xmlmapping}") String outputFile) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource(outputFile));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileFixedLength");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }
}
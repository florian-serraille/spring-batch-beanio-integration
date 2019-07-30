package com.labs.beanio.xml;

import com.labs.beanio.xml.domain.Register;
import org.beanio.StreamFactory;
import org.beanio.spring.BeanIOFlatFileItemReader;
import org.beanio.spring.BeanIOFlatFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobXMLToCSV(Step stepXMLtoCSV) {
        return jobBuilderFactory
                .get("jobXMLtoCSV")
                .start(stepXMLtoCSV)
                .build();
    }

    @Bean
    public Job jobDelimitedToFixedLenght(Step stepDelimitedToFixedLenght) {
        return jobBuilderFactory
                .get("jobDelimitedToFixedLenght")
                .start(stepDelimitedToFixedLenght)
                .build();
    }

    /* Step configuration */

    @Bean
    public Step stepXMLtoCSV(@Qualifier("beanIOXMLReader") ItemReader<Register> xmlReader,
                             @Qualifier("beanIOCSVWriter") ItemWriter<Register> csvWriter) {
        return stepBuilderFactory
                .get("stepXMLtoCSV")
                .<Register, Register>chunk(1)
                .reader(xmlReader)
                .writer(csvWriter)
                .build();
    }

    @Bean
    public Step stepDelimitedToFixedLenght(@Qualifier("beanIODelimitedReader") ItemReader<Register> delimitedReader,
                                           @Qualifier("beanIOFixedLengthWriter") ItemWriter<Register> fixedLenghtWriter) {
        return stepBuilderFactory
                .get("stepDelimitedToFixedLenght")
                .<Register, Register>chunk(1)
                .reader(delimitedReader)
                .writer(fixedLenghtWriter)
                .build();
    }

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIOXMLReader(StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.xml"));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileXML");
        beanIOFlatFileItemReader.open(new ExecutionContext());
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOCSVWriter(StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/output.csv"));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileCSV");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }


    @Bean
    public ItemReader<Register> beanIODelimitedReader(StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.delimited"));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileDelimited");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOFixedLengthWriter(StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/output.fl"));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileFixedLength");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }

    /* BeanIO requirement */

    @Bean
    public StreamFactory getStreamFactory() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("mapping.xml");
        StreamFactory streamFactory = StreamFactory.newInstance();
        streamFactory.load(is);

        return streamFactory;
    }
}
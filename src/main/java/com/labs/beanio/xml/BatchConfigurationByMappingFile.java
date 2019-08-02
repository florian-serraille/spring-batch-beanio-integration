package com.labs.beanio.xml;

import com.labs.beanio.xml.domain.Register;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BatchConfigurationByMappingFile {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationByMappingFile(JobBuilderFactory jobBuilderFactory,
                                           StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobXMLToCSVByMappingFile(Step stepXMLtoCSVByMappingFile) {
        return jobBuilderFactory
                .get("jobXMLtoCSVByMappingFile")
                .start(stepXMLtoCSVByMappingFile)
                .build();
    }

    @Bean
    public Job jobDelimitedToFixedLengthByMappingFile(Step stepDelimitedToFixedLengthByMappingFile) {
        return jobBuilderFactory
                .get("jobDelimitedToFixedLengthByMappingFile")
                .start(stepDelimitedToFixedLengthByMappingFile)
                .build();
    }

    /* Step configuration */

    @Bean
    public Step stepXMLtoCSVByMappingFile(@Qualifier("beanIOXMLReaderByMappingFile") ItemReader<Register> xmlReader,
                                          @Qualifier("beanIOCSVWriterByMappingFile") ItemWriter<Register> csvWriter) {
        return stepBuilderFactory
                .get("stepXMLtoCSV")
                .<Register, Register>chunk(1)
                .reader(xmlReader)
                .writer(csvWriter)
                .build();
    }

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
    public ItemReader<Register> beanIOXMLReaderByMappingFile(@Qualifier("streamFactoryByMappingFile") StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.xml"));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileXML");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOCSVWriterByMappingFile(@Qualifier("streamFactoryByMappingFile") StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/outputByFileMapping.csv"));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileCSV");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }


    @Bean
    public ItemReader<Register> beanIODelimitedReaderByMappingFile(@Qualifier("streamFactoryByMappingFile") StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.delimited"));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileDelimited");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOFixedLengthWriterByMappingFile(@Qualifier("streamFactoryByMappingFile") StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/outputByFileMapping.fl"));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileFixedLength");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }

    /* BeanIO requirement */

    @Bean
    public StreamFactory streamFactoryByMappingFile() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("mapping.xml");
        StreamFactory streamFactory = StreamFactory.newInstance();
        streamFactory.load(is);

        return streamFactory;
    }
}
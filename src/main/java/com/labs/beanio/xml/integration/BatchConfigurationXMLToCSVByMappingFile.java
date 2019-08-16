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
public class BatchConfigurationXMLToCSVByMappingFile {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationXMLToCSVByMappingFile(JobBuilderFactory jobBuilderFactory,
                                                   StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobXMLtoCSVByMappingFile(Step stepXMLtoCSVByMappingFile) {
        return jobBuilderFactory
                .get("jobXMLtoCSVByMappingFile")
                .incrementer(new RunIdIncrementer())
                .start(stepXMLtoCSVByMappingFile)
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

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIOXMLReaderByMappingFile(
            @Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
            @Value("${input.delimited}") String inputFile) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource(inputFile));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileXML");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOCSVWriterByMappingFile(
            @Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
            @Value("${output.csv.xmlmapping}") String outputFile) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/outputByFileMapping.csv"));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileCSV");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }
}
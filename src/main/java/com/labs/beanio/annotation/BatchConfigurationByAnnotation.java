package com.labs.beanio.annotation;

import com.labs.beanio.annotation.domain.Employee;
import com.labs.beanio.annotation.domain.Header;
import com.labs.beanio.annotation.domain.Register;
import com.labs.beanio.annotation.domain.Trailer;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.StreamBuilder;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchConfigurationByAnnotation {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationByAnnotation(JobBuilderFactory jobBuilderFactory,
                                          StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobDelimitedToCSVByAnnotation(Step stepDelimitedToCSVByAnnotation) {
        return jobBuilderFactory
                .get("jobDelimitedToCSVByAnnotation")
                .incrementer(new RunIdIncrementer())
                .start(stepDelimitedToCSVByAnnotation)
                .build();
    }

    /* Step configuration */

    @Bean
    public Step stepDelimitedToCSVByAnnotation(@Qualifier("beanIODelimitedReaderByAnnotation") ItemReader<Register> delimitedReader,
                                               @Qualifier("beanIOCSVWriterByAnnotation") ItemWriter<Register> csvWriter) {
        return stepBuilderFactory
                .get("stepDelimitedToFixedLengthByAnnotation")
                .<Register, Register>chunk(1)
                .reader(delimitedReader)
                .writer(csvWriter)
                .build();
    }

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIODelimitedReaderByAnnotation(@Qualifier("streamFactoryByAnnotation") StreamFactory streamFactory,
                                                                  @Value("${input.annotation}") String inputFile, ApplicationContext applicationContext) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource(inputFile));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileDelimited");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOCSVWriterByAnnotation(@Qualifier("streamFactoryByAnnotation") StreamFactory streamFactory,
                                                            @Value("${output.annotation}") String outputFile) throws Exception {

        BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();
        beanIOFlatFileItemWriter.setResource(new FileSystemResource(outputFile));
        beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
        beanIOFlatFileItemWriter.setStreamName("employeeFileCSV");
        beanIOFlatFileItemWriter.setTransactional(false);
        beanIOFlatFileItemWriter.afterPropertiesSet();

        return beanIOFlatFileItemWriter;
    }

    /* BeanIO requirement */

    @Bean
    public StreamFactory streamFactoryByAnnotation() {


        StreamFactory streamFactory = StreamFactory.newInstance();

        StreamBuilder streamBuilderDelimited = new StreamBuilder("employeeFileDelimited")
                .format("delimited")
                .parser(new DelimitedParserBuilder().delimiter('|'))
                .addRecord(Header.class)
                .addRecord(Employee.class)
                .addRecord(Trailer.class);

        StreamBuilder streamBuilderCSV = new StreamBuilder("employeeFileCSV")
                .format("csv")
                .parser(new CsvParserBuilder())
                .addRecord(Header.class)
                .addRecord(Employee.class)
                .addRecord(Trailer.class);

        streamFactory.define(streamBuilderDelimited);
        streamFactory.define(streamBuilderCSV);

        return streamFactory;

    }
}
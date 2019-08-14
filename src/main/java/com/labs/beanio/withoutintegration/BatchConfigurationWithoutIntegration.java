package com.labs.beanio.withoutintegration;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BatchConfigurationWithoutIntegration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationWithoutIntegration(JobBuilderFactory jobBuilderFactory,
                                                StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    /* Job configuration */

    @Bean
    public Job jobXMLToCSVWithoutIntegration(Step stepXMLtoCSVWithoutIntegration) {
        return jobBuilderFactory
                .get("jobXMLtoCSVWithoutIntegration")
                .start(stepXMLtoCSVWithoutIntegration)
                .build();
    }

    @Bean
    public Job jobDelimitedToFixedLengthWithoutIntegration(Step stepDelimitedToFixedLengthWithoutIntegration) {
        return jobBuilderFactory
                .get("jobDelimitedToFixedLengthWithoutIntegration")
                .start(stepDelimitedToFixedLengthWithoutIntegration)
                .build();
    }

    /* Step configuration */

    @Bean
    public Step stepXMLtoCSVWithoutIntegration(@Qualifier("beanIOXMLReaderWithoutIntegration") ItemReader<Register> xmlReader,
                                          @Qualifier("beanIOCSVWriterWithoutIntegration") ItemWriter<Register> csvWriter) {
        return stepBuilderFactory
                .get("stepXMLtoCSVWithoutIntegration")
                .<Register, Register>chunk(500)
                .reader(xmlReader)
                .writer(csvWriter)
                .build();
    }

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
    public ItemReader<Register> beanIOXMLReaderWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory) throws Exception {

        BeanReader in = streamFactory.createReader("employeeFileXML", new File("input/input.xml"));

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
    public ItemWriter<Register> beanIOCSVWriterWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory) throws Exception {

        BeanWriter out = streamFactory.createWriter("employeeFileCSV", new File("output/outputWithoutIntegration.csv"));

        return (items) -> {
            items.forEach(out::write);
            out.flush();
            out.close();
        };
    }


    @Bean
    public ItemReader<Register> beanIODelimitedReaderWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory) throws Exception {

        BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();
        beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.delimited"));
        beanIOFlatFileItemReader.setStreamFactory(streamFactory);
        beanIOFlatFileItemReader.setStreamName("employeeFileDelimited");
        beanIOFlatFileItemReader.afterPropertiesSet();

        return beanIOFlatFileItemReader;
    }

    @Bean
    public ItemWriter<Register> beanIOFixedLengthWriterWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory) throws Exception {

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
    public StreamFactory streamFactoryWithoutIntegration() throws IOException {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("mapping.xml");
        StreamFactory streamFactory = StreamFactory.newInstance();
        streamFactory.load(is);

        return streamFactory;
    }
}
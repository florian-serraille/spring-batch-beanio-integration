package com.labs.beanio.xml.withoutintegration;

import com.labs.beanio.xml.domain.Register;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
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

import java.io.File;

@Configuration
public class BatchConfigurationXMLToCSVWithoutIntegration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfigurationXMLToCSVWithoutIntegration(JobBuilderFactory jobBuilderFactory,
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

    /* Item configuration */

    @Bean
    public ItemReader<Register> beanIOXMLReaderWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory,
                                                                  @Value("${input.xml}") String inputFile) {

        BeanReader in = streamFactory.createReader("employeeFileXML", new File(inputFile));

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
    public ItemWriter<Register> beanIOCSVWriterWithoutIntegration(@Qualifier("streamFactoryWithoutIntegration") StreamFactory streamFactory,
                                                                  @Value("${output.no-integration}") String outputFile) {

        return (items) -> {
            BeanWriter out = streamFactory.createWriter("employeeFileCSV", new File(outputFile));
            items.forEach(out::write);
            out.flush();
            out.close();
        };
    }
}
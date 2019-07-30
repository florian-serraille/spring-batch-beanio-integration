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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

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

	@Bean
	public Job jobXMLtoCSV(Step step) {
		return jobBuilderFactory
				.get("jobXMLtoCSV")
				.start(step)
				.build();
	}

	@Bean
	public Step stepXMLtoCSV(ItemReader<Register> xmlReader,
							 ItemWriter<Register> xmlWriter) {
		return stepBuilderFactory
				.get("stepXMLtoCSV")
				.<Register, Register>chunk(1)
				.reader(xmlReader)
				.writer(xmlWriter)
				.build();
	}

	@Bean
	public  ItemReader<Register> beanIOXMLReader() throws Exception {
		BeanIOFlatFileItemReader<Register> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader<>();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("mapping.xml");

		StreamFactory streamFactory = StreamFactory.newInstance();
		streamFactory.load(is);

		beanIOFlatFileItemReader.setResource(new FileSystemResource("input/input.xml"));
		beanIOFlatFileItemReader.setStreamFactory(streamFactory);
		beanIOFlatFileItemReader.setStreamName("employeeFileXML");
		beanIOFlatFileItemReader.open(new ExecutionContext());
		beanIOFlatFileItemReader.afterPropertiesSet();

		return beanIOFlatFileItemReader;
	}

	@Bean
	public  ItemWriter<Register> beanIOCSVWriter() throws Exception {

		BeanIOFlatFileItemWriter<Register> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter<>();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("mapping.xml");

		StreamFactory streamFactory = StreamFactory.newInstance();
		streamFactory.load(is);

		beanIOFlatFileItemWriter.setResource(new FileSystemResource("output/output.csv"));
		beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
		beanIOFlatFileItemWriter.setStreamName("employeeFileCSV");
		beanIOFlatFileItemWriter.setTransactional(false);
		beanIOFlatFileItemWriter.afterPropertiesSet();

		return beanIOFlatFileItemWriter;
	}
}
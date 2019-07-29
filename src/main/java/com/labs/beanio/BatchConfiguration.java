package com.labs.beanio;

import org.beanio.StreamFactory;
import org.beanio.spring.BeanIOFlatFileItemReader;
import org.beanio.spring.BeanIOFlatFileItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.InputStream;
import java.io.OutputStream;

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
	public Job job(Step step) {
		return jobBuilderFactory
				.get("job")
				.start(step)
				.build();
	}

	@Bean
	public Step step(ItemReader<String> reader,
	                         ItemWriter<String> writer) {
		return stepBuilderFactory
				.get("step")
				.<String, String>chunk(1)
				.reader(reader)
				.writer(writer)
				.build();
	}

	@Bean
	public  ItemReader<String> beanIOReader() throws Exception {
		BeanIOFlatFileItemReader<String> beanIOFlatFileItemReader = new BeanIOFlatFileItemReader();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("mapping.xml");

		StreamFactory streamFactory = StreamFactory.newInstance();
		streamFactory.load(is);

		beanIOFlatFileItemReader.setResource(new FileSystemResource("input.xml"));
		beanIOFlatFileItemReader.setStreamFactory(streamFactory);
		beanIOFlatFileItemReader.setStreamName("employeeFileXML");
		beanIOFlatFileItemReader.afterPropertiesSet();

		return beanIOFlatFileItemReader;
	}

	@Bean
	public  ItemWriter<String> beanIOWriter() throws Exception {

		BeanIOFlatFileItemWriter<String> beanIOFlatFileItemWriter = new BeanIOFlatFileItemWriter();

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("mapping.xml");

		StreamFactory streamFactory = StreamFactory.newInstance();
		streamFactory.load(is);

		beanIOFlatFileItemWriter.setResource(new FileSystemResource("output.csv"));
		beanIOFlatFileItemWriter.setStreamFactory(streamFactory);
		beanIOFlatFileItemWriter.setStreamName("employeeFileXML");
		beanIOFlatFileItemWriter.afterPropertiesSet();

		return beanIOFlatFileItemWriter;
	}
}
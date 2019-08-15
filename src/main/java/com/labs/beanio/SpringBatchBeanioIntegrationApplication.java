package com.labs.beanio;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchBeanioIntegrationApplication implements CommandLineRunner {

	private final ApplicationContext applicationContext;
	private final JobLauncher jobLauncher;

	public SpringBatchBeanioIntegrationApplication(ApplicationContext applicationContext, JobLauncher jobLauncher) {
		this.applicationContext = applicationContext;
		this.jobLauncher = jobLauncher;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchBeanioIntegrationApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Stream.of(args)
				.map(s -> applicationContext.getBean(s, Job.class))
				.forEach(job -> {
					try {
						jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});


	}
}

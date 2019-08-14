package com.labs.beanio;

import com.labs.beanio.annotation.BatchConfigurationByAnnotation;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import(BatchConfigurationByAnnotation.class)
@EnableBatchProcessing
@PropertySource("classpath:application-test.properties")
public class BatchTestConfiguration {

	@Bean
	public JobLauncherTestUtils getJobLauncherTestUtils(){
		return new JobLauncherTestUtils();
	}

}

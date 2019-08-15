package com.labs.beanio.xml;

import org.beanio.StreamFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BeanIOXMLConfiguration {
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

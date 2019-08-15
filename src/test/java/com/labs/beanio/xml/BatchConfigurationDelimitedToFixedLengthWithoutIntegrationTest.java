package com.labs.beanio.xml;

import com.labs.beanio.BatchTestConfiguration;
import com.labs.beanio.xml.withoutintegration.BatchConfigurationDelimitedToFixedLengthWithoutIntegration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import({BatchConfigurationDelimitedToFixedLengthWithoutIntegration.class,
        BeanIOXMLConfiguration.class})
@ContextConfiguration(classes = {BatchTestConfiguration.class})
public class BatchConfigurationDelimitedToFixedLengthWithoutIntegrationTest {

    @Value("${output.no-integration}")
    private String OUTPUT_GENERATED_FILE;

    @Value("${output.expected.no-integration}")
    private String EXPECTED_FILE;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void testInjections() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }

    @Test
    public void testJob() throws Exception {
        // Given this expected output file
        FileSystemResource EXPECTED_OUTPUT = new FileSystemResource(EXPECTED_FILE);

        // When execute job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        // Test exist status
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        // Test output generated file (compare only the line number of both files)
        AssertFile.assertFileEquals(EXPECTED_OUTPUT, new FileSystemResource(OUTPUT_GENERATED_FILE));
    }

    @After
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(OUTPUT_GENERATED_FILE));
    }

}
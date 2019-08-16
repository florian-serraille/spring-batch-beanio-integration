package com.labs.beanio.xml.withoutintegration;

import com.labs.beanio.xml.domain.Register;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("itemReaderDelimitedNoIntegration")
public class ItemReaderDelimitedNoIntegration extends ItemStreamSupport implements ItemReader<Register> {

    private final String inputFile;
    private final StreamFactory streamFactory;

    private BeanReader reader;

    public ItemReaderDelimitedNoIntegration(@Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
                                            @Value("${input.delimited}") String inputFile) {
        this.streamFactory = streamFactory;
        this.inputFile = inputFile;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        reader = streamFactory.createReader("employeeFileDelimited", new File(inputFile));
    }

    @Override
    public Register read() {
        Register register;
        while ((register = (Register) reader.read()) != null) {
            return register;
        }
        return null;
    }

    @Override
    public void close() {
        reader.close();
    }
}

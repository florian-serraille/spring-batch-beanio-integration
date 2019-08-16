package com.labs.beanio.xml.withoutintegration;

import com.labs.beanio.xml.domain.Register;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component("itemWriterFixedLengthNoIntegration")
public class ItemWriterFixedLengthNoIntegration extends ItemStreamSupport implements ItemWriter<Register> {

    private final String outputFile;
    private final StreamFactory streamFactory;

    private BeanWriter writer;

    public ItemWriterFixedLengthNoIntegration(@Qualifier("streamFactoryByXMLMapping") StreamFactory streamFactory,
                                              @Value("${output.fixedlength.no-integration}") String outputFile) {
        this.streamFactory = streamFactory;
        this.outputFile = outputFile;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        writer = streamFactory.createWriter("employeeFileFixedLength", new File(outputFile));
    }

    @Override
    public void write(List<? extends Register> items) {
        items.forEach(writer::write);
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }

}

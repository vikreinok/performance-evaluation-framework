package ee.ttu.thesis;

import ee.ttu.thesis.model.Source;
import ee.ttu.thesis.processor.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Analyzer {


    protected List<Processor> processors = new ArrayList<Processor>();

    public void process(List<Source> data) {

        for (Processor processor : processors) {
            processor.process(data);
        }
    }

    public void addProcessor(Processor... processors) {
        for (Processor processor : processors) {
            this.processors.add(processor);
        }
    }
}

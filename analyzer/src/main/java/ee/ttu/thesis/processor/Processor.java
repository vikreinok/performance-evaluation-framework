package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.Source;

import java.util.List;

/**
 *
 */
public interface Processor {

    void process(List<Source> data);
}

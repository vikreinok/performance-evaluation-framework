package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.processor.Result;
import ee.ttu.thesis.model.stagemonitor.Source;

import java.util.List;

/**
 *
 */
public interface Processor {

    public enum ProcessorType {
        CALLING_CONTEXT_TREE_DEPTH,
        CALLING_CONTEXT_TREE_SIZE,
        DB_EXECUTION_TIME,
        DB_QUERY_COUNT,
        EXECUTION_TIME,
    }

    void process(List<Source> data);

    Result getResult();
}

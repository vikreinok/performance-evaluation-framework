package ee.ttu.thesis;

import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.processor.Processor;
import ee.ttu.thesis.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ee.ttu.thesis.util.Logger.log;

/**
 *
 */
public class Analyzer {


    protected Set<Processor> processors = new HashSet<Processor>();

    public void process(List<Source> data) {

        log(String.format("Processing request type: %s", getHttpRequestInfo(data)));

        for (Processor processor : processors) {
            processor.process(data);
        }
    }

    public void addProcessor(Processor... processors) {
        for (Processor processor : processors) {
            // TODO Add equal and hashcode by type
            if (this.processors.contains(processor)) {
                Logger.log("Already exists");
            }
            this.processors.add(processor);
        }
    }

    /**
     * As the Each datapoint is a set of data for specific HTTP request type...
     *
     * @param data
     * @return
     */
    private String getHttpRequestInfo(List<Source> data) {
        Source firstSource = data.get(0);
        return String.format("URL %-80s request-id=%-6s", firstSource.getUrl(), firstSource.getHeaders().getRequestId());
    }
}

package ee.ttu.thesis;

import ee.ttu.thesis.model.processor.Result;
import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.processor.Processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ee.ttu.thesis.util.Logger.log;
import static ee.ttu.thesis.util.Logger.logErr;

/**
 *
 */
public class Analyzer {

    public static final double DIFF_AVERAGE_THRESHOLD = 0.01;

    protected Set<Processor> processors = new HashSet<Processor>();

    public void processMetrics(List<Source> data) {

        log(String.format("Processing HTTP request metrics. Data set size %5d. HTTP request: %s", data.size(), getHttpRequestInfo(data)));


        // Adds processed information to Result model
        for (Processor processor : processors) {
            processor.process(data);
        }

        // Output warnings
        for (Processor processor : processors) {
            Result result = processor.getResult();

            if (result.getDiffAverage() > DIFF_AVERAGE_THRESHOLD) {
                logErr(String.format("Issue -> %-58s %s", result.getMetricName(), getHttpRequestInfo(data)));
            }
        }


    }


    public void addProcessor(Processor... processors) {
        for (Processor processor : processors) {
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
        return String.format("Request-id=%-6s %-4s %-80s",  firstSource.getHeaders().getRequestId(), firstSource.getMethod(), firstSource.getUrl());
    }





}

package ee.ttu.thesis;

import ee.ttu.thesis.model.processor.Result;
import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.processor.Processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ee.ttu.thesis.util.Logger.log;
import static ee.ttu.thesis.util.Logger.logErr;

/**
 *
 */
public class Analyzer {

    public static final double DIFF_AVERAGE_THRESHOLD = 0.1;

    protected Set<Processor> processors = new HashSet<Processor>();

    public List<Result> processMetrics(List<Source> data) {

        log(String.format("Processing HTTP request metrics. Data set size %5d. HTTP request: %s", data.size(), getHttpRequestInfo(data)));

        // Adds processed information to Result model
        for (Processor processor : processors) {
            processor.process(data);
        }

        List<Result> results = new ArrayList<Result>(processors.size());
        // Output warnings
        for (Processor processor : processors) {
            Result result = processor.getResult();


            // Strongest marker of a performance issue
            if (result.getDiffAverage() > DIFF_AVERAGE_THRESHOLD) {
                logErr(String.format("Issue diffAverage -> %-30s value %9.2f %s", result.getMetricName(), result.getDiffAverage(), getHttpRequestInfo(data)));
            }
            results.add(result);
        }
        return results;
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
        return String.format("Request-id=%-6s %-4s %-80s", firstSource.getHeaders().getRequestId(), firstSource.getMethod(), firstSource.getUrl());
    }

    public void compare(List<Result> previousResults, List<Result> currentResults) {

        if (previousResults == null) {
            return;
        }

        for (int index = 0; index < previousResults.size(); index++) {
            Result previous = previousResults.get(index);

            for (Result current : currentResults) {
                if (previous.getMetricName().equals(current.getMetricName())) {

//                    previous.print();
//                    current.print();
                    previous.compare(current);
                }
            }


        }

    }

    private boolean specificType(Result previous, Processor.ProcessorType type) {
        return previous.getMetricName().equals(type.name());
    }
}

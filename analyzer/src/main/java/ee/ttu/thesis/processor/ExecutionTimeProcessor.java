package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.Source;

import java.util.List;

/**
 *
 */
public class ExecutionTimeProcessor extends AbstractProcessor {


    public void process(List<Source> data) {

        size = data.size();
        for (int index = 0; index < size; index++) {
            Source current = index != size ? data.get(index) : null;
            Source previous = index > 0 ? data.get(index - 1) : current;

            if (previous != null) {
                long diff = current.getExecutionTime() - previous.getExecutionTime();
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (current.getExecutionTime() > max) {
                max = current.getExecutionTime();
            }

            if (current.getExecutionTime() < min) {
                min = current.getExecutionTime();
            }

            sum += current.getExecutionTime();
            values.add(Double.valueOf(current.getExecutionTime()));

            log();
        }


        analyzeMetric();
    }

    @Override
    String getUnit() {
        return "ms";
    }

    @Override
    String getMetricName() {
        return "ExecutionTime";
    }
}

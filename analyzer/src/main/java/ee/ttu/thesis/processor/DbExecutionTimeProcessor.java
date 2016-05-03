package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.stagemonitor.Source;

import java.util.List;

/**
 *
 */
public class DbExecutionTimeProcessor extends AbstractProcessor {


    public void process(List<Source> data) {

        size = data.size();
        for (int index = 0; index < size; index++) {
            Source current = index != size ? data.get(index) : null;
            Source previous = index > 0 ? data.get(index - 1) : current;

            Long currentMetric = current.getExecutionTimeDb();
            if (previous != null) {
                Long previousMetric = previous.getExecutionTimeDb();
                long diff = currentMetric - previousMetric;
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (currentMetric > max) {
                max = currentMetric;
            }

            if (currentMetric < min) {
                min = currentMetric;
            }

            sum += currentMetric;
            values.add(Double.valueOf(currentMetric));

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
        return "DbExecutionTime";
    }
}

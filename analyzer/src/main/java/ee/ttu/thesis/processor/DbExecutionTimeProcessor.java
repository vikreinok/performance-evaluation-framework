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

            if (previous != null) {
                long diff = current.getExecutionTimeDb() - previous.getExecutionTimeDb();
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (current.getExecutionTimeDb() > max) {
                max = current.getExecutionTimeDb();
            }

            if (current.getExecutionTimeDb() < min) {
                min = current.getExecutionTimeDb();
            }

            sum += current.getExecutionTimeDb();
            values.add(Double.valueOf(current.getExecutionTimeDb()));

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

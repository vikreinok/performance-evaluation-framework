package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.stagemonitor.Source;

import java.util.List;

/**
 *
 */
public class DbQueryCountProcessor extends AbstractProcessor {


    public void process(List<Source> data) {

        size = data.size();
        for (int index = 0; index < size; index++) {
            Source current = index != size ? data.get(index) : null;
            Source previous = index > 0 ? data.get(index - 1) : current;

            if (previous != null) {
                long diff = current.getExecutionCountDb() - previous.getExecutionCountDb();
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (current.getExecutionCountDb() > max) {
                max = current.getExecutionCountDb();
            }

            if (current.getExecutionCountDb() < min) {
                min = current.getExecutionCountDb();
            }

            sum += current.getExecutionCountDb();
            values.add(Double.valueOf(current.getExecutionCountDb()));

            log();
        }


        analyzeMetric();
    }

    @Override
    String getUnit() {
        return "";
    }

    @Override
    String getMetricName() {
        return "DbQueryCount";
    }
}

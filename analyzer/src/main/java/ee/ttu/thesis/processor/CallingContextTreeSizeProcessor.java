package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.stagemonitor.Source;

import java.util.List;

/**
 *
 */
public class CallingContextTreeSizeProcessor extends AbstractProcessor {


    public void process(List<Source> data) {

        size = data.size();
        for (int index = 0; index < size; index++) {
            Source current = index != size ? data.get(index) : null;
            Source previous = index > 0 ? data.get(index - 1) : current;

            if (previous != null) {
                long diff = current.getCallStack().length() - previous.getCallStack().length();
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (current.getCallStack().length() > max) {
                max = current.getCallStack().length();
            }

            if (current.getCallStack().length() < min) {
                min = current.getCallStack().length();
            }

            sum += current.getCallStack().length();
            values.add(Double.valueOf(current.getCallStack().length()));

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
        return "CallingContextTreeSize";
    }
}

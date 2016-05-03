package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.Source;

import java.util.List;

/**
 *
 */
public class CallingContextTreeDepthProcessor extends AbstractProcessor {


    public void process(List<Source> data) {

        size = data.size();
        for (int index = 0; index < size; index++) {
            Source current = index != size ? data.get(index) : null;
            Source previous = index > 0 ? data.get(index - 1) : current;

            if (previous != null) {
                long diff = current.getCallStack().replaceAll("[^\n]", "").length() - previous.getCallStack().replaceAll("[^\n]", "").length();
                diffSum += diff;
                diffSqrtSum += Math.sqrt(Math.abs(diff));
            }

            if (current.getCallStack().replaceAll("[^\n]", "").length() > max) {
                max = current.getCallStack().replaceAll("[^\n]", "").length();
            }

            if (current.getCallStack().replaceAll("[^\n]", "").length() < min) {
                min = current.getCallStack().replaceAll("[^\n]", "").length();
            }

            sum += current.getCallStack().replaceAll("[^\n]", "").length();
            values.add(Double.valueOf(current.getCallStack().replaceAll("[^\n]", "").length()));

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
        return "CallingContextTreeDepth";
    }
}

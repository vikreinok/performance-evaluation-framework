package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.stagemonitor.Source;

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

            long currentMetric = current.getCallStack().replaceAll("[^\n]", "").length();

            if (previous != null) {
                long previousMetric = previous.getCallStack().replaceAll("[^\n]", "").length();
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
        return "";
    }

    @Override
    ProcessorType getProcessorType() {
        return ProcessorType.CALLING_CONTEXT_TREE_DEPTH;
    }
}

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

            int currentMetric = current.getCallStack().length();
            if (previous != null) {
                int perviousMetric = previous.getCallStack().length();
                long diff = currentMetric - perviousMetric;
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
        return ProcessorType.CALLING_CONTEXT_TREE_SIZE;
    }
}

package ee.ttu.thesis.processor;

import ee.ttu.thesis.model.processor.Result;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class AbstractProcessor implements Processor {
    protected long sum = 0;
    protected long diffSum = 0;
    protected double diffSqrtSum = 0;
    protected int size = 0;
    protected long max = Long.MIN_VALUE;
    protected long min = Long.MAX_VALUE;
    protected List<Double> values = new ArrayList<Double>();

    protected Result result = null;

    protected void log() {
//        System.out.println(this.toString());
    }

    protected void log(String message) {
        System.out.println(message);
    }

    abstract String getUnit();

    abstract ProcessorType getProcessorType();

    protected void analyzeMetric() {
        double average = (double) sum / size;
        double median = median(values);
        double range = range(min, max);
        double max = (double) this.max;
        double min = (double) this.min;
        double diffAverage = (double) diffSum / size;
        double squareRootAverage = diffSqrtSum / size;
        double standardDeviation = stdDev(values);

        result = new Result(getProcessorType().name(), getUnit(), average, median, range, max, min, diffAverage, squareRootAverage, standardDeviation);
//        result.print();
    }

    public double stdDev(List<Double> values) {
        if (values == null || values.size() == 0)
            return 0.0;
        double sum = 0;
        double sq_sum = 0;
        for (int i = 0; i < values.size(); ++i) {
            sum += values.get(i);
            sq_sum += values.get(i) * values.get(i);
        }
        double mean = sum / values.size();
        double variance = sq_sum / values.size() - mean * mean;
        return Math.sqrt(variance);
    }

    public double median(List<Double> values) {
        int middle = values.size() / 2;
        if (values.size() % 2 == 1) {
            return values.get(middle);
        } else {
            return (values.get(middle - 1) + values.get(middle)) / 2.0;
        }
    }

    public double range(double min, double max) {
        return max - min;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer("AbstractProcessor{");
        sb.append("diffSum=").append(diffSum);
        sb.append(", diffSqrtSum=").append(diffSqrtSum);
        sb.append(", size=").append(size);
        sb.append(", max=").append(max);
        sb.append(", min=").append(min);
        sb.append('}');
        return sb.toString();
    }

    public Result getResult() {
        if (result == null) {
            throw new IllegalStateException("Method analyzeMetric not executed");
        }
        return result;
    }
}

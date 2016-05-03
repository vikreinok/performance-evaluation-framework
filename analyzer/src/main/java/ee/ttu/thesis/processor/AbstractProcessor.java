package ee.ttu.thesis.processor;

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

    protected void log() {
//        System.out.println(this.toString());
    }

    protected void log(String message) {
        System.out.println(message);
    }

    abstract String getUnit();

    abstract String getMetricName();

    protected void analyzeMetric() {
        double average = (double) sum / size;
        double median = median(values);
        double range = range(min, max);
        double max = (double) this.max;
        double min = (double) this.min;
        double diffAverage = (double) diffSum / size;
        double squareRootAverage = diffSqrtSum / size;
        double standardDeviation = stdDev(values);
        log(String.format("-----------%-30s-------------", getMetricName()));
        log(String.format("%-20s %10.2f %s", "average", average, getUnit()));
        log(String.format("%-20s %10.2f %s", "median", median, getUnit()));
        log(String.format("%-20s %10.2f %s", "range", range, getUnit()));
        log(String.format("%-20s %10.2f %s", "max", max, getUnit()));
        log(String.format("%-20s %10.2f %s", "min", min, getUnit()));
        log(String.format("%-20s %10.2f %s", "diffAverage", diffAverage, getUnit()));
        log(String.format("%-20s %10.2f" , "squareRootAverage", squareRootAverage));
        log(String.format("%-20s %10.2f", "standardDeviation", standardDeviation));
        log(String.format("------------------------------------------------------"));

    }

    public double stdDev(List<Double> values) {
        if(values == null || values.size() == 0)
            return 0.0;
        double sum = 0;
        double sq_sum = 0;
        for(int i = 0; i < values.size(); ++i) {
            sum += values.get(i);
            sq_sum += values.get(i) * values.get(i);
        }
        double mean = sum / values.size();
        double variance = sq_sum / values.size() - mean * mean;
        return Math.sqrt(variance);
    }

    public double median(List<Double> values) {
        int middle = values.size()/2;
        if (values.size()%2 == 1) {
            return values.get(middle);
        } else {
            return (values.get(middle-1) + values.get(middle)) / 2.0;
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
}

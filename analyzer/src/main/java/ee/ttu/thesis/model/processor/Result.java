package ee.ttu.thesis.model.processor;

import static ee.ttu.thesis.util.Logger.log;

/**
 *
 */
public class Result {

    public static final double THRESHOLD_PERCENTAGE = 100;
    private String metricName;
    private String unit;

    private double average;
    private double median;
    private double range;
    private double max;
    private double min;
    private double diffAverage;
    private double squareRootAverage;
    private double standardDeviation;


    public Result(String metricName, String unit, double average, double median, double range, double max, double min, double diffAverage, double squareRootAverage, double standardDeviation) {
        this(average, median, range, max, min, diffAverage, squareRootAverage, standardDeviation);
        this.metricName = metricName;
        this.unit = unit;
    }

    public Result(double average, double median, double range, double max, double min, double diffAverage, double squareRootAverage, double standardDeviation) {
        this.average = average;
        this.median = median;
        this.range = range;
        this.max = max;
        this.min = min;
        this.diffAverage = diffAverage;
        this.squareRootAverage = squareRootAverage;
        this.standardDeviation = standardDeviation;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getDiffAverage() {
        return diffAverage;
    }

    public void setDiffAverage(double diffAverage) {
        this.diffAverage = diffAverage;
    }

    public double getSquareRootAverage() {
        return squareRootAverage;
    }

    public void setSquareRootAverage(double squareRootAverage) {
        this.squareRootAverage = squareRootAverage;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void print() {
        log(String.format("-----------%-30s-------------", metricName));
        log(String.format("%-20s %10.2f %s", "average", average, unit));
        log(String.format("%-20s %10.2f %s", "median", median, unit));
        log(String.format("%-20s %10.2f %s", "range", range, unit));
        log(String.format("%-20s %10.2f %s", "max", max, unit));
        log(String.format("%-20s %10.2f %s", "min", min, unit));
        log(String.format("%-20s %10.2f %s", "diffAverage", diffAverage, unit));
        log(String.format("%-20s %10.2f", "squareRootAverage", squareRootAverage));
        log(String.format("%-20s %10.2f", "standardDeviation", standardDeviation));
        log(String.format("------------------------------------------------------"));
    }

    public void compare(Result result) {

        evaluate("average",this.average, result.getAverage());
        evaluate("median",this.median, result.getMedian());
        evaluate("range",this.range, result.getRange());
        evaluate("max",this.max, result.getMax());
        evaluate("min",this.min, result.getMin());
        evaluate("diffAverage",this.diffAverage, result.getDiffAverage());
        evaluate("squareRootAverage",this.squareRootAverage, result.getSquareRootAverage());
        evaluate("standardDeviation",this.standardDeviation, result.getStandardDeviation());


    }

    protected static double changePercentage(double oldValue, double newValue) {
        double diff = Math.abs(oldValue - newValue);
        double percentage =  diff /oldValue;
        if (Double.isInfinite(percentage)) {
            return 0;
        }
        return percentage;
    }

    protected void evaluate(String attribute, double oldValue, double newValue) {
        double percentage = changePercentage(oldValue, newValue);
        percentage *= 100;
        if (percentage > THRESHOLD_PERCENTAGE) {
            System.out.println(String.format("Metric %-30s %-18s differed from last by %8.03f%%", this.metricName ,attribute, percentage));
        }
    }
}

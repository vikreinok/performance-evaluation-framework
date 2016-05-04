package ee.ttu.thesis.model.processor;

import static ee.ttu.thesis.util.Logger.log;

/**
 *
 */
public class Result {

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
}

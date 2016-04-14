package com.takipi.samples.servprof.state;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsCollector {
    // Use LongAdder if Java 8
    private final Map<Object, AtomicLong> map;
    private MetricMultiplexer multiplexer;

    public MetricsCollector() {
        //metrics can be added dynamically so use a thread safe structure
        //each of the metric values can also be changed concurrently,
        //so use a AtomicLong to sum up the values safely
        this.map = new ConcurrentHashMap<>(1000);
    }

    public void adjust(Object metric, long amount) {
        adjust(metric, amount);
    }

    public void adjust(Object metric, long amount, Object data) {

//		DataFilter dataFilter = new DataFilter();
//		dataFilter.readData(data);

        // Add here value logger.

        //if we have a multiplexer = use it
        if (getMultiplexer() != null) {

            // give an optional multiplexer the ability to use dynamic state to
            // divide the metrics into sub categories
            Object[] multiplexedMetrics = getMultiplexer().multiplex(metric, data);

            for (Object multiplexedMetric : multiplexedMetrics) {
                doAdjust(multiplexedMetric, amount);
            }
        } else {
            doAdjust(metric, amount);
        }
    }

    private void doAdjust(Object metric, long amount) {

        AtomicLong curValue = map.get(metric);

        //new metric? => add to map
        if (curValue == null) {
            curValue = new AtomicLong();
            map.put(metric, curValue);
        }

        curValue.addAndGet(amount);
    }

    public Map<Object, Long> asMap() {
        Map<Object, Long> result = new HashMap<>();

        for (Map.Entry<Object, AtomicLong> entry : map.entrySet()) {
            // use the long adder's sumThenReset to safely get and reset the metric value
            result.put(entry.getKey(), entry.getValue().getAndSet(0));
        }

        return result;
    }

    public MetricMultiplexer getMultiplexer() {
        return multiplexer;
    }

    public void setMultiplexer(MetricMultiplexer multiplexer) {
        this.multiplexer = multiplexer;
    }
}

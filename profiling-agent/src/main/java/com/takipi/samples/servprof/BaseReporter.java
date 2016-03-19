package com.takipi.samples.servprof;

import com.takipi.samples.servprof.state.MetricsData;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class BaseReporter {

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			//run the reporter thread as daemon
			Thread thread = new Thread(runnable);
			thread.setName(name);
			thread.setDaemon(true);

			return thread;
		}
	});

	private final long reportIntervalMillis;
	private boolean printToConsole;
	final String name;

	public BaseReporter(String name, long reportIntervalMillis, boolean printToConsole) {
		this.reportIntervalMillis = reportIntervalMillis;
		this.setPrintToConsole(printToConsole);
		this.name = name;
	}

	public void start() {
		//start the reporter thread
		executor.scheduleAtFixedRate(new ReporterTask(), reportIntervalMillis, reportIntervalMillis,
				TimeUnit.MILLISECONDS);
	}

	public boolean isPrintToConsole() {
		return printToConsole;
	}

	public void setPrintToConsole(boolean printToConsole) {
		this.printToConsole = printToConsole;
	}

	protected class ReporterTask implements Runnable {
		@Override
		public void run() {
			reportMetrics(MetricsData.statsCollector.asMap());
		}
	}

	protected String parseMetric(String metricName, long value) {
		return String.format("Metric: %50s, total running time: %10d ms", metricName, value);
	}

	protected void reportMetrics(Map<Object, Long> metrics) {

		// iterate over the metrics and report them out
		for (Map.Entry<Object, Long> metric : metrics.entrySet()) {
			String name = metric.getKey().toString();
			long value = metric.getValue();

			if (isPrintToConsole() && value > 0) {
				String output = parseMetric(name, value);
				System.out.println(output);
			}

			reportMetric(name, value);
		}
		System.out.printf("------------------------------------------ size  %d -----------------------------------------------%n", metrics.size());
	}

	protected abstract void reportMetric(String name, long value);
}

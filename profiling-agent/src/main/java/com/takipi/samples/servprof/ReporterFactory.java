package com.takipi.samples.servprof;

import com.takipi.samples.servprof.state.LocalVarMultiplexer;
import com.takipi.samples.servprof.state.MetricsData;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ReporterFactory {
	public static BaseReporter createStatsdReporter(String name, long reportIntervalMillis) {

		MetricsData.statsCollector.setMultiplexer(new LocalVarMultiplexer("descr"));

		BaseReporter reporter = new StatsdReporter(name, reportIntervalMillis, true);

		return reporter;

	}

	public static BaseReporter createFileReporter(String name, String outputFileName, long reportIntervalMillis)
			throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(outputFileName);
		BaseReporter reporter = new FileReporter(pw, name, reportIntervalMillis, true);

		return reporter;

	}
}

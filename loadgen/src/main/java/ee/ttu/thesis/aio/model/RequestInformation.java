package ee.ttu.thesis.aio.model;

/**
 *
 */
public class RequestInformation {

    private String threadIdentifier = formatThreadIdentifier(0);
    private String periodNumber = formatPeriodNumber(0);

    public static String formatThreadIdentifier(int threadIdentifier) {
        return String.format("%03d", threadIdentifier);
    }

    public static String formatPeriodNumber(int periodNumber) {
        return String.format("%04d", periodNumber);
    }

    public String getThreadIdentifier() {
        return threadIdentifier;
    }

    public void setThreadIdentifier(String threadIdentifier) {
        this.threadIdentifier = threadIdentifier;
    }

    public void setThreadIdentifier(int threadIdentifier) {
        this.threadIdentifier = formatThreadIdentifier(threadIdentifier);
    }

    public String getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = formatPeriodNumber(periodNumber);
    }

    public String buildRequestIdentifier(String requestId) {
        return String.format("%s_%s_%s", getThreadIdentifier(), requestId, getPeriodNumber());
    }

}

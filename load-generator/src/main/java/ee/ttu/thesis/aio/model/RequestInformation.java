package ee.ttu.thesis.aio.model;

/**
 *
 */
public class RequestInformation {

    private String sessionId = formatSessionId(0);
    private String periodNumber = formatPeriodNumber(0);
    private String modificationId = formatModificationId(0);

    public static String formatSessionId(int sessionId) {
        return String.format("%03d", sessionId);
    }

    public static String formatPeriodNumber(int periodNumber) {
        return String.format("%04d", periodNumber);
    }

    public static String formatModificationId(int modificationId) {
        return String.format("%04d", modificationId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = formatSessionId(sessionId);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = formatPeriodNumber(periodNumber);
    }

    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getModificationId() {
        return modificationId;
    }

    public void setModificationId(String modificationId) {
        this.modificationId = modificationId;
    }

    public void setModificationId(int modificationId) {
        this.modificationId = formatModificationId(modificationId);
    }

    public String buildRequestName(String requestId) {
        return String.format("%s_%s_%s", getSessionId(), requestId, getPeriodNumber());
    }

}

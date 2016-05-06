
package ee.ttu.thesis.model.stagemonitor;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "content-length",
        "x-country",
        "x-brand",
        "host",
        "content-type",
        "connection",
        "x-language",
        "randomheader",
        "accept",
        "user-agent",
        "request-id",
        "request-name",
        "thread-id",
        "period-number"
})
public class Headers {

    @JsonProperty("content-length")
    private String contentLength;
    @JsonProperty("x-country")
    private String xCountry;
    @JsonProperty("x-brand")
    private String xBrand;
    @JsonProperty("host")
    private String host;
    @JsonProperty("content-type")
    private String contentType;
    @JsonProperty("connection")
    private String connection;
    @JsonProperty("x-language")
    private String xLanguage;
    @JsonProperty("randomheader")
    private String randomheader;
    @JsonProperty("accept")
    private String accept;
    @JsonProperty("user-agent")
    private String userAgent;
    @JsonProperty("request-id")
    private String requestId;
    @JsonProperty("request-name")
    private String requestName;
    @JsonProperty("session-id")
    private String sessionId;
    @JsonProperty("period-number")
    private String periodNumber;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The contentLength
     */
    @JsonProperty("content-length")
    public String getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength The content-length
     */
    @JsonProperty("content-length")
    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * @return The xCountry
     */
    @JsonProperty("x-country")
    public String getXCountry() {
        return xCountry;
    }

    /**
     * @param xCountry The x-country
     */
    @JsonProperty("x-country")
    public void setXCountry(String xCountry) {
        this.xCountry = xCountry;
    }

    /**
     * @return The xBrand
     */
    @JsonProperty("x-brand")
    public String getXBrand() {
        return xBrand;
    }

    /**
     * @param xBrand The x-brand
     */
    @JsonProperty("x-brand")
    public void setXBrand(String xBrand) {
        this.xBrand = xBrand;
    }

    /**
     * @return The host
     */
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    /**
     * @param host The host
     */
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return The contentType
     */
    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The content-type
     */
    @JsonProperty("content-type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return The connection
     */
    @JsonProperty("connection")
    public String getConnection() {
        return connection;
    }

    /**
     * @param connection The connection
     */
    @JsonProperty("connection")
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * @return The xLanguage
     */
    @JsonProperty("x-language")
    public String getXLanguage() {
        return xLanguage;
    }

    /**
     * @param xLanguage The x-language
     */
    @JsonProperty("x-language")
    public void setXLanguage(String xLanguage) {
        this.xLanguage = xLanguage;
    }

    /**
     * @return The randomheader
     */
    @JsonProperty("randomheader")
    public String getRandomheader() {
        return randomheader;
    }

    /**
     * @param randomheader The randomheader
     */
    @JsonProperty("randomheader")
    public void setRandomheader(String randomheader) {
        this.randomheader = randomheader;
    }

    /**
     * @return The accept
     */
    @JsonProperty("accept")
    public String getAccept() {
        return accept;
    }

    /**
     * @param accept The accept
     */
    @JsonProperty("accept")
    public void setAccept(String accept) {
        this.accept = accept;
    }

    /**
     * @return The userAgent
     */
    @JsonProperty("user-agent")
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent The user-agent
     */
    @JsonProperty("user-agent")
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @JsonProperty("request-id")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("request-id")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("request-name")
    public String getRequestName() {
        return requestName;
    }

    @JsonProperty("request-name")
    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    @JsonProperty("session-id")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("session-id")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("period-number")
    public String getPeriodNumber() {
        return periodNumber;
    }

    @JsonProperty("period-number")
    public void setPeriodNumber(String periodNumber) {
        this.periodNumber = periodNumber;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

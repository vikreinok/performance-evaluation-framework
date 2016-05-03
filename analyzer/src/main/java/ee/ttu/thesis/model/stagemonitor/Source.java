
package ee.ttu.thesis.model.stagemonitor;

import org.codehaus.jackson.annotate.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "bytesWritten",
    "headers",
    "measurement_start",
    "instance",
    "method",
    "userAgent",
    "containsCallTree",
    "sessionId",
    "error",
    "url",
    "executionTimeDb",
    "executionTime",
    "@timestamp",
    "application",
    "executionCountDb",
    "callStack",
    "name",
    "host",
    "id",
    "executionTimeCpu",
    "uniqueVisitorId",
    "parameters",
    "statusCode",
    "status"
})
public class Source {

    @JsonProperty("bytesWritten")
    private Long bytesWritten;
    @JsonProperty("headers")
    private Headers headers;
    @JsonProperty("measurement_start")
    private Long measurementStart;
    @JsonProperty("instance")
    private String instance;
    @JsonProperty("method")
    private String method;
    @JsonProperty("userAgent")
    private UserAgent userAgent;
    @JsonProperty("containsCallTree")
    private Boolean containsCallTree;
    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("url")
    private String url;
    @JsonProperty("executionTimeDb")
    private Long executionTimeDb;
    @JsonProperty("executionTime")
    private Long executionTime;
    @JsonProperty("@timestamp")
    private String Timestamp;
    @JsonProperty("application")
    private String application;
    @JsonProperty("executionCountDb")
    private Long executionCountDb;
    @JsonProperty("callStack")
    private String callStack;
    @JsonProperty("name")
    private String name;
    @JsonProperty("host")
    private String host;
    @JsonProperty("id")
    private String id;
    @JsonProperty("executionTimeCpu")
    private Long executionTimeCpu;
    @JsonProperty("uniqueVisitorId")
    private String uniqueVisitorId;
    @JsonProperty("parameters")
    private Parameters parameters;
    @JsonProperty("statusCode")
    private Long statusCode;
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The bytesWritten
     */
    @JsonProperty("bytesWritten")
    public Long getBytesWritten() {
        return bytesWritten;
    }

    /**
     * 
     * @param bytesWritten
     *     The bytesWritten
     */
    @JsonProperty("bytesWritten")
    public void setBytesWritten(Long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    /**
     * 
     * @return
     *     The headers
     */
    @JsonProperty("headers")
    public Headers getHeaders() {
        return headers;
    }

    /**
     * 
     * @param headers
     *     The headers
     */
    @JsonProperty("headers")
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    /**
     * 
     * @return
     *     The measurementStart
     */
    @JsonProperty("measurement_start")
    public Long getMeasurementStart() {
        return measurementStart;
    }

    /**
     * 
     * @param measurementStart
     *     The measurement_start
     */
    @JsonProperty("measurement_start")
    public void setMeasurementStart(Long measurementStart) {
        this.measurementStart = measurementStart;
    }

    /**
     * 
     * @return
     *     The instance
     */
    @JsonProperty("instance")
    public String getInstance() {
        return instance;
    }

    /**
     * 
     * @param instance
     *     The instance
     */
    @JsonProperty("instance")
    public void setInstance(String instance) {
        this.instance = instance;
    }

    /**
     * 
     * @return
     *     The method
     */
    @JsonProperty("method")
    public String getMethod() {
        return method;
    }

    /**
     * 
     * @param method
     *     The method
     */
    @JsonProperty("method")
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 
     * @return
     *     The userAgent
     */
    @JsonProperty("userAgent")
    public UserAgent getUserAgent() {
        return userAgent;
    }

    /**
     * 
     * @param userAgent
     *     The userAgent
     */
    @JsonProperty("userAgent")
    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 
     * @return
     *     The containsCallTree
     */
    @JsonProperty("containsCallTree")
    public Boolean getContainsCallTree() {
        return containsCallTree;
    }

    /**
     * 
     * @param containsCallTree
     *     The containsCallTree
     */
    @JsonProperty("containsCallTree")
    public void setContainsCallTree(Boolean containsCallTree) {
        this.containsCallTree = containsCallTree;
    }

    /**
     * 
     * @return
     *     The sessionId
     */
    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 
     * @param sessionId
     *     The sessionId
     */
    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The executionTimeDb
     */
    @JsonProperty("executionTimeDb")
    public Long getExecutionTimeDb() {
        return executionTimeDb;
    }

    /**
     * 
     * @param executionTimeDb
     *     The executionTimeDb
     */
    @JsonProperty("executionTimeDb")
    public void setExecutionTimeDb(Long executionTimeDb) {
        this.executionTimeDb = executionTimeDb;
    }

    /**
     * 
     * @return
     *     The executionTime
     */
    @JsonProperty("executionTime")
    public Long getExecutionTime() {
        return executionTime;
    }

    /**
     * 
     * @param executionTime
     *     The executionTime
     */
    @JsonProperty("executionTime")
    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * 
     * @return
     *     The Timestamp
     */
    @JsonProperty("@timestamp")
    public String getTimestamp() {
        return Timestamp;
    }

    /**
     * 
     * @param Timestamp
     *     The @timestamp
     */
    @JsonProperty("@timestamp")
    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }

    /**
     * 
     * @return
     *     The application
     */
    @JsonProperty("application")
    public String getApplication() {
        return application;
    }

    /**
     * 
     * @param application
     *     The application
     */
    @JsonProperty("application")
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * 
     * @return
     *     The executionCountDb
     */
    @JsonProperty("executionCountDb")
    public Long getExecutionCountDb() {
        return executionCountDb;
    }

    /**
     * 
     * @param executionCountDb
     *     The executionCountDb
     */
    @JsonProperty("executionCountDb")
    public void setExecutionCountDb(Long executionCountDb) {
        this.executionCountDb = executionCountDb;
    }

    /**
     * 
     * @return
     *     The callStack
     */
    @JsonProperty("callStack")
    public String getCallStack() {
        return callStack;
    }

    /**
     * 
     * @param callStack
     *     The callStack
     */
    @JsonProperty("callStack")
    public void setCallStack(String callStack) {
        this.callStack = callStack;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The host
     */
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    /**
     * 
     * @param host
     *     The host
     */
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The executionTimeCpu
     */
    @JsonProperty("executionTimeCpu")
    public Long getExecutionTimeCpu() {
        return executionTimeCpu;
    }

    /**
     * 
     * @param executionTimeCpu
     *     The executionTimeCpu
     */
    @JsonProperty("executionTimeCpu")
    public void setExecutionTimeCpu(Long executionTimeCpu) {
        this.executionTimeCpu = executionTimeCpu;
    }

    /**
     * 
     * @return
     *     The uniqueVisitorId
     */
    @JsonProperty("uniqueVisitorId")
    public String getUniqueVisitorId() {
        return uniqueVisitorId;
    }

    /**
     * 
     * @param uniqueVisitorId
     *     The uniqueVisitorId
     */
    @JsonProperty("uniqueVisitorId")
    public void setUniqueVisitorId(String uniqueVisitorId) {
        this.uniqueVisitorId = uniqueVisitorId;
    }

    /**
     * 
     * @return
     *     The parameters
     */
    @JsonProperty("parameters")
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * 
     * @param parameters
     *     The parameters
     */
    @JsonProperty("parameters")
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * 
     * @return
     *     The statusCode
     */
    @JsonProperty("statusCode")
    public Long getStatusCode() {
        return statusCode;
    }

    /**
     * 
     * @param statusCode
     *     The statusCode
     */
    @JsonProperty("statusCode")
    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
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

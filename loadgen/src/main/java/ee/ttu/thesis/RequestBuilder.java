package ee.ttu.thesis;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import ee.ttu.thesis.aio.model.RequestInformation;
import ee.ttu.thesis.filter.ResponseTimeFilter;
import ee.ttu.thesis.filter.SetHeaderFilter;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 *
 */
public class RequestBuilder {

    public static final String HEADER_NAME_REQUEST_NAME = "request-name";
    public static final String HEADER_NAME_REQUEST_ID = "request-id";
    public static final String HEADER_NAME_SESSION_ID = "session-id";
    public static final String HEADER_NAME_PERIOD_NUMBER = "period-number";
    public final SetHeaderFilter DEFAULT_CONTENT_TYPE_HEADER = new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

    public SetHeaderFilter requestNameFilter = new SetHeaderFilter(HEADER_NAME_REQUEST_NAME, "");
    public SetHeaderFilter requestIdFilter = new SetHeaderFilter(HEADER_NAME_REQUEST_ID, "");
    public SetHeaderFilter sessionIdFilter = new SetHeaderFilter(HEADER_NAME_SESSION_ID, "");
    public SetHeaderFilter periodNumberFilter = new SetHeaderFilter(HEADER_NAME_PERIOD_NUMBER, "");

    protected String contextPath = "";
    protected Client client;
    protected RequestInformation requestInformation;
    private String host = "http://localhost:8080/";

    public RequestBuilder(String contextPath) {
        this.contextPath = contextPath;
    }

    public RequestBuilder(String host, String contextPath) {
        this.host = host;
        this.contextPath = contextPath;
    }

    public RequestBuilder addHeader(String name, String value) {
        getClient().addFilter(new SetHeaderFilter(name, value));
        return this;
    }

    public RequestBuilder builder() {
        return this;
    }

    public void build() {
        getClient();
    }

    public WebResource resource(String path, String requestId, Object... values) {
        setRequestNameFilterValue(requestInformation.buildRequestName(requestId));
        setRequestIdFilterValue(requestId);
        setSessionIdFilterValue(requestInformation.getSessionId());
        setPeriodNumberFilterValue(requestInformation.getPeriodNumber());
        return resource(UriBuilder.fromPath(path).build(values).toString());
    }

    protected WebResource resource(String path) {

        String uri;
        if (path.contains("http://") || path.contains("https://")) {
            uri = path;
        } else {
            uri = host + contextPath + path;
        }

        WebResource resource = getClient().resource(uri);
        return resource;
    }

    protected Client getClient() {
        if (client == null) {

            ClientConfig cc = new DefaultClientConfig();
            cc.getClasses().add(JacksonJsonProvider.class);

            client = Client.create(cc);

            client.addFilter(DEFAULT_CONTENT_TYPE_HEADER);

//            client.addFilter(new LoggingFilter(System.out));
            client.addFilter(new ResponseTimeFilter());

            client.setFollowRedirects(false);
        }

        return client;
    }

    public void setType(String mediaType) {
        removeContentType();
        getClient().addFilter(new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, mediaType));
    }

    public void removeContentType() {
        getClient().removeFilter(DEFAULT_CONTENT_TYPE_HEADER);
    }

    public void setRequestNameFilterValue(String requestName) {
        if (getClient().isFilterPresent(requestNameFilter)) {
            getClient().removeFilter(requestNameFilter);
        }
        requestNameFilter = new SetHeaderFilter(HEADER_NAME_REQUEST_NAME, requestName);
        getClient().addFilter(requestNameFilter);
    }

    public void setRequestIdFilterValue(String requestId) {
        if (getClient().isFilterPresent(requestIdFilter)) {
            getClient().removeFilter(requestIdFilter);
        }
        requestIdFilter = new SetHeaderFilter(HEADER_NAME_REQUEST_ID, requestId);
        getClient().addFilter(requestIdFilter);
    }

    public void setSessionIdFilterValue(String sessionId) {
        if (getClient().isFilterPresent(sessionIdFilter)) {
            getClient().removeFilter(sessionIdFilter);
        }
        sessionIdFilter = new SetHeaderFilter(HEADER_NAME_SESSION_ID, sessionId);
        getClient().addFilter(sessionIdFilter);
    }

    public void setPeriodNumberFilterValue(String periodNumber) {
        if (getClient().isFilterPresent(periodNumberFilter)) {
            getClient().removeFilter(periodNumberFilter);
        }
        periodNumberFilter = new SetHeaderFilter(HEADER_NAME_PERIOD_NUMBER, periodNumber);
        getClient().addFilter(periodNumberFilter);

    }

    public void setRequestInformation(RequestInformation requestInformation) {
        this.requestInformation = requestInformation;
    }
}

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

    public static final String HEADER_NAME_REQUEST_ID = "request-id";
    public final SetHeaderFilter DEFAULT_CONTENT_TYPE_HEADER = new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    public final SetHeaderFilter DEFAULT_REQUEST_ID_FILTER = new SetHeaderFilter(HEADER_NAME_REQUEST_ID, "");

    private String host = "http://localhost:8080/";
    protected String contextPath = "";
    protected Client client;

    protected RequestInformation requestInformation;

    public RequestBuilder(String contextPath) {
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
        String requestIdentifier = requestInformation.buildRequestIdentifier(requestId);
        setRequestIdFilterValue(requestIdentifier);
        return resource(UriBuilder.fromPath(path).build(values).toString());
    }

    protected WebResource resource(String path) {
        WebResource resource = getClient().resource(host + contextPath + path);
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

    public void setRequestIdFilterValue(String requestId) {
        getClient().removeFilter(DEFAULT_REQUEST_ID_FILTER);
        getClient().addFilter(new SetHeaderFilter(HEADER_NAME_REQUEST_ID, requestId));
    }

    public void setRequestInformation(RequestInformation requestInformation) {
        this.requestInformation = requestInformation;
    }
}

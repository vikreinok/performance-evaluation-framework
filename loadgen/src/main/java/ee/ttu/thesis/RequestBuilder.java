package ee.ttu.thesis;

import com.mcbfinance.aio.web.rest.SetHeaderFilter;
import com.mcbfinance.aio.web.rest.model.AuthenticationInfoDTO;
import com.mcbfinance.aio.web.rest.model.CustomerDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RequestBuilder {

    public static final String HEADER_NAME_COOKIE = "Cookie";
    public static final String HEADER_NAME_SET_COOKIE = "Set-Cookie";
    public static final SetHeaderFilter DEFAULT_CONTENT_TYPE_HEADER = new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

    private String host = "http://localhost:8080/";
    protected String contextPath = "";
    protected Client client;

    private List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>(4);

    public RequestBuilder(String contextPath) {
        this.contextPath = contextPath;
    }

    public RequestBuilder addHeader(String name, String value) {
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(name, value);
        headers.add(entry);
        getClient().addFilter(new SetHeaderFilter(name, value));
        return this;
    }

    public WebResource resource(String path, Object... values) {
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
            for (Map.Entry<String, String> header : headers) {
                client.addFilter(new SetHeaderFilter(header.getKey(), header.getValue()));
            }

            client.addFilter(new SetHeaderFilter(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON + ", " + MediaType.WILDCARD));
            client.addFilter(DEFAULT_CONTENT_TYPE_HEADER);
//            Adds logging
//            client.addFilter(new LoggingFilter(System.out));

            client.setFollowRedirects(false);
        }

        return client;
    }

    protected CustomerDTO getCurrentCustomer() {
        return resource("/authentication").get(AuthenticationInfoDTO.class).customer;
    }

    protected void logout() {
        resource("/authentication").delete();
    }


    public void setType(String mediaType) {
        removeContentType();
        client.addFilter(new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, mediaType));
    }

    public void removeContentType() {
        getClient().removeFilter(DEFAULT_CONTENT_TYPE_HEADER);
    }
}

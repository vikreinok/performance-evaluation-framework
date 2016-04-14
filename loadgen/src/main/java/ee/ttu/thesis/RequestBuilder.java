package ee.ttu.thesis;

import com.mcbfinance.aio.web.rest.SetHeaderFilter;
import com.mcbfinance.aio.web.rest.model.AuthenticationInfoDTO;
import com.mcbfinance.aio.web.rest.model.CustomerDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
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


    private static final String APPLICATION_JSON = "application/json";
    public static final String HEADER_NAME_COOKIE = "Cookie";
    public static final String HEADER_NAME_SET_COOKIE = "Set-Cookie";

    private String host = "http://localhost:8080/";
    protected String contextPath = "catering-core/";
    protected Client client;
    protected String accept = APPLICATION_JSON;

    private List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>(4);

    public RequestBuilder(String contextPath) {
        this();
        this.contextPath = contextPath;
    }

    public RequestBuilder() {
    }


    public RequestBuilder addHeader(String name, String value) {
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(name, value);
        headers.add(entry);
        getClient().addFilter(new SetHeaderFilter(name, value));
        return this;
    }

    public RequestBuilder setAccept(String value) {
        accept = value;
        return this;
    }


    public ClientResponse put(String path, GenericType<?> type, String payload) {
        WebResource webResource = client.resource(host + contextPath + path);
        WebResource.Builder requestBuilder = webResource.getRequestBuilder();

        requestBuilder = addHeaders(requestBuilder);

        requestBuilder.accept(accept);
        requestBuilder.type(accept);

        ClientResponse response = requestBuilder.post(ClientResponse.class, payload);

        handleError(response);

        return response;
    }

    private WebResource.Builder addHeaders(WebResource.Builder requestBuilder) {
        for (Map.Entry<String, String> header : headers) {
            requestBuilder = requestBuilder.header(header.getKey(), header.getValue());
        }
        return requestBuilder;
    }


    public ClientResponse post(String path, GenericType<?> type, Object payload) {
        WebResource webResource = client.resource(host + contextPath + path);
        WebResource.Builder requestBuilder = webResource.getRequestBuilder();

        requestBuilder = addHeaders(requestBuilder);

        requestBuilder.accept(accept);
        requestBuilder.type(accept);

        ClientResponse response = requestBuilder.post(ClientResponse.class, payload);

        handleError(response);
        System.out.println(response.toString());
        System.out.println(response.getEntityInputStream().toString());

        return response;
    }


    public ClientResponse get(String path, GenericType<?> type) {
        WebResource webResource = client.resource(host + contextPath + path);
        WebResource.Builder requestBuilder = webResource.getRequestBuilder();

        requestBuilder = addHeaders(requestBuilder);

        requestBuilder.accept(accept);

        ClientResponse response = requestBuilder.get(ClientResponse.class);

        handleError(response);

        return response;
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
//            Reflections reflections = new Reflections("com.mcbfinance.aio");
//
//            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Provider.class);
//
//
            ClientConfig cc = new DefaultClientConfig();
            cc.getClasses().add(JacksonJsonProvider.class);
//            cc.getClasses().add(JSONListElementProvider.class);
//
//            Iterator<Class<?>> iterator = annotated.iterator();
//            while (iterator.hasNext()) {
//                Class next =  iterator.next();
//                if (next.getName().equals(FinancialDataProvider.class.getName()) ) {
//                    System.out.println("Removing provider " + next.getSimpleName());
//                    iterator.remove();
//                }
//            }
//            for (Class<?> clazz : annotated) {
//                cc.getClasses().add(clazz);
//            }
            client = Client.create(cc);
//            client = Client.create();
            for (Map.Entry<String, String> header : headers) {
                client.addFilter(new SetHeaderFilter(header.getKey(), header.getValue()));
            }

            client.addFilter(new SetHeaderFilter(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON + ", " + MediaType.WILDCARD ));
            client.addFilter(new SetHeaderFilter(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        }

        return client;
    }

    protected CustomerDTO getCurrentCustomer() {
        return resource("/authentication").get(AuthenticationInfoDTO.class).customer;
    }

    protected void logout() {
        resource("/authentication").delete();
    }

    private void handleError(ClientResponse response) {
        if (response.getStatus() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

            String currentThreadName = Thread.currentThread().getName();
            System.err.println("Failed : HTTP error code : " + response.getStatus() + ", Current thread name" + currentThreadName + " " + response.toString());
        }
    }


}

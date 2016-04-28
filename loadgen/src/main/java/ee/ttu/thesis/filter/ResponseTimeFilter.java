package ee.ttu.thesis.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import ee.ttu.thesis.RequestBuilder;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 *
 */
public class ResponseTimeFilter extends ClientFilter {

    private Long id;

    @Override
    public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
        long identifier = ((id != null) ? ++this.id : 0);
        this.logRequest(identifier, clientRequest);

        String header = getRequestIdHeader(clientRequest);

        long startTime = System.currentTimeMillis();
        ClientResponse response = this.getNext().handle(clientRequest);
        long endTime = System.currentTimeMillis();

        long responseTime = endTime - startTime;
        System.out.printf("%-180s %-15s %010d [ms]%n", clientRequest.getURI().toString(), header, responseTime);
        this.logResponse(identifier, response);
        return response;
     }

    private String getRequestIdHeader(ClientRequest clientRequest) {
        String header = "";
        MultivaluedMap<String, Object> headers = clientRequest.getHeaders();
        List<Object> headerValues = headers.get(RequestBuilder.HEADER_NAME_REQUEST_ID);
        if (headerValues != null && headerValues.size() > 0) {
            header = (String)headerValues.get(0);
        }
        return header;
    }

    private void logRequest(long id, ClientRequest clientRequest) {

    }

    private void logResponse(long id, ClientResponse response) {

    }
}

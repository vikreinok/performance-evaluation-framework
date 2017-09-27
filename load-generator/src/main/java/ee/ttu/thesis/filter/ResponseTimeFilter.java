package ee.ttu.thesis.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import ee.ttu.thesis.RequestBuilder;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * For logging request time
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
        String message = String.format("%-6s %-180s %-15s %06d [ms]%n", clientRequest.getMethod(), clientRequest.getURI().toString(), header, responseTime);
        log(message);
        this.logResponse(identifier, response);
        return response;
     }

    private String getRequestIdHeader(ClientRequest clientRequest) {
        String header = "";
        MultivaluedMap<String, Object> headers = clientRequest.getHeaders();
        List<Object> headerValues = headers.get(RequestBuilder.HEADER_NAME_REQUEST_NAME);
        if (headerValues != null && headerValues.size() > 0) {
            header = (String)headerValues.get(0);
        }
        return header;
    }

    private void logRequest(long id, ClientRequest clientRequest) {

    }

    private void logResponse(long id, ClientResponse response) {

    }


    /**
     * TODO use logging framework such a log4j
     * @param message
     */
    private void log(String message) {
        try {
            BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));

            log.write(message);
            log.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

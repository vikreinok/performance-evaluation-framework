package ee.ttu.thesis.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 *
 */
public class ResponseTimeFilter extends ClientFilter {

    private Long id;

    @Override
    public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
        long identifier = ((id != null) ? ++this.id : 0);
        this.logRequest(identifier, clientRequest);
        long startTime = System.currentTimeMillis();
        ClientResponse response = this.getNext().handle(clientRequest);
        long endTime = System.currentTimeMillis();

        long responseTime = endTime - startTime;
        System.out.printf("%-80ss %010d [ms]%n", clientRequest.getURI().toString(), responseTime);
        this.logResponse(identifier, response);
        return response;
     }

    private void logRequest(long id, ClientRequest clientRequest) {

    }

    private void logResponse(long id, ClientResponse response) {

    }
}

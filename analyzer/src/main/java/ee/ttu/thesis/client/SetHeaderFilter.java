package ee.ttu.thesis.client;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class SetHeaderFilter extends ClientFilter {
    
    private final String name;
    private final String value;
    
    public SetHeaderFilter(String headerName, String headerValue) {
        name = headerName;
        value = headerValue;
    }

    public SetHeaderFilter(String headerName, String... headerValues) {
        name = headerName;

        String tempValue = "";
        for (int i = 0; i < headerValues.length; i++) {
            String headerValue = headerValues[i];
            tempValue += headerValue;
            if (i < headerValues.length - 1) {
                tempValue += "; ";
            }
        }
        value = tempValue;
    }

    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        cr.getHeaders().add(name, value);
        ClientResponse response = getNext().handle(cr);
        return response;
    }
}
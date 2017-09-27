package ee.ttu.thesis.aio;

import com.mcbfinance.aio.web.rest.model.PasswordAuthenticationDTO;
import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.aio.model.DomainInformation;
import ee.ttu.thesis.petclinic.AbstractLoadGenerator;

import javax.ws.rs.core.HttpHeaders;

/**
 *
 */
public abstract class AbstractAio extends AbstractLoadGenerator implements AioFlowSteps {

    public static final String CONTEXT_PATH = "loanengine/rest/";
    public static final int EXECUTION_COUNT = 50;


    protected DomainInformation domainInformation = null;

    @Override
    public abstract String getCountry();

    @Override
    public abstract String getBrand();

    @Override
    public abstract String getLanguage();

    @Override
    protected Runnable getCustomerFlow(String modificationId, Integer sessionId) {
        // Modification id no implemented because it is out of scope to fix huge applications
        return new AioFlow(sessionId);
    }

    @Override
    public void logout() {
        rb.resource("authentication", "11000").delete();
    }

    @Override
    public void login(String username, String password) {
        PasswordAuthenticationDTO requestEntity = new PasswordAuthenticationDTO(username, password);

        try {
            ClientResponse clientResponse = rb.resource("authentication", "12000").post(ClientResponse.class, requestEntity);

            if (clientResponse.getStatus() == 200) {
                rb.addHeader(HttpHeaders.COOKIE, clientResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        logToConsole(authenticationInfoDTO);
    }

    @Override
    public void acceptCases(int nrOfCalls) {
        // dev resource accept cases
        for (int count = 0; count < nrOfCalls; count++) {
            rb.resource("developer/cases", "100" + nrOfCalls + count).post(); // 204 no content
        }
    }

}

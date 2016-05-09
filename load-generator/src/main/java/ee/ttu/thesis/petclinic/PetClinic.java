package ee.ttu.thesis.petclinic;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.NamedThreadPoolExecutor;
import ee.ttu.thesis.RequestBuilder;
import ee.ttu.thesis.aio.model.RequestInformation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static ee.ttu.thesis.util.Logger.logErr;

/**
 *
 */
public class PetClinic {

    public static final int EXECUTION_COUNT = 50;
    public static final String MODIFICATION_ID = "0000";

    private static final int PARALLEL_THREADS = 1;

    private RequestInformation requestInformation;

    public PetClinic() {
        this.requestInformation = new RequestInformation();;
    }

    public static void main(String[] args) {
        PetClinic.start();
    }

    public static void start() {

        ThreadPoolExecutor threadPoolExecutor =
                new NamedThreadPoolExecutor(
                        50,
                        100,
                        5000,
                        TimeUnit.MILLISECONDS,
                        "-PetClinicRequestThreadPoolExecutor-"
                );
        for (int index = 0; index < PARALLEL_THREADS; index++) {

            String threadName = "PetClinicRequesterNr_" + index;
            Integer sessionId = index;

            threadPoolExecutor.execute(new PetClinicFlow(MODIFICATION_ID, threadName, sessionId));
        }
        threadPoolExecutor.shutdown();
    }


    public RequestInformation getRequestInformation() {
        return requestInformation;
    }

    public void viewAndAdd() {

        RequestBuilder rb = new RequestBuilder("http://localhost:9966/", "petclinic");
        rb.setRequestInformation(requestInformation);

        rb.resource("/owners", "001000").queryParam("lastName", "").get(ClientResponse.class);

        String ownerPayload = "firstName=Firstname&lastName=Lastname&address=Address&city=City&telephone=12345678";
        rb.removeContentType();
        ClientResponse addnewOwnerResponse = rb.resource("/owners/new", "002000").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, ownerPayload);
        rb.setType(MediaType.APPLICATION_JSON);
        String location = addnewOwnerResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);

        String ownerId = location.substring(location.lastIndexOf("/") + 1, location.length());

        ClientResponse getOwnerResponse = rb.resource(location, "003000").get(ClientResponse.class);
        String jSessionId = getOwnerResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        rb.addHeader(HttpHeaders.COOKIE, jSessionId);

        rb.resource("/owners/" + ownerId + "/pets/new", "004000").get(ClientResponse.class);

        rb.removeContentType();
        rb.resource("/owners/" + ownerId + "/pets/new", "005000")
                .queryParam("name", "Petname")
                .queryParam("birthDate", "2015/04/01")
                .queryParam("type", "cat")
                .type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
        rb.setType(MediaType.APPLICATION_JSON);


    }
}

class PetClinicFlow implements Runnable {

    private String modificationId;
    private String threadName;
    private Integer sessionId;

    public PetClinicFlow(String modificationId, String threadName, Integer sessionId) {
        this.modificationId = modificationId;
        this.threadName = threadName;
        this.sessionId = sessionId;
    }

    public void run() {
        PetClinic petClinic = new PetClinic();

        RequestInformation requestInformation = petClinic.getRequestInformation();

        requestInformation.setPeriodNumber(0);
        requestInformation.setSessionId(sessionId);
        requestInformation.setModificationId(modificationId);

        try {
            for (int executionNumber = 0; executionNumber < PetClinic.EXECUTION_COUNT; executionNumber++) {
                requestInformation.setPeriodNumber(executionNumber);
                petClinic.viewAndAdd();
            }
        } catch (ClientHandlerException e) {
            logErr("Before running load generator please run petclinic app by command at petclinic project root 'mvn tomcat7:run'");
         } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




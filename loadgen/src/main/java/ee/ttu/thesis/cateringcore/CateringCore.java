package ee.ttu.thesis.cateringcore;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import ee.ttu.thesis.NamedThreadPoolExecutor;
import ee.ttu.thesis.RequestBuilder;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CateringCore {

    public static void main(String[] args) {
        CateringCore cc = new CateringCore();
        cc.start();
    }

    public void start() {

        int corePoolSize = 5;
        int maxPoolSize = 100;
        long keepAliveTime = 5000;

        ThreadPoolExecutor threadPoolExecutor =
                new NamedThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        "-CateringCoreRequestThreadPoolExecutor-"
                );
        for (int index = 0; index <= 5; index++) {

            threadPoolExecutor.execute(new CateringCoreFlow("CateringCoreRequest " + index));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        threadPoolExecutor.shutdown();


    }


}

class CateringCoreFlow implements Runnable {

    private String name;

    public CateringCoreFlow(String name) {
        this.name = name;
    }

    public void run() {
        final GenericType<Collection<String>> genericType = new GenericType<Collection<String>>() {};

        RequestBuilder authBuilder = new RequestBuilder();
        authBuilder.addHeader("Authorization", "YWRtaW46YWRtaW4=");

        ClientResponse clientResponse = authBuilder.post("rest/login", genericType, "{}");
        String token = clientResponse.getHeaders().get("X-Token").get(0);

        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.addHeader("X-Token", token);

        for (int i = 0; i < 10000; i++) {

            int sleepDuration = new Random().nextInt(50) + 10;
            sleep(sleepDuration);

//            requestBuilder.get(genericType, "rest/diner");
//            sleep(10);

            requestBuilder.get("rest/diner/menus/2", genericType);
//            sleep(10);

            String menuPayload = "{\"menuId\":\"3\",\"id\":null,\"name\":\"Meatball\",\"price\":\"2.5\",\"created\":\"\",\"vegeterian\":null,\"foodType\":null,\"menu\":{\"id\":3}}";
            requestBuilder.post("rest/menu_item", genericType, menuPayload);
//            sleep(10);

            String dinerPayload = "{\"id\":null,\"name\":\"Diner\",\"description\":\"Test diner\",\"picture\":\"-1\",\"modifyDate\":\"\",\"created\":\"\"}";
            requestBuilder.post("rest/diner", genericType, dinerPayload);
//            sleep(10);


        }
    }

    private void sleep(int sleepDuration) {
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}


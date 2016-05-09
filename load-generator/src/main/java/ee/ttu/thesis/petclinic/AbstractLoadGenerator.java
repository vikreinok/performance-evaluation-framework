package ee.ttu.thesis.petclinic;

import ee.ttu.thesis.NamedThreadPoolExecutor;
import ee.ttu.thesis.RequestBuilder;
import ee.ttu.thesis.aio.model.RequestInformation;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public abstract class AbstractLoadGenerator {

    protected static final String MODIFICATION_ID = "0000";
    protected static final int PARALLEL_THREADS = 1;

    protected  RequestInformation requestInformation;
    protected RequestBuilder rb = null;

    public AbstractLoadGenerator() {
        this.requestInformation = new RequestInformation();
    }

    protected ThreadPoolExecutor createThreadPoolExecutor(String applicationName) {
        int corePoolSize = 5;
        int maximumPoolSize = 100;
        int keepAliveTime = 5000;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        return new NamedThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                "-" + applicationName + "RequestThreadPoolExecutor-"
        );
    }

    protected void start(String modificationId) {

        ThreadPoolExecutor threadPoolExecutor = createThreadPoolExecutor("PetClinic");

        for (int index = 0; index < getThreadCount(); index++) {

            Integer sessionId = index;

            threadPoolExecutor.execute(getCustomerFlow(modificationId, sessionId));
        }
        threadPoolExecutor.shutdown();
    }

    protected static String getFirstArgumentValue(String[] args) {
        String modificationId = MODIFICATION_ID;
        if (args != null && args.length > 0) {
            modificationId = args[0];
        }
        return modificationId;
    }

    protected int getThreadCount() {
        return PARALLEL_THREADS;
    }

    protected abstract Runnable getCustomerFlow(String modificationId, Integer sessionId);




    public RequestInformation getRequestInformation() {
        return requestInformation;
    }
}

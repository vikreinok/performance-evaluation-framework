package ee.ttu.thesis.aio;

import ee.ttu.thesis.aio.model.RequestInformation;

public class AioFlow implements Runnable {

        private int sessionId;

        public AioFlow(Integer sessionId) {
            this.sessionId = sessionId;
        }

        public void run() {
            AioLT aio = new AioLT();

            RequestInformation requestInformation = aio.getRequestInformation();

            requestInformation.setPeriodNumber(0);
            requestInformation.setSessionId(sessionId);

            aio.registration();

            try {
                for (int executionNumber = 0; executionNumber < AbstractAio.EXECUTION_COUNT; executionNumber++) {
                    requestInformation.setPeriodNumber(executionNumber);
                    aio.viewSSAndDraw();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

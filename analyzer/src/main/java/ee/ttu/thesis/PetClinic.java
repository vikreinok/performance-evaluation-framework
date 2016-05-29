package ee.ttu.thesis;

import ee.ttu.thesis.model.processor.Result;
import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.processor.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data analyzer initializer for Pet Clinic application
 * Elastic search queries are suited for load data generated for Per Clinic application
 */
public class PetClinic {


    public static void main(String[] args) {
        PetClinic petClinic = new PetClinic();
        petClinic.analyze();
    }

    private void analyze() {
        try {
            HttpQuery esQuery = new HttpQuery();

//            getSettings();
//            putSettings();

            Map<String, List<String>> uniqueModificationRequestIds = esQuery.getModificationRequestIds("petclinic_uniqueModificationAndRequestIds.json");


            for (String modificationId : uniqueModificationRequestIds.keySet()) {
                log(String.format("-----------------------ModificationId %s-----------------------", modificationId));

                Map<String, List<Result>> previousResults = new HashMap<String, List<Result>>();
                for (String requestId : uniqueModificationRequestIds.get(modificationId)) {
                    List<Source> data = esQuery.getResponseData("petclinic_generic.json", requestId, modificationId);

                    Analyzer analyzer = new Analyzer();
                    analyzer.addProcessor(
                            new DbQueryCountProcessor(),
                            new DbExecutionTimeProcessor(),
                            new ExecutionTimeProcessor(),
                            new CallingContextTreeSizeProcessor(),
                            new CallingContextTreeDepthProcessor()
                    );
                    List<Result> results = analyzer.processMetrics(data);
                    analyzer.compare(previousResults.get(requestId), results);

                    previousResults.put(requestId, results);
//                    log(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String str) {
        System.out.println(str);
    }

    private void log(Object obj) {
        System.out.println(ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE));
    }
}

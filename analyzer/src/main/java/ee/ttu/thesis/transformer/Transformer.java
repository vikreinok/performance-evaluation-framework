package ee.ttu.thesis.transformer;

import ee.ttu.thesis.model.aggregations.Aggregation;
import ee.ttu.thesis.model.aggregations.Bucket;
import ee.ttu.thesis.model.aggregations.Bucket_;
import ee.ttu.thesis.model.stagemonitor.Hit;
import ee.ttu.thesis.model.stagemonitor.Response;
import ee.ttu.thesis.model.stagemonitor.Source;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Used fro transform responses from ElasticSearch JSON to object representation.
 */
public class Transformer {

    /**
     * Transforms response from ES (JSON) to object
     *
     * Structure of response from ES
     *
     * <ul>
     * <li>modification id - 0000
     * <ul>
     * <li>Request id - 000000</li>
     * <li>Request id - 000001</li>
     * <li>Request id - ...</li>
     * </ul>
     * </li>
     * <li>modification id - 0001
     * <ul>
     * <li>Request id - 000000</li>
     * <li>Request id - ...</li>
     * </ul>
     * </li>
     * <li>modification id - ...</li>
     * </ul>
     *
     * @param jsonResponse - a response from ES query as a String
     * @return
     */
    public Map<String, List<String>> transformToModificationRequestId(String jsonResponse) {

        Aggregation response = null;

        ObjectMapper om = new ObjectMapper();
        try {
            response = om.readValue(jsonResponse, Aggregation.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to map json to " + Aggregation.class.getName());
        }

        // Have to preserve order of modification ids
        Map<String, List<String>> aggregations = new LinkedHashMap<String, List<String>>();

        if (response.getAggregations() == null && response.getAggregations().getGroupByModificationId().getBuckets().size() == 0) {
            throw new IllegalStateException("No request-id headers are found. Please make sure you have metrics data set and correct index name.");
        }

        for (Bucket modificationId : response.getAggregations().getGroupByModificationId().getBuckets()) {
            List<String> requestIds = new ArrayList<String>();
            for (Bucket_ requestId : modificationId.getGroupByRequestId().getBuckets()) {
                requestIds.add(requestId.getKey());
            }
            aggregations.put(modificationId.getKey(), requestIds);
        }

        return aggregations;
    }


    /**
     * Transforms response from ES (JSON) to object
     *
     * @param jsonResponse - a response from ES query as a String
     * @return
     */
    public List<Source> transformToResponseData(String jsonResponse) {

        ObjectMapper om = new ObjectMapper();
        Response response = null;
        try {
            response = om.readValue(jsonResponse, Response.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to map json to " + Response.class.getName());
        }

        List<Source> data = new ArrayList<Source>();
        if (response != null && response.getHits() != null) {
            List<Hit> hits = response.getHits().getHits();
//            log(String.format("Size of data %d", hits.size()));
            for (Hit hit : hits) {
                Source source = hit.getSource();
                data.add(source);

//                log(hit.getId());
//                parseResourcePaths(source.getCallStack());
//                log(source);
//                writeToFile(source.getId(), source.getCallStack());
            }
        }

        if (data == null || data.size() == 0) {
            throw new IllegalStateException("Data is empty");
        }

        return data;
    }


    private void writeToFile(String id, String message) {
        try {
            Files.write(Paths.get(String.format("C:\\Users\\Viktor.Reinok\\Desktop\\callStacks\\%s.txt", id)), message.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

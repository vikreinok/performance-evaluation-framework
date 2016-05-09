package ee.ttu.thesis;


import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.client.RequestBuilder;
import ee.ttu.thesis.model.aggregations.Aggregation;
import ee.ttu.thesis.model.aggregations.Bucket;
import ee.ttu.thesis.model.aggregations.Bucket_;
import ee.ttu.thesis.model.stagemonitor.Hit;
import ee.ttu.thesis.model.stagemonitor.Response;
import ee.ttu.thesis.model.stagemonitor.Source;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class HttpQuery {

    private RequestBuilder rb;

    RequestBuilder getRequestBuilder() {
        if (rb == null) {
            rb = new RequestBuilder("");
        }
        return rb;
    }

    public List<Source> getResponseData(String searchPath, String queryFileName, String requestId, String modificationId) throws IOException {
        String query = getQuery(queryFileName);
        query = query.replaceFirst("\"\\{requestId\\}\"", "\"" + requestId + "\"");
        query = query.replaceFirst("\"\\{modificationId\\}\"", "\"" + modificationId + "\"");

        ClientResponse clientResponse = getRequestBuilder().resource(searchPath).post(ClientResponse.class, query);
        String content = getString(clientResponse.getEntityInputStream());
//        log(content);

        ObjectMapper om = new ObjectMapper();



        Response response = om.readValue(content, Response.class);

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
        return data;
    }

    public Map<String, List<String>> getModificationRequestIds(String path, String queryFileName) throws IOException {

        String payload = getQuery(queryFileName);
        ClientResponse clientResponse = getRequestBuilder().resource(path).post(ClientResponse.class, payload);
        String content = getString(clientResponse.getEntityInputStream());
//        log(content);

        ObjectMapper om = new ObjectMapper();

        Aggregation response = om.readValue(content, Aggregation.class);

        // Have to preserve order of modification ids
        Map<String, List<String>> aggregations = new LinkedHashMap<String, List<String>>();

        if (response.getAggregations() == null) {
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

    private void getSettings(String settingsPath) {
        ClientResponse settingsGetClientResponse = getRequestBuilder().resource(settingsPath).get(ClientResponse.class);
        String settingsGetContent = getString(settingsGetClientResponse.getEntityInputStream());
        log(settingsGetContent);
    }

    private void putSettings(String settingsPath) {

        String settingsPayload = getQuery("settings.json");
        ClientResponse settingsPutClientResponse = getRequestBuilder().resource(settingsPath).put(ClientResponse.class, settingsPayload);
        String settingsPutContent = getString(settingsPutClientResponse.getEntityInputStream());
        log(settingsPutContent);
    }

    protected String getQuery(String name) {
        final String path = "query/";

        InputStream in = null;
        StringBuilder sb = null;
        try {

            URL url = this.getClass().getClassLoader().getResource(path + name);
            if (url == null) {
                throw new IllegalArgumentException(String.format("NO such file in resource directory folder '%s' file name '%s'", path, name));
            }
            File file;
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                file = new File(url.getPath());
            }
            in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

            String line;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String query = sb != null ? sb.toString() : "";
//        log(String.format("<-------------------------------%-50s-------------------------------->", name));
//        log(query);
//        log(String.format("</------------------------------%-50s--------------------------------/>", name));
        return query;
    }

    private void parseResourcePaths(String callStack) {

        String nthLineContent = getNthLine(callStack, 3);
        log(nthLineContent.substring(54));

    }

    private String getNthLine(String content, int lineNr) {
        String nthLineContent = null;
        BufferedReader br = new BufferedReader(new StringReader(content));
        try {
            nthLineContent = null;
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (counter == lineNr) {
                    nthLineContent = line;
                }
                counter++;
            }
        } catch (IOException e) {

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nthLineContent;
    }


    private void writeToFile(String id, String message) {
        try {
            Files.write(Paths.get(String.format("C:\\Users\\Viktor.Reinok\\Desktop\\callStacks\\%s.txt", id)), message.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getString(InputStream is) {
        String content = "";

        try {
            String UTF8 = "UTF-8";
            int BUFFER_SIZE = 8192;

            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    UTF8), BUFFER_SIZE);
            String str;
            while ((str = br.readLine()) != null) {
                content += str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    private void log(String str) {
        System.out.println(str);
    }

    private void log(Object obj) {
        System.out.println(ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE));
    }
}

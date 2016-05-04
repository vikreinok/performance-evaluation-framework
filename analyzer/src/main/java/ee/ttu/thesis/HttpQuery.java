package ee.ttu.thesis;

import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.client.RequestBuilder;
import ee.ttu.thesis.model.aggregations.Aggregation;
import ee.ttu.thesis.model.aggregations.Bucket;
import ee.ttu.thesis.model.stagemonitor.Hit;
import ee.ttu.thesis.model.stagemonitor.Response;
import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.processor.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class HttpQuery {


    public void query() {

        try {
            RequestBuilder rb = new RequestBuilder("");

            String index = "stagemonitor-requests-2016.05.03";
            String type = "/requests";
            String searchPath = index + type + "/_search";
            String settingsPath = index + type + "/_settings";

//            getSettings(rb, settingsPath);
//            putSettings(rb, settingsPath);

            List<String> uniqueRequestIds = getUniqueRequestIds(rb, searchPath);


            for (String requestId : uniqueRequestIds) {
                List<Source> data = getResponseData(rb, searchPath, requestId);

                if (data == null || data.size() == 0) {
                    throw new IllegalStateException("Data is empty");
                }

                Analyzer analyzer = new Analyzer();
                analyzer.addProcessor(
                        new DbQueryCountProcessor(),
                        new DbExecutionTimeProcessor(),
                        new ExecutionTimeProcessor(),
                        new CallingContextTreeSizeProcessor(),
                        new CallingContextTreeDepthProcessor()
                );
                analyzer.processMetrics(data);

//            log(response);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Source> getResponseData(RequestBuilder rb, String searchPath, String requestId) throws IOException {
        String query = getQuery("petclinic_generic.json");
        query = query.replaceFirst("\"requestId\"", "\"" + requestId + "\"");

        ClientResponse clientResponse = rb.resource(searchPath).post(ClientResponse.class, query);
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


    private List<String> getUniqueRequestIds(RequestBuilder rb, String path) throws IOException {

        String payload = getQuery("petclinic_uniqueRequestIds.json");
        ClientResponse clientResponse = rb.resource(path).post(ClientResponse.class, payload);
        String content = getString(clientResponse.getEntityInputStream());
//        log(content);

        ObjectMapper om = new ObjectMapper();
        Aggregation response = om.readValue(content, Aggregation.class);

        List<String> aggregations = new ArrayList<String>();

        for (Bucket bucket : response.getAggregations().getGroupByRequestId().getBuckets()) {
            aggregations.add(bucket.getKey());
        }

        return aggregations;
    }

    private void getSettings(RequestBuilder rb, String settingsPath) {
        ClientResponse settingsGetClientResponse = rb.resource(settingsPath).get(ClientResponse.class);
        String settingsGetContent = getString(settingsGetClientResponse.getEntityInputStream());
        log(settingsGetContent);
    }

    private void putSettings(RequestBuilder rb, String settingsPath) {

        String settingsPayload = getQuery("settings.json");
        ClientResponse settingsPutClientResponse = rb.resource(settingsPath).put(ClientResponse.class, settingsPayload);
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

    private void log(GetResponse getResponse) {
        System.out.println(getResponse.getSourceAsString());
    }
}
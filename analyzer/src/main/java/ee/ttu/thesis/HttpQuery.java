package ee.ttu.thesis;

import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.client.RequestBuilder;
import ee.ttu.thesis.model.Hit;
import ee.ttu.thesis.model.Response;
import ee.ttu.thesis.model.Source;
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
import java.util.List;


/**
 *
 */
public class HttpQuery {


    public void query() {

        try {
            RequestBuilder rb = new RequestBuilder("");

            String index = "stagemonitor-requests-2016.04.29";
            String type = "/requests";
            String path = index + type + "/_search";
            String settingsPath = index + type + "/_search";
//            ClientResponse clientResponse = rb.resource(path).get(ClientResponse.class);


//            ClientResponse settingsGetClientResponse = rb.resource(settingsPath).get(ClientResponse.class);
//            String settingsGetContent = getString(settingsGetClientResponse.getEntityInputStream());
//            log(settingsGetContent);


//            String settingsPayload = getQuery("settings.json");
//            ClientResponse settingsPutClientResponse = rb.resource(settingsPath).put(ClientResponse.class, settingsPayload);
//            String settingsPutContent = getString(settingsPutClientResponse.getEntityInputStream());
//            log(settingsPutContent);


//            String payload = getQuery("containsCallTree.json");
//            String payload = getQuery("groupByHttpMethod.json");
//            String payload = getQuery("groupByHttpMethoddAndUrl.json");
            String payload = getQuery("groupByHttpHeaderValue.json");
            ClientResponse clientResponse = rb.resource(path).post(ClientResponse.class, payload);
            String content = getString(clientResponse.getEntityInputStream());
            log(content);

            ObjectMapper om = new ObjectMapper();
            Response response = om.readValue(content, Response.class);

            if (response != null && response.getHits() != null) {
                List<Hit> hits = response.getHits().getHits();
                log(hits.size());
                for (Hit hit : hits) {
                    log(hit.getId());
                    Source source = hit.getSource();
    //                parseResourcePaths(source.getCallStack());
    //                log(source);
                    log(source.getHeaders().getRequestId());
    //                log(source.getContainsCallTree());
    //                log(source.getCallStack());
    //                writeToFile(source.getId(), source.getCallStack());
                }
            }

//            log(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
        log(String.format("<-------------------------------%-50s-------------------------------->", name));
        log(query);
        log(String.format("</------------------------------%-50s--------------------------------/>", name));
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

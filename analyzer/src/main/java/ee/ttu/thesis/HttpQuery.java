package ee.ttu.thesis;


import com.sun.jersey.api.client.ClientResponse;
import ee.ttu.thesis.client.RequestBuilder;
import ee.ttu.thesis.model.stagemonitor.Source;
import ee.ttu.thesis.transformer.Transformer;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


/**
 * Elastic Search HTTP generic queries
 */
public class HttpQuery {


    final String type = "/requests";
    String currentDate = DateTimeFormat.forPattern("yyyy.MM.dd").print(LocalDateTime.now());
    String index = "stagemonitor-requests-" + currentDate;
//    String index = "stagemonitor-requests-2016.05.06";
    final String searchPath = index + type + "/_search";
    final String settingsPath = index + type + "/_settings";

    private RequestBuilder rb;
    private Transformer transformer;

    /**
     * Singleton
     * Package private for testability
     *
     * @see RequestBuilder
     */
    RequestBuilder getRequestBuilder() {
        if (rb == null) {
            rb = new RequestBuilder("");
        }
        return rb;
    }

    /**
     * Singleton
     * Package private for testability
     *
     * @see Transformer
     */
    Transformer getTransformer() {
        if (transformer == null) {
            transformer = new Transformer();
        }
        return transformer;
    }

    public List<Source> getResponseData(String queryFileName, String requestId, String modificationId) throws IOException {
        String query = getQuery(queryFileName);
        query = query.replaceFirst("\"\\{requestId\\}\"", "\"" + requestId + "\"");
        query = query.replaceFirst("\"\\{modificationId\\}\"", "\"" + modificationId + "\"");
        String jsonResponse = executeQuery(query);
        return getTransformer().transformToResponseData(jsonResponse);
    }

    public Map<String, List<String>> getModificationRequestIds(String queryFileName) throws IOException {
        String payload = getQuery(queryFileName);
        String jsonResponse = executeQuery(payload);
        return new Transformer().transformToModificationRequestId(jsonResponse);
    }

    private String executeQuery(String query) {
        ClientResponse clientResponse = getRequestBuilder().resource(searchPath).post(ClientResponse.class, query);
        String jsonResponse = getString(clientResponse.getEntityInputStream());
//        log(jsonResponse);
        return jsonResponse;
    }

    private void getSettings() {
        ClientResponse settingsGetClientResponse = getRequestBuilder().resource(settingsPath).get(ClientResponse.class);
        String settingsGetContent = getString(settingsGetClientResponse.getEntityInputStream());
        log(settingsGetContent);
    }

    private void putSettings() {
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
                // FIXME hack. Depends on executable name
                String urlString = url.toString();
                if (urlString.contains(".jar")) {
                    urlString = urlString.replace("/analyzer-jar-with-dependencies.jar!", "/classes");
                    urlString = urlString.replace("jar:file:/", "");
                }
                System.out.println(urlString);
                System.out.println(urlString);
                file = new File(urlString);
            } catch (Exception e) {
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

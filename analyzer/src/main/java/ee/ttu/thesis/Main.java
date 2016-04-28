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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class Main {


    public static void main(String[] args) {
        Main main = new Main();
        main.query();
    }

    private void query() {

        try {
            RequestBuilder rb = new RequestBuilder("");

            String index = "stagemonitor-requests-2016.04.28";
            String type = "/requests";
            String path = index + type + "/_search";
            ClientResponse clientResponse = rb.resource(path).get(ClientResponse.class);

//            String c = getContent("http://localhost:9200/" + path);


            ObjectMapper om = new ObjectMapper();

            String content = getString(clientResponse.getEntityInputStream());
//            String content = c;
            Response response = om.readValue(content, Response.class);

            List<Hit> hits = response.getHits().getHits();
            log(hits.size());
            for (Hit hit: hits) {
                Source source = hit.getSource();
                log(hit.getId());
                parseResourcePaths(source.getCallStack());
                log(source);
//                source.getHeaders().
//                log(source.getContainsCallTree());
//                log(source.getCallStack());
//                writeToFile(source.getId(), source.getCallStack());
            }

//            log(content);
//            log(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private String getContent(String uri) {

        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(uri);


            // create a urlconnection object
            HttpURLConnection con = (HttpURLConnection) url.openConnection();;

            con.setRequestMethod("GET");
            con.getHeaderFields().put("Content-Type", Arrays.asList("application/x-www-form-urlencoded", "charset=UTF-8"));

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
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

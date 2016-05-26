import utils.OSValidator;
import utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class Builder {

    private final static String WIN_PREFIX = "cmd.exe /c ";

    public String buildLoadGenerator() {
        try {
            String prefix = getPrefix();

            Process process = Runtime.getRuntime().exec(prefix + "mvn -pl load-generator -am package assembly:single -DskipTests -P load-generator-build-profile");
            return getStandardOutput(process);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String buildDataAnalyzer() {
        try {
            String prefix = getPrefix();

            Process process = Runtime.getRuntime().exec(prefix + "mvn -pl analyzer -am package assembly:single -DskipTests -P data-analyzer-build-profile");
            return getStandardOutput(process);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPrefix() {
        String prefix = OSValidator.isWindows() ? WIN_PREFIX : "";
        prefix += "cd.. & ";
        return prefix;
    }

    private String getStandardOutput(Process process) {
        String lineSeparator = StringUtil.getLineSeparator();
        StringBuilder sb = new StringBuilder();

        BufferedReader input = null;
        String line;
        try {
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
                sb.append(lineSeparator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
import utils.OSValidator;
import utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class AbstractExecutable {
    private final static String WIN_PREFIX = "cmd.exe /c ";

    protected String getPrefix() {
        String prefix = OSValidator.isWindows() ? WIN_PREFIX : "";
        prefix += "cd.. & ";
        return prefix;
    }

    protected String getStandardOutput(Process process) {
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

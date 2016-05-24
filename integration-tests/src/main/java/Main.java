import utils.OSValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class Main {

    private final static String WIN_PREFIX = "cmd.exe /c ";

    public static void main(String[] args) {
        Main main = new Main();
        main.buildJars();
    }

    private void buildJars() {
        try {
            Runtime rt = Runtime.getRuntime();

            String prefix = OSValidator.isWindows() ? WIN_PREFIX : "";

            Process pr1 = rt.exec(prefix + "mvn -pl load-generator -am package assembly:single -DskipTests -P load-generator-build-profile");
            printToConsole(pr1);

            Process pr2 = rt.exec(prefix + "mvn -pl analyzer -am package assembly:single -DskipTests -P data-analyzer-build-profile");
            printToConsole(pr2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printToConsole(Process pr1) throws IOException {
        String line;
        BufferedReader input = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }
}

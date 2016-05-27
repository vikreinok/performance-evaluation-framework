import java.io.IOException;

/**
 *
 */
public class Builder extends AbstractExecutable {

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

}

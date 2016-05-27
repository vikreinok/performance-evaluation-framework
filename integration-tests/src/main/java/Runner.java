import java.io.IOException;

/**
 *
 */
public class Runner extends AbstractExecutable {


    public String runPetClinic() {
        try {
            String prefix = getPrefix();

            Process process = Runtime.getRuntime().exec(prefix + "& cd.. & cd petclinic & mvn clean tomcat7:run");
            return getStandardOutput(process);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

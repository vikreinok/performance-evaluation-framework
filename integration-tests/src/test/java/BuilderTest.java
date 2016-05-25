import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 *
 */
public class BuilderTest {

    Builder builder = null;

    @Before
    public void setUp() throws Exception {
        builder = new Builder();
    }

    @Test
    public void testBuildLoadGenerator() throws Exception {
        String log = builder.buildLoadGenerator();
        assertThat(log, CoreMatchers.containsString("load-generator\\target\\load-generator-jar-with-dependencies.jar"));

    }

    @Test
    public void testBuildDataAnalyzer() throws Exception {
        String log = builder.buildDataAnalyzer();
        assertThat(log, CoreMatchers.containsString("analyzer\\target\\analyzer-jar-with-dependencies.jar"));
    }
}
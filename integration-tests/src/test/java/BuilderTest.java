import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertThat;

/**
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuilderTest {

    Builder builder = null;

    @Before
    public void setUp() throws Exception {
        builder = new Builder();
    }

    @Test
    public void test0_BuildLoadGenerator() throws Exception {
        String log = builder.buildLoadGenerator();
        assertThat(log, CoreMatchers.containsString("load-generator\\target\\load-generator-jar-with-dependencies.jar"));
    }

    @Test
    public void test1_BuildDataAnalyzer() throws Exception {
        String log = builder.buildDataAnalyzer();
        assertThat(log, CoreMatchers.containsString("analyzer\\target\\analyzer-jar-with-dependencies.jar"));
    }


}
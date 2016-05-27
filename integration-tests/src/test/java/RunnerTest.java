import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class RunnerTest {

    Runner runner;

    @Before
    public void setUp() throws Exception {
        runner = new Runner();
    }

    @Test
    public void testRunPetClinic() throws Exception {
        String log = runner.runPetClinic();
        assertNotNull(log);
    }
}
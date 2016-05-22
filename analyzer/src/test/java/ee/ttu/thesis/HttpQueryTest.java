package ee.ttu.thesis;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class HttpQueryTest {

    HttpQuery httpQuery = null;

    @Before
    public void setUp() throws Exception {
        httpQuery = new HttpQuery();
    }

    @Test
    public void testGetQuery() throws Exception {
        final String queryFileName = "petclinic_generic.json";
        String query = httpQuery.getQuery(queryFileName);
        assertTrue(String.format("Could not find query in file: %s", queryFileName), !query.isEmpty());
    }
}
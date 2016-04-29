package ee.ttu.thesis;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class HttpQueryTest {


    HttpQuery  httpQuery = null;

    @Before
    public void setUp() throws Exception {
        httpQuery = new HttpQuery();
    }

    @Test
    public void testGetQuery() throws Exception {
        String query = httpQuery.getQuery("1");
        assertTrue(query, !query.isEmpty());
    }
}
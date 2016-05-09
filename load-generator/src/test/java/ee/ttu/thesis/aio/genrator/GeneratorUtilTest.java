package ee.ttu.thesis.aio.genrator;

import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class GeneratorUtilTest {

    @Test
    public void testGenerateIdentifier() throws Exception {

        final Pattern PATTERN_SSN_EE = Pattern.compile("^[1-6]\\d{2}([0][1-9]|[1][0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])\\d{4}$");

        for (int testCaseNr = 0; testCaseNr < 1000; testCaseNr++) {
            String ssn = GeneratorUtil.generateIdentifier();
            assertTrue(ssn, PATTERN_SSN_EE.matcher(ssn).matches());
        }

    }
}
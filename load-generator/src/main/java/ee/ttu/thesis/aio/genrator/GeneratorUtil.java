package ee.ttu.thesis.aio.genrator;

import java.util.Random;

/**
 *
 */
public class GeneratorUtil {

    public static final String generateEEMsisd() {
        String msisdn = "+37256" + (generateRandomNumber(899999) + 100000);
        logToConsole(String.format("Generated msisdn %s", msisdn));
        return msisdn;
    }

    public static final String generateIdentifier() {

        String centuryAndSex = generateRandomNumber(1) == 1 ? "4" : "3";
        String years = String.format("%02d", generateRandomNumber(105 - 40) + 40);
        String months = String.format("%02d", generateRandomNumber(11) + 1);
        String days = String.format("%02d", generateRandomNumber(27) + 1);
        String randomDigits = String.format("%03d", generateRandomNumber(999));

        String ssn = centuryAndSex + years + months + days + randomDigits;
        ssn += calculateChecksum(ssn);

        logToConsole(String.format("Generated ssn %s", ssn));
        return ssn;
    }

    public static final String generateEmail() {
        String email = String.format("email%06d@email.com", generateRandomNumber(999999));
        logToConsole(String.format("Generated ssn %s", email));
        return email;
    }

    private static int generateRandomNumber(int n) {
        return new Random(System.currentTimeMillis()).nextInt(n);
    }

    private static void logToConsole(String format) {
//        System.out.println(format);
    }


    public static int calculateChecksum(String ssn) {
        String firstTenDigits = ssn.substring(0, 10);
        int[] WEIGHTS_1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
        int[] WEIGHTS_2 = new int[]{3, 4, 5, 6, 7, 8, 9, 1, 2, 3};
        int controlDigit = 0;

        int i;
        for (i = 0; i < 10; ++i) {
            controlDigit += Character.getNumericValue(firstTenDigits.charAt(i)) * WEIGHTS_1[i];
        }
        controlDigit %= 11;
        if (controlDigit == 10) {
            controlDigit = 0;

            for (i = 0; i < 10; ++i) {
                controlDigit += Character.getNumericValue(firstTenDigits.charAt(i)) * WEIGHTS_2[i];
            }

            controlDigit %= 11;
            if (controlDigit == 10) {
                controlDigit = 0;
            }
        }

        return controlDigit;
    }

    public static String generateMsisdn(String countryCode) {
        return countryCode + generateRandomNumber(99999999);
    }
}

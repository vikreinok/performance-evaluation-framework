package ee.ttu.thesis.aio.genrator;

import java.util.Random;

/**
 *
 */
public class GeneratorUtil {

    public static final String generateMsisd() {
//        return "+37256" + (generateRandomNumber(899999) + 100000);
        String msisdn = "56" + (generateRandomNumber(899999) + 100000);
        System.out.println("Generated msisdn " + msisdn);
        return msisdn;
    }


    public static final String generateIdentifier() {
//        SSNValidatorEELT validator = new SSNValidatorEELT();
        String firstPartOfSSN = "3900816";
        boolean illegalSSN = false;
        String randomSSN;
        do {
            randomSSN = firstPartOfSSN + (generateRandomNumber(8999) + 1000);

//            try {
//                validator.validate(randomSSN);
//                illegalSSN = false;
//            } catch (SSNFormatException var4) {
//                illegalSSN = true;
//            }
        } while (illegalSSN);

        System.out.println("Generated ssn " + randomSSN);
        return randomSSN;
    }

    private static int generateRandomNumber(int n) {
        return new Random(System.currentTimeMillis()).nextInt(n);
    }
}

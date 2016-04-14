package ee.ttu.thesis.aio.genrator;

import java.util.Random;

/**
 *
 */
public class GeneratorUtil {

    public static final String generateMsisd() {
        return "56" + (generateRandomNumber(899999) + 100000);
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
        return randomSSN;
    }

    private static int generateRandomNumber(int n) {
        return new Random(System.currentTimeMillis()).nextInt(n);
    }
}

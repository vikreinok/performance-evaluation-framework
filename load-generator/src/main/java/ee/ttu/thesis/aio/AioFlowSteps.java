package ee.ttu.thesis.aio;

/**
 *
 */
public interface AioFlowSteps {

    String PASSWORD_DEFAULT = "Qwerty123";
    String OTP_DEFAULT = "1234";

    String getCountry();

    String getBrand();

    String getLanguage();

    void registration();

    void registerOrAuthenticate(String ssn);

    void viewSSAndDraw();

    void verifySubmitView();

    void selectProduct(String randomProductId);

    String openProductSelectionView();

    void submitApplication();

    void openApplicationForm();

    void logout();

    void login(String username, String password);

    void acceptCases(int nrOfCalls);

}

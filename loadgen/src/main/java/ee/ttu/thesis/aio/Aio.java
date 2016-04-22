package ee.ttu.thesis.aio;

import com.mcbfinance.aio.builder.TestDataBuilder;
import com.mcbfinance.aio.model.CountryCodes;
import com.mcbfinance.aio.model.CustomerCommunicationSettings;
import com.mcbfinance.aio.model.extraservice.ExtraService;
import com.mcbfinance.aio.web.rest.filters.CountryContextFilterFactory;
import com.mcbfinance.aio.web.rest.model.*;
import com.mcbfinance.aio.web.rest.model.creditapp.*;
import com.mcbfinance.aio.web.rest.model.product.ProductWithExtraServicesDTO;
import com.mcbfinance.aio.web.rest.resources.authentication.BanksAuthenticationResourceEE;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import ee.ttu.thesis.RequestBuilder;
import ee.ttu.thesis.aio.genrator.GeneratorUtil;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 */
public class Aio {

    public static final String CONTEXT_PATH = "loanengine/rest/";
    public static final int EXECUTION_COUNT = 1000;

    RequestBuilder rb = null;
    InformationHolder informationHolder = null;

    public static void main(String[] args) {
        Aio aio = new Aio();

        aio.registration();
        for (int executionNumber = 0; executionNumber < EXECUTION_COUNT; executionNumber++) {
            aio.viewSSAndDraw();
        }
    }

    private void viewSSAndDraw() {
        String ssn = informationHolder.getSsn();

        setUp();

        registerOrAuthenticateEESving(ssn);
        generateInvoice();
        String contractId = openSS();
//        changeDueDateInSS();
        clickOnAccountStatemetn(contractId);
        drawSS(contractId);
        acceptCases(1);
        clickOnInvoicesAndContracts(contractId);
        allocate(4000);
        logout();

        informationHolder.print();

        // SS
        //decrease CL
//        rb.get("contracts/26317933504/extra-services/predicted/changeCreditLine?upgrade=false", genericType);
        // click on change preferneces (eelistused)
//        rb.post("credit-application/mmp", genericType, "{\"contractDeliveryMethod\":\"EMAIL\",\"invoiceDeliveryMethod\":\"EMAIL\",\"preDueDateReminderSMS\":\"false\",\"marketingPermission\":\"false\"}"); // {"mmp":0}

    }

    private void registration() {
        setUp();

        registerOrAuthenticateEESving(null);
//        registrationEEC24();
        openApplicationForm();
        submitApplication();
        String randomProductId = productSelectionView();
        productSelection(randomProductId);
        afterApplicationSubmit();
        acceptCases(2);


//        String contractId = openSS();
////        changeDueDateInSS();
//        clickOnAccountStatemetn(contractId);
//        drawSS(contractId);
//        acceptCases(1);
//        clickOnInvoicesAndContracts(contractId);
//        generateInvoice();
////        processLateInvoice(invoiceId);
//        allocate(10000);
        logout();

        informationHolder.print();

        // SS
        //decrease CL
//        rb.get("contracts/26317933504/extra-services/predicted/changeCreditLine?upgrade=false", genericType);
        // click on change preferneces (eelistused)
//        rb.post("credit-application/mmp", genericType, "{\"contractDeliveryMethod\":\"EMAIL\",\"invoiceDeliveryMethod\":\"EMAIL\",\"preDueDateReminderSMS\":\"false\",\"marketingPermission\":\"false\"}"); // {"mmp":0}

    }

    private void setUp() {
        rb = new RequestBuilder(CONTEXT_PATH);
        rb.builder()
                .addHeader("RandomHeader", "randomHeader")
                .addHeader(CountryContextFilterFactory.BRAND_HEADER, "sving")
                .addHeader(CountryContextFilterFactory.LANGUAGE_HEADER, "et")
                .addHeader(CountryContextFilterFactory.COUNTRY_HEADER, "EE")
                .build();

        informationHolder = new InformationHolder();
    }

    private void generateInvoice() {
        rb.resource("developer/invoices/generate").queryParam("paid", "false").put();
    }

    private void allocate(Integer amount) {
//        rb.resource("developer/contracts/pay-now-payment").queryParam("amount", String.valueOf(amount)).put();
        rb.resource("developer/contracts/allocate").queryParam("amount", String.valueOf(amount)).put();
    }

    private void processLateInvoice(String invoiceId) {
        rb.resource("developer/invoices/processLate").queryParam("id", invoiceId).put();
    }

    protected CustomerDTO getCurrentCustomer() {
        return rb.resource("authentication").get(AuthenticationInfoDTO.class).customer;
    }

    protected void logout() {
        rb.resource("authentication").delete();
    }

    private void clickOnInvoicesAndContracts(String contractId) {

        Collection<InvoiceDTO> invoiceDTOs = rb.resource(String.format("contracts/%s/invoices", contractId)).get(new GenericType<Collection<InvoiceDTO>>() {});
        ClientResponse clientResponse = rb.resource(String.format("contracts/%s/document", contractId)).get(ClientResponse.class);

        logToConsole(invoiceDTOs);
        logToConsole(clientResponse);
    }

    private void clickOnAccountStatemetn(String contractId) {
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId)).queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        logToConsole(accountStatementDTO);
    }

    private void drawSS(String contractId) {

        rb.resource(String.format("contracts/%s/draw", contractId)).put(new DrawDTO(4000)); //  204
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId)).get(ContractDTO.class);
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId)).queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {});
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId)).get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {});

        logToConsole(contractDTO);
        logToConsole(accountStatementDTO);
        logToConsole(clProductsProductWithExtraServicesDTOs);
        logToConsole(extraServices);
    }

    private void changeDueDateInSS() {

        // click on change DD
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range").get(DuedateRangeDTO.class);

        MMPDTO mmpdto1 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate));
        MMPDTO mmpdto2 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(1)));
        MMPDTO mmpdto3 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(3)));

        logToConsole(duedateRangeDTO);
        logToConsole(mmpdto1);
        logToConsole(mmpdto2);
        logToConsole(mmpdto3);
    }

    private String openSS() {
        //        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class);
        String contractId = authenticationInfoDTO.customer.contracts.get(0);
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId)).get(ContractDTO.class);

        InvoiceDTO openInvoice = null;
        try {
            openInvoice = rb.resource(String.format("contracts/%s/invoices/open", contractId)).get(InvoiceDTO.class);
        } catch (UniformInterfaceException e) {
            e.getResponse().getStatus(); // if empty then 204
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers").get(new GenericType<Collection<LoanIssuerDTO>>() {});
        Integer outstandingAmount = rb.resource(String.format("contracts/%s/invoices/outstanding", contractId)).get(Integer.class); // 0
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId)).get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {});
        List<DrawSelectionsDTO> drawSelectionsDTOs = rb.resource(String.format("contracts/%s/draw-selections", contractId)).get(new GenericType<List<DrawSelectionsDTO>>() {}); // arrays of json objects
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {});

        informationHolder.setCustomerId(Long.valueOf(authenticationInfoDTO.customer.id));
        informationHolder.setMsisdn(authenticationInfoDTO.customer.msisdn);
        informationHolder.setSsn(authenticationInfoDTO.customer.identifier);
        informationHolder.setOutstandingAmount(outstandingAmount);
        informationHolder.setContractId(Long.valueOf(contractDTO.id));
        informationHolder.setTransactionCount(contractDTO.pendingTransactions.size());

        logToConsole(authenticationInfoDTO);
        logToConsole(contractDTO);
        logToConsole(openInvoice);
        logToConsole(loanIssuerDTOs);
        logToConsole(outstandingAmount);
        logToConsole(extraServices);
        logToConsole(drawSelectionsDTOs);
        logToConsole(clProductsProductWithExtraServicesDTOs);

        return contractId;
    }

    private void registerOrAuthenticateEESving(String ssn) {

        Collection<String> banks = rb.resource("authentication/banks_ee/").get(new GenericType<Collection<String>>() {});

        BankAuthenticationRequestDTO bankAuthenticationRequestDTO = new BankAuthenticationRequestDTO();
        bankAuthenticationRequestDTO.callbackUrl = "https://demo.sving.com/ee/application/";

        IPizzaAuthenticationResponseDTO iPizzaAuthenticationResponseDTO = rb.resource("authentication/banks_ee/dummy_bank_ee_id").post(IPizzaAuthenticationResponseDTO.class, bankAuthenticationRequestDTO);

        if (ssn == null) {
            ssn = GeneratorUtil.generateIdentifier();
        }

        try {
            String postEntity = "CSSN=" + ssn + "&t=" + iPizzaAuthenticationResponseDTO.transactionId;
            rb.removeContentType();

            ClientResponse clientResponse = rb.resource("authentication/banks_ee/dummy_bank_ee_id/confirm")
                    .queryParam(BanksAuthenticationResourceEE.BankAuthenticationResource.TRANSACTION_ID, iPizzaAuthenticationResponseDTO.transactionId)
                    .queryParam(BanksAuthenticationResourceEE.BankAuthenticationResource.CALLBACK_PARAM, bankAuthenticationRequestDTO.callbackUrl)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, postEntity);
            if (clientResponse.getStatus() == 303) {
                rb.addHeader(RequestBuilder.HEADER_NAME_COOKIE, clientResponse.getHeaders().get(RequestBuilder.HEADER_NAME_SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rb.setType(MediaType.APPLICATION_JSON);
        }
        informationHolder.setSsn(ssn);

        logToConsole(banks);
        logToConsole(iPizzaAuthenticationResponseDTO);
    }

    private void acceptCases(int nrOfCalls) {
        // dev resource accept cases
        for (int count = 0; count < nrOfCalls; count++) {
            rb.resource("developer/cases").post(); // 204 no content
        }
    }

    private void afterApplicationSubmit() {
        // after submit
        SubmitApplicationDTO submitApplicationDTO = new SubmitApplicationDTO();
        submitApplicationDTO.firstDrawAmount = 0;
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application/submit").post(CreditApplicationDTO.class, submitApplicationDTO);
        CreditApplicationDTO creditApplicationDTO2 = rb.resource("credit-application").get(CreditApplicationDTO.class);
        // why credit app 2. request

        logToConsole(creditApplicationDTO);
        logToConsole(creditApplicationDTO2);
    }

    private void productSelection(String randomProductId) {
        // submit view
        rb.resource("credit-application/product").put(new ProductSelectionWithFirstDrawtDTO(Long.valueOf(randomProductId), null)); // 204 no response
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range").get(DuedateRangeDTO.class);
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class);

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class);
        PaymentPlanDTO paymentPlanDTO = rb.resource("credit-application/paymentplan").get(PaymentPlanDTO.class);

        // DD popup
        MMPDTO mmpdto1 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate));
        MMPDTO mmpdto2 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(1)));
        MMPDTO mmpdto3 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(3)));

        logToConsole(creditApplicationDTO);
        logToConsole(authenticationInfoDTO);
        logToConsole(paymentPlanDTO);
        logToConsole(mmpdto1);
        logToConsole(mmpdto2);
        logToConsole(mmpdto3);
    }

    private String productSelectionView() {
        // Product selection view

        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class);


        Map<Long, Collection<DrawSelectionsDTO>> drawSections = rb.resource("products/draw-selections").get(new GenericType<Map<Long, Collection<DrawSelectionsDTO>>>() {});  // 200  draw sections
        Map<Long, Collection<DrawSelectionsDTO>> drawSectionsWithMaturity = rb.resource("products/draw-selections-with-maturity").queryParam("maturityPeriod", "36").get(new GenericType<Map<Long, Collection<DrawSelectionsDTO>>>() {});  // 200  draw sections

        Collection<ProductAvailableDTO> installmentProducts = rb.resource("credit-application/product/availability/INSTALLMENT").get(new GenericType<Collection<ProductAvailableDTO>>() {});  // 200   alot of product as a response
        Collection<ProductAvailableDTO> creditLineProducts = rb.resource("credit-application/product/availability/CREDIT_LINE").get(new GenericType<Collection<ProductAvailableDTO>>() {});  // 200   alot of product as a response


        logToConsole(creditApplicationDTO);
        logToConsole(installmentProducts);
        logToConsole(creditLineProducts);
        logToConsole(drawSections);
        logToConsole(drawSectionsWithMaturity);

        if (creditLineProducts.size() > 0) {
            int randomIndex = new Random(System.currentTimeMillis()).nextInt(creditLineProducts.size() - 1);
            ProductAvailableDTO[] productAvailableDTOs = creditLineProducts.toArray(new ProductAvailableDTO[creditLineProducts.size()]);
            ProductAvailableDTO productAvailableDTO = productAvailableDTOs[randomIndex];
            String id = productAvailableDTO.id;
            System.out.printf("Selected product ID %s principal %d%n", id, productAvailableDTO.principal);
            return id;
        }

        throw new IllegalStateException("Could find product from list");
    }

    private void submitApplication() {
        // Submitting the application
        AuthenticationPostOfficeDTO authenticationPostOfficeDTO = new AuthenticationPostOfficeDTO();
        authenticationPostOfficeDTO.authenticationPostOfficeId = 1L;
        rb.resource("credit-application/authpostoffice").put(authenticationPostOfficeDTO);  // 204 no response

        String msisdn = GeneratorUtil.generateMsisd();
        rb.resource("customer/msisdn").put(new MsisdnUpdateDTO(msisdn));  // 204 no response


        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.email = new EmailUpdateDTO(TestDataBuilder.DEFAULT_EMAIL);
        registrationDTO.bankAccount = new BankAccountUpdateDTO("EE361010010050120017");
        registrationDTO.password = new PasswordUpdateDTO(TestDataBuilder.DEFAULT_PASSWORD);
        registrationDTO.msisdnVerification = new MsisdnVerificationDTO(msisdn, TestDataBuilder.TEST_OTP);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.city = "City";
        addressDTO.street = "Street 1";
        addressDTO.postcode = "10000";
        registrationDTO.address = addressDTO;
        registrationDTO.communicationSettings = new CommunicationSettingsDTO(CustomerCommunicationSettings.DeliveryMethod.EMAIL, CustomerCommunicationSettings.DeliveryMethod.EMAIL, true, null);
        registrationDTO.preferredLanguage = CountryCodes.CountryCode.EE.getLocale().getLanguage();

        rb.resource("customer/registration").put(registrationDTO);  // 204 no response


        EstonianFinancialDataDTO estonianFinancialDataDTO = new EstonianFinancialDataDTO();
        estonianFinancialDataDTO.company = "Company";
        estonianFinancialDataDTO.debtTypesExperienced = new EstonianFinancialDataDTO.Loan[0];
        estonianFinancialDataDTO.education = EstonianFinancialDataDTO.Education.UNIVERSITY;
        estonianFinancialDataDTO.employmentDuration = EstonianFinancialDataDTO.EmploymentDuration.MONTHS_5_12;
        estonianFinancialDataDTO.householdChildren = 2;
        estonianFinancialDataDTO.livingCosts = 32300;
        estonianFinancialDataDTO.netIncome = 354000;
        estonianFinancialDataDTO.occupation = EstonianFinancialDataDTO.Occupation.FULL_TIME;
        estonianFinancialDataDTO.occupationType = EstonianFinancialDataDTO.OccupationType.SPECIALIST_OFFICE_WORKER;
        estonianFinancialDataDTO.residence = EstonianFinancialDataDTO.Residence.DORM;
        estonianFinancialDataDTO.maritalStatus = EstonianFinancialDataDTO.MaritalStatus.SINGLE;


//        EstonianFinancialDataDTO estonianFinancialDataDTO = new EstonianFinancialDataDTO();
//        estonianFinancialDataDTO.maritalStatus = EstonianFinancialDataDTO.MaritalStatus.SINGLE;
//        estonianFinancialDataDTO.education = EstonianFinancialDataDTO.Education.SECONDARY;
//        estonianFinancialDataDTO.occupation = EstonianFinancialDataDTO.Occupation.STUDENT;
//        estonianFinancialDataDTO.netIncome = 2540 - 00;
//        estonianFinancialDataDTO.livingCosts = 123 - 00;
//        estonianFinancialDataDTO.residence = EstonianFinancialDataDTO.Residence.WITH_PARENTS;
//        estonianFinancialDataDTO.householdChildren = 1;
//        estonianFinancialDataDTO.debtTypesExperienced = new EstonianFinancialDataDTO.Loan[0];


        rb.resource("credit-application/financialdata").put(estonianFinancialDataDTO); // 204 no response

        informationHolder.setMsisdn(msisdn);
    }

    private void openApplicationForm() {
        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class);
//        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);
        try {
            CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {

        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }
        CreditApplicationDTO creditApplicationPutDTO = rb.resource("credit-application").put(CreditApplicationDTO.class, new NewApplicationDTO());
        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers").get(new GenericType<Collection<LoanIssuerDTO>>() {});
        Collection<PostOfficeDTO> postOfficeDTOs = rb.resource("authentication/ee/postoffices").get(new GenericType<Collection<PostOfficeDTO>>() {});
        try {
            rb.resource("credit-application/previous").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {
//            assertTrue(e.getResponse().getStatus() == 404);
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        informationHolder.setCustomerId(Long.valueOf(authenticationInfoDTO.customer.id));

//        logToConsole(response);
        logToConsole(authenticationInfoDTO);
        logToConsole(creditApplicationPutDTO);
        logToConsole(loanIssuerDTOs.toArray());
        logToConsole(postOfficeDTOs.toArray());
    }

    private void registrationEEC24() {
        UserRegistrationInitDataDTO userRegistrationInitDataDTO = new UserRegistrationInitDataDTO();
        String ssn = GeneratorUtil.generateIdentifier();
        String msisdn = GeneratorUtil.generateMsisd();
        userRegistrationInitDataDTO.ssn = ssn;
        userRegistrationInitDataDTO.msisdn = msisdn;

        UserRegistrationInitResponseDTO response = rb.resource("authentication/user-registration/register-init").post(UserRegistrationInitResponseDTO.class, userRegistrationInitDataDTO);
        UserRegistrationEEDataDTO userRegistrationEEDataDTO = new UserRegistrationEEDataDTO();
        userRegistrationEEDataDTO.ssn = userRegistrationInitDataDTO.ssn;
        userRegistrationEEDataDTO.msisdn = userRegistrationInitDataDTO.msisdn;
        userRegistrationEEDataDTO.firstName = "Ivan";
        userRegistrationEEDataDTO.lastName = "Drago";
        userRegistrationEEDataDTO.otp = TestDataBuilder.TEST_OTP;
        userRegistrationEEDataDTO.email = TestDataBuilder.DEFAULT_EMAIL;
        userRegistrationEEDataDTO.password = TestDataBuilder.DEFAULT_PASSWORD;

        try {
            ClientResponse clientResponse = rb.resource("authentication/user-registration/register-confirm-ee").post(ClientResponse.class, userRegistrationEEDataDTO);
            if (clientResponse.getStatus() == 204) {
                rb.addHeader(RequestBuilder.HEADER_NAME_COOKIE, clientResponse.getHeaders().get(RequestBuilder.HEADER_NAME_SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        informationHolder.setSsn(ssn);
        informationHolder.setMsisdn(msisdn);

//        logToConsole(response);
    }

    public static void logToConsole(Object obj) {
//        System.out.println(ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE));
    }


}

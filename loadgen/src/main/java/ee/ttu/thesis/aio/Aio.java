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
import ee.ttu.thesis.NamedThreadPoolExecutor;
import ee.ttu.thesis.RequestBuilder;
import ee.ttu.thesis.aio.genrator.GeneratorUtil;
import ee.ttu.thesis.aio.model.DomainInformation;
import ee.ttu.thesis.aio.model.RequestInformation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Aio  {

    public static final String CONTEXT_PATH = "loanengine/rest/";
    public static final int EXECUTION_COUNT = 1000;
    public static final int PARRALEL_THREADS = 2;

    protected RequestBuilder rb = null;
    protected DomainInformation domainInformation = null;
    protected RequestInformation requestInformation = new RequestInformation();

    public Aio() {
    }

    public static void main(String[] args) {
        Aio.start();
    }

    public static void start() {

        ThreadPoolExecutor threadPoolExecutor =
                new NamedThreadPoolExecutor(
                        5,
                        100,
                        5000,
                        TimeUnit.MILLISECONDS,
                        "-AioRequestThreadPoolExecutor-"
                );
        for (int index = 0; index < PARRALEL_THREADS; index++) {

            String threadName = "AioRequesterNr_" + index;
            Integer threadIdentifier = index;

            threadPoolExecutor.execute(new AioFlow(threadName, threadIdentifier));
        }
        threadPoolExecutor.shutdown();
    }

    public void registration() {
        setUp();

//        registrationEEC24();
        registerOrAuthenticateEESving(null);
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

        domainInformation.print();

        // SS
        //decrease CL
//        rb.get("contracts/26317933504/extra-services/predicted/changeCreditLine?upgrade=false", genericType);
        // click on change preferneces (eelistused)
//        rb.post("credit-application/mmp", genericType, "{\"contractDeliveryMethod\":\"EMAIL\",\"invoiceDeliveryMethod\":\"EMAIL\",\"preDueDateReminderSMS\":\"false\",\"marketingPermission\":\"false\"}"); // {"mmp":0}

    }

    public void viewSSAndDraw() {
        String ssn = domainInformation.getSsn();

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

        domainInformation.print();

        // SS
        //decrease CL
//        rb.get("contracts/26317933504/extra-services/predicted/changeCreditLine?upgrade=false", genericType);
        // click on change preferneces (eelistused)
//        rb.post("credit-application/mmp", genericType, "{\"contractDeliveryMethod\":\"EMAIL\",\"invoiceDeliveryMethod\":\"EMAIL\",\"preDueDateReminderSMS\":\"false\",\"marketingPermission\":\"false\"}"); // {"mmp":0}

    }

    protected CustomerDTO getCurrentCustomer() {
        return rb.resource("authentication", "00000").get(AuthenticationInfoDTO.class).customer;
    }

    private void setUp() {
        rb = new RequestBuilder(CONTEXT_PATH);
        rb.builder()
                .addHeader("RandomHeader", "randomHeader")
                .addHeader(CountryContextFilterFactory.BRAND_HEADER, "sving")
                .addHeader(CountryContextFilterFactory.LANGUAGE_HEADER, "et")
                .addHeader(CountryContextFilterFactory.COUNTRY_HEADER, "EE")
                .build();

        rb.setRequestInformation(requestInformation);
        domainInformation = new DomainInformation();
    }

    private void generateInvoice() {
        rb.resource("developer/invoices/generate", "12000").queryParam("paid", "false").put();
    }

    private void allocate(Integer amount) {
//        rb.resource("developer/contracts/pay-now-payment").queryParam("amount", String.valueOf(amount)).put();
        rb.resource("developer/contracts/allocate", "12000").queryParam("amount", String.valueOf(amount)).put();
    }

    private void processLateInvoice(String invoiceId) {
        rb.resource("developer/invoices/processLate", "00000").queryParam("id", invoiceId).put();
    }

    protected void logout() {
        rb.resource("authentication", "11000").delete();
    }

    private void clickOnInvoicesAndContracts(String contractId) {

        Collection<InvoiceDTO> invoiceDTOs = rb.resource(String.format("contracts/%s/invoices", contractId), "01010").get(new GenericType<Collection<InvoiceDTO>>() {});
        ClientResponse clientResponse = rb.resource(String.format("contracts/%s/document", contractId), "01020").get(ClientResponse.class);

        logToConsole(invoiceDTOs);
        logToConsole(clientResponse);
    }

    private void clickOnAccountStatemetn(String contractId) {
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId), "00810").queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        logToConsole(accountStatementDTO);
    }

    private void drawSS(String contractId) {

        rb.resource(String.format("contracts/%s/draw", contractId), "00910").put(new DrawDTO(4000)); //  204
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId), "00920").get(ContractDTO.class);
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId), "00930").queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE", "00940").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {});
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId), "00950").get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {});

        logToConsole(contractDTO);
        logToConsole(accountStatementDTO);
        logToConsole(clProductsProductWithExtraServicesDTOs);
        logToConsole(extraServices);
    }

    private void changeDueDateInSS() {

        // click on change DD
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range", "00000").get(DuedateRangeDTO.class);

        MMPDTO mmpdto1 = rb.resource("credit-application/mmp", "00000").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate));
        MMPDTO mmpdto2 = rb.resource("credit-application/mmp", "00000").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(1)));
        MMPDTO mmpdto3 = rb.resource("credit-application/mmp", "00000").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(3)));

        logToConsole(duedateRangeDTO);
        logToConsole(mmpdto1);
        logToConsole(mmpdto2);
        logToConsole(mmpdto3);
    }

    private String openSS() {
        //        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00710").get(AuthenticationInfoDTO.class);
        String contractId = authenticationInfoDTO.customer.contracts.get(0);
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId), "00720").get(ContractDTO.class);

        InvoiceDTO openInvoice = null;
        try {
            openInvoice = rb.resource(String.format("contracts/%s/invoices/open", contractId), "00730").get(InvoiceDTO.class);
        } catch (UniformInterfaceException e) {
            e.getResponse().getStatus(); // if empty then 204
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers", "00740").get(new GenericType<Collection<LoanIssuerDTO>>() {});
        Integer outstandingAmount = rb.resource(String.format("contracts/%s/invoices/outstanding", contractId), "00750").get(Integer.class); // 0
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId), "00760").get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {});
        List<DrawSelectionsDTO> drawSelectionsDTOs = rb.resource(String.format("contracts/%s/draw-selections", contractId), "00770").get(new GenericType<List<DrawSelectionsDTO>>() {}); // arrays of json objects
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE", "00780").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {});

        domainInformation.setCustomerId(Long.valueOf(authenticationInfoDTO.customer.id));
        domainInformation.setMsisdn(authenticationInfoDTO.customer.msisdn);
        domainInformation.setSsn(authenticationInfoDTO.customer.identifier);
        domainInformation.setOutstandingAmount(outstandingAmount);
        domainInformation.setContractId(Long.valueOf(contractDTO.id));
        domainInformation.setTransactionCount(contractDTO.pendingTransactions.size());

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

        Collection<String> banks = rb.resource("authentication/banks_ee/", "00100").get(new GenericType<Collection<String>>() {
        });

        BankAuthenticationRequestDTO bankAuthenticationRequestDTO = new BankAuthenticationRequestDTO();
        bankAuthenticationRequestDTO.callbackUrl = "https://demo.sving.com/ee/application/";

        IPizzaAuthenticationResponseDTO iPizzaAuthenticationResponseDTO = rb.resource("authentication/banks_ee/dummy_bank_ee_id", "00110").post(IPizzaAuthenticationResponseDTO.class, bankAuthenticationRequestDTO);

        if (ssn == null) {
            ssn = GeneratorUtil.generateIdentifier();
        }

        try {
            String postEntity = "CSSN=" + ssn + "&t=" + iPizzaAuthenticationResponseDTO.transactionId;
            rb.removeContentType();

            ClientResponse clientResponse = rb.resource("authentication/banks_ee/dummy_bank_ee_id/confirm", "00000")
                    .queryParam(BanksAuthenticationResourceEE.BankAuthenticationResource.TRANSACTION_ID, iPizzaAuthenticationResponseDTO.transactionId)
                    .queryParam(BanksAuthenticationResourceEE.BankAuthenticationResource.CALLBACK_PARAM, bankAuthenticationRequestDTO.callbackUrl)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .post(ClientResponse.class, postEntity);
            if (clientResponse.getStatus() == 303) {
                rb.addHeader(HttpHeaders.COOKIE, clientResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rb.setType(MediaType.APPLICATION_JSON);
        }
        domainInformation.setSsn(ssn);

        logToConsole(banks);
        logToConsole(iPizzaAuthenticationResponseDTO);
    }

    private void acceptCases(int nrOfCalls) {
        // dev resource accept cases
        for (int count = 0; count < nrOfCalls; count++) {
            rb.resource("developer/cases", "100"+ + nrOfCalls + count).post(); // 204 no content
        }
    }

    private void afterApplicationSubmit() {
        // after submit
        SubmitApplicationDTO submitApplicationDTO = new SubmitApplicationDTO();
        submitApplicationDTO.firstDrawAmount = 0;
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application/submit", "00610").post(CreditApplicationDTO.class, submitApplicationDTO);
        CreditApplicationDTO creditApplicationDTO2 = rb.resource("credit-application", "00620").get(CreditApplicationDTO.class);
        // why credit app 2. request

        logToConsole(creditApplicationDTO);
        logToConsole(creditApplicationDTO2);
    }

    private void productSelection(String randomProductId) {
        // submit view
        rb.resource("credit-application/product", "00510").put(new ProductSelectionWithFirstDrawtDTO(Long.valueOf(randomProductId), null)); // 204 no response
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range", "00520").get(DuedateRangeDTO.class);
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application", "00530").get(CreditApplicationDTO.class);

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00540").get(AuthenticationInfoDTO.class);
        PaymentPlanDTO paymentPlanDTO = rb.resource("credit-application/paymentplan", "00550").get(PaymentPlanDTO.class);

        // DD popup
        MMPDTO mmpdto1 = rb.resource("credit-application/mmp", "00560").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate));
        MMPDTO mmpdto2 = rb.resource("credit-application/mmp", "00563").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(1)));
        MMPDTO mmpdto3 = rb.resource("credit-application/mmp", "00566").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(3)));

        logToConsole(creditApplicationDTO);
        logToConsole(authenticationInfoDTO);
        logToConsole(paymentPlanDTO);
        logToConsole(mmpdto1);
        logToConsole(mmpdto2);
        logToConsole(mmpdto3);
    }

    private String productSelectionView() {
        // Product selection view

        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application", "00410").get(CreditApplicationDTO.class);


        Map<Long, Collection<DrawSelectionsDTO>> drawSections = rb.resource("products/draw-selections", "00420").get(new GenericType<Map<Long, Collection<DrawSelectionsDTO>>>() {});  // 200  draw sections
        Map<Long, Collection<DrawSelectionsDTO>> drawSectionsWithMaturity = rb.resource("products/draw-selections-with-maturity", "00430").queryParam("maturityPeriod", "36").get(new GenericType<Map<Long, Collection<DrawSelectionsDTO>>>() {});  // 200  draw sections

        Collection<ProductAvailableDTO> installmentProducts = rb.resource("credit-application/product/availability/INSTALLMENT", "00440").get(new GenericType<Collection<ProductAvailableDTO>>() {});  // 200   alot of product as a response
        Collection<ProductAvailableDTO> creditLineProducts = rb.resource("credit-application/product/availability/CREDIT_LINE", "00450").get(new GenericType<Collection<ProductAvailableDTO>>() {});  // 200   alot of product as a response


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
            logToConsole(String.format("Selected product ID %s principal %d%n", id, productAvailableDTO.principal));
            return id;
        }

        throw new IllegalStateException("Could find product from list");
    }

    private void submitApplication() {
        // Submitting the application
        AuthenticationPostOfficeDTO authenticationPostOfficeDTO = new AuthenticationPostOfficeDTO();
        authenticationPostOfficeDTO.authenticationPostOfficeId = 1L;
        rb.resource("credit-application/authpostoffice", "00310").put(authenticationPostOfficeDTO);  // 204 no response

        String msisdn = GeneratorUtil.generateMsisd();
        rb.resource("customer/msisdn", "00320").put(new MsisdnUpdateDTO(msisdn));  // 204 no response


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

        rb.resource("customer/registration", "00330").put(registrationDTO);  // 204 no response


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


        rb.resource("credit-application/financialdata", "00340").put(estonianFinancialDataDTO); // 204 no response

        domainInformation.setMsisdn(msisdn);
    }

    private void openApplicationForm() {
        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00210").get(AuthenticationInfoDTO.class);
//        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);
        try {
            CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application", "00220").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {

        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }
        CreditApplicationDTO creditApplicationPutDTO = rb.resource("credit-application", "00230").put(CreditApplicationDTO.class, new NewApplicationDTO());
        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers", "00240").get(new GenericType<Collection<LoanIssuerDTO>>() {});
        Collection<PostOfficeDTO> postOfficeDTOs = rb.resource("authentication/ee/postoffices", "00250").get(new GenericType<Collection<PostOfficeDTO>>() {});
        try {
            rb.resource("credit-application/previous", "00260").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {
//            assertTrue(e.getResponse().getStatus() == 404);
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        domainInformation.setCustomerId(Long.valueOf(authenticationInfoDTO.customer.id));

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

        UserRegistrationInitResponseDTO response = rb.resource("authentication/user-registration/register-init", "00000").post(UserRegistrationInitResponseDTO.class, userRegistrationInitDataDTO);
        UserRegistrationEEDataDTO userRegistrationEEDataDTO = new UserRegistrationEEDataDTO();
        userRegistrationEEDataDTO.ssn = userRegistrationInitDataDTO.ssn;
        userRegistrationEEDataDTO.msisdn = userRegistrationInitDataDTO.msisdn;
        userRegistrationEEDataDTO.firstName = "Ivan";
        userRegistrationEEDataDTO.lastName = "Drago";
        userRegistrationEEDataDTO.otp = TestDataBuilder.TEST_OTP;
        userRegistrationEEDataDTO.email = TestDataBuilder.DEFAULT_EMAIL;
        userRegistrationEEDataDTO.password = TestDataBuilder.DEFAULT_PASSWORD;

        try {
            ClientResponse clientResponse = rb.resource("authentication/user-registration/register-confirm-ee", "00000").post(ClientResponse.class, userRegistrationEEDataDTO);
            if (clientResponse.getStatus() == 204) {
                rb.addHeader(HttpHeaders.COOKIE, clientResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        domainInformation.setSsn(ssn);
        domainInformation.setMsisdn(msisdn);

//        logToConsole(response);
    }

    public static void logToConsole(Object obj) {
//        System.out.println(ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE));
    }

    public RequestInformation getRequestInformation() {
        return requestInformation;
    }
}


class AioFlow implements Runnable {

    private String name;
    private int threadIdentifier;

    public AioFlow(String name, Integer threadIdentifier) {
        this.name = name;
        this.threadIdentifier = threadIdentifier;
    }

    public void run() {
        Aio aio = new Aio();

        RequestInformation requestInformation = aio.getRequestInformation();

        requestInformation.setPeriodNumber(0);
        requestInformation.setThreadIdentifier(threadIdentifier);

        aio.registration();

        try {
            for (int executionNumber = 0; executionNumber < Aio.EXECUTION_COUNT; executionNumber++) {
                requestInformation.setPeriodNumber(executionNumber);
                aio.viewSSAndDraw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
package ee.ttu.thesis.aio;

import com.mcbfinance.aio.builder.TestDataBuilder;
import com.mcbfinance.aio.model.extraservice.ExtraService;
import com.mcbfinance.aio.selfservice.facade.application.CreditApplicationSteps;
import com.mcbfinance.aio.web.rest.filters.CountryContextFilterFactory;
import com.mcbfinance.aio.web.rest.model.*;
import com.mcbfinance.aio.web.rest.model.auth.registration.UserRegistrationDataDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.UserRegistrationInitDataDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.UserRegistrationInitResponseDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.UserRegistrationInitResponseLTDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.lt.UserRegistrationIdentifyDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.lt.UserRegistrationIdentifyResponseDTO;
import com.mcbfinance.aio.web.rest.model.auth.registration.lt.UserRegistrationPaymentDataResponseLTDTO;
import com.mcbfinance.aio.web.rest.model.creditapp.*;
import com.mcbfinance.aio.web.rest.model.product.ProductWithExtraServicesDTO;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import ee.ttu.thesis.RequestBuilder;
import ee.ttu.thesis.aio.genrator.GeneratorUtil;
import ee.ttu.thesis.aio.model.DomainInformation;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 */
public class AioLT extends AbstractAio {

    @Override
    public String getCountry() {
        return "LT";
    }

    @Override
    public String getLanguage() {
        return "lt";
    }

    @Override
    public String getBrand() {
        return "credit24";
    }

    public static void main(String[] args) {
        new AioLT().start(null, "AIO_LT");
    }

    @Override
    public void registration() {
        setUp();

        registerOrAuthenticate(null);
        openApplicationForm();
        submitApplication();
        String randomProductId = openProductSelectionView();
        selectProduct(randomProductId);
        verifySubmitView();
        acceptCases(2);

        logout();

        domainInformation.print();

    }

    @Override
    public void registerOrAuthenticate(String ssn) {
        registerOrAuthenticateLTC24(ssn);
    }

    @Override
    public void viewSSAndDraw() {
//        setUp();

        login(String.valueOf(domainInformation.getCustomerId()), PASSWORD_DEFAULT);
        generateInvoice();
        String contractId = openSS();
//        changeDueDateInSS();
        clickOnAccountStatement(contractId);
        drawSS(contractId);
        acceptCases(1);
        clickOnInvoicesAndContracts(contractId);
        allocate(4000);
        logout();

        domainInformation.print();

    }

    protected CustomerDTO getCurrentCustomer() {
        return rb.resource("authentication", "00000").get(AuthenticationInfoDTO.class).customer;
    }

    private void setUp() {
        rb = new RequestBuilder(CONTEXT_PATH);
        rb.builder()
                .addHeader("RandomHeader", "randomHeader")
                .addHeader(CountryContextFilterFactory.BRAND_HEADER, getBrand())
                .addHeader(CountryContextFilterFactory.LANGUAGE_HEADER, getLanguage())
                .addHeader(CountryContextFilterFactory.COUNTRY_HEADER, getCountry())
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

    private void clickOnInvoicesAndContracts(String contractId) {

        Collection<InvoiceDTO> invoiceDTOs = rb.resource(String.format("contracts/%s/invoices", contractId), "01010").get(new GenericType<Collection<InvoiceDTO>>() {
        });
        ClientResponse clientResponse = rb.resource(String.format("contracts/%s/document", contractId), "01020").get(ClientResponse.class);

        logToConsole(invoiceDTOs);
        logToConsole(clientResponse);
    }

    private void clickOnAccountStatement(String contractId) {
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId), "00810").queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        logToConsole(accountStatementDTO);
    }

    private void drawSS(String contractId) {

        rb.resource(String.format("contracts/%s/draw", contractId), "00910").put(new DrawDTO(4000)); //  204
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId), "00920").get(ContractDTO.class);
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId), "00930").queryParam("from", "2016-01-07").get(AccountStatementDTO.class);
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE", "00940").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {
        });
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId), "00950").get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {
        });

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

        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers", "00740").get(new GenericType<Collection<LoanIssuerDTO>>() {
        });
        Integer outstandingAmount = rb.resource(String.format("contracts/%s/invoices/outstanding", contractId), "00750").get(Integer.class); // 0
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId), "00760").get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {
        });
        List<DrawSelectionsDTO> drawSelectionsDTOs = rb.resource(String.format("contracts/%s/draw-selections", contractId), "00770").get(new GenericType<List<DrawSelectionsDTO>>() {
        }); // arrays of json objects
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE", "00780").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {
        });

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

    private void registerOrAuthenticateLTC24(String ssn) {

//        try {
//            AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00100").get(AuthenticationInfoDTO.class);
//        } catch (UniformInterfaceException e) {
//            e.getResponse().getStatus(); // 401
//        } catch (ClientHandlerException e) {
//            e.printStackTrace();
//        }

        Collection<String> banks = rb.resource("authentication/user-registration/list-banks-lt", "00110").get(new GenericType<Collection<String>>() {
        });


        if (ssn == null) {
            ssn = GeneratorUtil.generateIdentifier();
        }

        String email = GeneratorUtil.generateEmail();
        String msisdn = GeneratorUtil.generateMsisdn("+370");



        UserRegistrationInitDataDTO requestInitDto = new UserRegistrationInitDataDTO();
        requestInitDto.ssn = ssn;
        requestInitDto.password = PASSWORD_DEFAULT;
        requestInitDto.msisdn = msisdn;

        UserRegistrationInitResponseLTDTO responseInitDto = rb.resource("authentication/user-registration/register-init-lt-c24", "00120").post(UserRegistrationInitResponseLTDTO.class, requestInitDto);


        UserRegistrationDataDTO requestConfirmDto = new UserRegistrationDataDTO();
        requestConfirmDto.bankName = banks.iterator().next();
        requestConfirmDto.email = email;
        requestConfirmDto.ssn = ssn;
        requestConfirmDto.msisdn = msisdn;
        requestConfirmDto.password = PASSWORD_DEFAULT;
        requestConfirmDto.bankReturnUrl = "https://dt01.credit24.com/lt/application/";
        requestConfirmDto.otp = OTP_DEFAULT;
        requestConfirmDto.consent = true;
        requestConfirmDto.politicallyExposedPerson = false;
        requestConfirmDto.dataProcessingConsent = true;


        try {

            ClientResponse clientResponse =  rb.resource("authentication/user-registration/register-confirm-lt-c24", "00130")
                     .post(ClientResponse.class, requestConfirmDto);

            if (clientResponse.getStatus() == 200) {
                rb.addHeader(HttpHeaders.COOKIE, clientResponse.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rb.setType(MediaType.APPLICATION_JSON);
        }

        AuthenticationInfoDTO authenticationInfoDTO2 = rb.resource("authentication", "00140").get(AuthenticationInfoDTO.class);


        String response  = rb.resource("authentication/banks_lt/one-cent-payment-status-lt", "00150").queryParam("customer_id", authenticationInfoDTO2.customer.id).get(String.class);

        UserRegistrationPaymentDataResponseLTDTO iPizzaAuthenticationResponseDTO = rb.resource("authentication/user-registration/get-payment-data", "00160").get(UserRegistrationPaymentDataResponseLTDTO.class);

        Collection<String> banks2 = rb.resource("authentication/user-registration/list-banks-lt", "00170").get(new GenericType<Collection<String>>() {
        });

        domainInformation.setCustomerId(Long.valueOf(authenticationInfoDTO2.customer.id));
        domainInformation.setSsn(ssn);
        domainInformation.setMsisdn(msisdn);

        UserRegistrationIdentifyDTO userRegistrationIdentifyDTO = new UserRegistrationIdentifyDTO();
        userRegistrationIdentifyDTO.id = domainInformation.getCustomerId();
        userRegistrationIdentifyDTO.identifier = domainInformation.getSsn();

        UserRegistrationIdentifyResponseDTO userRegistrationIdentifyResponseDTO = rb.resource("authentication/user-registration/identify-demo-lt-c24", "00180").post(UserRegistrationIdentifyResponseDTO.class, userRegistrationIdentifyDTO);


//        rb.resource("developer/cases/user-registration", "00175").queryParam("ssn", domainInformation.getSsn()).post(); // 204 no content


        logToConsole(banks);
        logToConsole(authenticationInfoDTO2);
        logToConsole(iPizzaAuthenticationResponseDTO);
        logToConsole(userRegistrationIdentifyResponseDTO);
    }

    @Override
    public void verifySubmitView() {
        // after submit
        SubmitApplicationDTO submitApplicationDTO = new SubmitApplicationDTO();
        submitApplicationDTO.firstDrawAmount = 0;
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application/submit", "00610").post(CreditApplicationDTO.class, submitApplicationDTO);
        CreditApplicationDTO creditApplicationDTO2 = rb.resource("credit-application", "00620").get(CreditApplicationDTO.class);
        // why credit app 2. request

        rb.resource("developer/creditApplications/requestBka", "00630").post(); // 204 no content

        logToConsole(creditApplicationDTO);
        logToConsole(creditApplicationDTO2);
    }

    @Override
    public void selectProduct(String randomProductId) {
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

    @Override
    public String openProductSelectionView() {

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00400").get(AuthenticationInfoDTO.class);

        List<ClosedContractDTO> closedContractDTOs = rb.resource("customer-lt/closed-contracts", "00410").get((new GenericType<List<ClosedContractDTO>>() {
        }));

        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application", "00410").get(CreditApplicationDTO.class);

        Collection<ProductAvailableDTO> installmentProducts = rb.resource("credit-application/product/availability/INSTALLMENT", "00430").get(new GenericType<Collection<ProductAvailableDTO>>() {
        });  // 200   alot of product as a response
        Collection<ProductAvailableDTO> creditLineProducts = rb.resource("credit-application/product/availability/CREDIT_LINE", "00440").get(new GenericType<Collection<ProductAvailableDTO>>() {
        });  // 200   alot of product as a response

        Map<Long, Collection<DrawSelectionsDTO>> drawSectionsWithMaturity = rb.resource("products/draw-selections-with-maturity", "00450").queryParam("maturityPeriod", "36").get(new GenericType<Map<Long, Collection<DrawSelectionsDTO>>>() {
        });  // 200  draw sections

        logToConsole(authenticationInfoDTO);
        logToConsole(closedContractDTOs);
        logToConsole(creditApplicationDTO);
        logToConsole(installmentProducts);
        logToConsole(creditLineProducts);
        logToConsole(drawSectionsWithMaturity);

        if (creditLineProducts.size() > 0) {
            int randomIndex = new Random(System.currentTimeMillis()).nextInt(creditLineProducts.size());
            ProductAvailableDTO[] productAvailableDTOs = creditLineProducts.toArray(new ProductAvailableDTO[creditLineProducts.size()]);
            ProductAvailableDTO productAvailableDTO = productAvailableDTOs[randomIndex];
            String id = productAvailableDTO.id;
            logToConsole(String.format("Selected product ID %s principal %d%n", id, productAvailableDTO.principal));
            return id;
        }

        throw new IllegalStateException("Could find product from list");
    }

    @Override
    public void submitApplication() {

        LegalDocumentDTO legalDocumentDTO = new LegalDocumentDTO();
        legalDocumentDTO.refinanceRequest = true;
        legalDocumentDTO.customerFlowStep = CreditApplicationSteps.CustomerFlowStep.FINANCIAL_DATA.name();

        rb.resource("credit-application-lt/update-permissions", "00310").post(ClientResponse.class, legalDocumentDTO);


        LegalDocumentDTO legalDocumentDTO2 = new LegalDocumentDTO();
        legalDocumentDTO.refinanceRequest = true;
        legalDocumentDTO.customerFlowStep = CreditApplicationSteps.CustomerFlowStep.FINANCIAL_DATA.name();

        rb.resource("credit-application-lt/update-permissions", "00320").post(ClientResponse.class, legalDocumentDTO2);


        LithuanianFinancialDataDTO ltFinancialDto = new LithuanianFinancialDataDTO();
        ltFinancialDto.company = "Company";
        ltFinancialDto.education = LithuanianFinancialDataDTO.Education.UNIVERSITY;
        ltFinancialDto.employmentDuration = LithuanianFinancialDataDTO.EmploymentDuration.FROM_4_TO_12_MONTHS;
        ltFinancialDto.livingAddress = "second address 2";
        ltFinancialDto.monthlyObligations = 22200;
        ltFinancialDto.netIncome = 150000;
        ltFinancialDto.occupation = LithuanianFinancialDataDTO.Occupation.PART_TIME;
        ltFinancialDto.receiveRefinance = true;
        ltFinancialDto.residence = LithuanianFinancialDataDTO.Residence.OWN;

        rb.resource("credit-application/financialdata", "00340").put(ltFinancialDto); // 204 no response

    }

    @Override
    public void openApplicationForm() {
        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication", "00210").get(AuthenticationInfoDTO.class);
        List<ClosedContractDTO> closedContractDTOs = rb.resource("customer-lt/closed-contracts", "00220").get((new GenericType<List<ClosedContractDTO>>() {
        }));

        try {
            CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application", "00220").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {
            //404
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        CreditApplicationDTO creditApplicationPutDTO = rb.resource("credit-application", "00230").put(CreditApplicationDTO.class);


        try {
            rb.resource("credit-application/previous", "00260").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {
//            assertTrue(e.getResponse().getStatus() == 404);
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        domainInformation.setCustomerId(Long.valueOf(authenticationInfoDTO.customer.id));

        logToConsole(authenticationInfoDTO);
        logToConsole(closedContractDTOs);
        logToConsole(creditApplicationPutDTO);

    }

    private void registrationEEC24() {
        UserRegistrationInitDataDTO userRegistrationInitDataDTO = new UserRegistrationInitDataDTO();
        String ssn = GeneratorUtil.generateIdentifier();
        String msisdn = GeneratorUtil.generateEEMsisd();
        userRegistrationInitDataDTO.ssn = ssn;
        userRegistrationInitDataDTO.msisdn = msisdn;

        UserRegistrationInitResponseDTO response = rb.resource("authentication/user-registration/register-init", "00000").post(UserRegistrationInitResponseDTO.class, userRegistrationInitDataDTO);
        UserRegistrationDataDTO userRegistrationEEDataDTO = new UserRegistrationDataDTO();
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

}

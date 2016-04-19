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
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 */
public class Aio {


    public static void main(String[] args) {
        Aio aio = new Aio();
        aio.start();
    }

    private void start() {
        RequestBuilder rb = new RequestBuilder("loanengine/rest/");
        rb.addHeader("Host", "demo.sving.com");
        rb.addHeader(CountryContextFilterFactory.BRAND_HEADER, "sving");
        rb.addHeader(CountryContextFilterFactory.COUNTRY_HEADER, "EE");
        rb.addHeader(CountryContextFilterFactory.LANGUAGE_HEADER, "et");

        final GenericType<Collection<String>> genericType = new GenericType<Collection<String>>() {
        };
//        registrationEESving(rb);
        registrationEEC24(rb);
        openApplicationForm(rb);
        submitApplication(rb);
        String randomProductId = productSelectionView(rb);
        productSelection(rb, randomProductId);
        afterApplicationSubmit(rb);
        acceptCases(rb, 2);
        String contractId = openSS(rb);
//        changeDueDateInSS(rb, contractId);
        clickOnAccountStatemetn(rb, contractId);
        drawSS(rb, contractId);
        acceptCases(rb, 1);
        clickOnInvoicesAndContracts(rb, contractId);


        generateInvoice(rb);

        if (true) {
            return;
        }



        // SS
        //decrease CL
//        rb.get("contracts/26317933504/extra-services/predicted/changeCreditLine?upgrade=false", genericType); // [{"product":{"id":"8000085","name":"Bronze Plus","openingFee":0,"principal":125000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}},{"product":{"id":"8000084","name":"Bronze","openingFee":0,"principal":100000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}},{"product":{"id":"8000083","name":"Basic Plus","openingFee":0,"principal":75000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}},{"product":{"id":"8000082","name":"Basic","openingFee":0,"principal":50000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}},{"product":{"id":"8000081","name":"Small Plus","openingFee":0,"principal":25000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}},{"product":{"id":"8000080","name":"Small","openingFee":0,"principal":10000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"predictedES":{"price":2000,"mmp":1000,"dueDate":"2016-05-09"}}]

        // click on change preferneces (eelistused)
//        rb.post("credit-application/mmp", genericType, "{\"contractDeliveryMethod\":\"EMAIL\",\"invoiceDeliveryMethod\":\"EMAIL\",\"preDueDateReminderSMS\":\"false\",\"marketingPermission\":\"false\"}"); // {"mmp":0}


    }

    private void generateInvoice(RequestBuilder rb) {
         rb.resource("developer/invoices/generate").queryParam("paid", "true").put();
    }

    private void processLateInvoice(RequestBuilder rb, String invoiceId) {
         rb.resource("developer/invoices/processLate").queryParam("id", invoiceId).put();
    }

    private void clickOnInvoicesAndContracts(RequestBuilder rb, String contractId) {

        Collection<InvoiceDTO> invoiceDTOs = rb.resource(String.format("contracts/%s/invoices", contractId)).get(new GenericType<Collection<InvoiceDTO>>() {});  // []
        ClientResponse clientResponse =  rb.resource(String.format("contracts/%s/document", contractId)).get(ClientResponse.class);   // []

        logToConsole(invoiceDTOs);
        logToConsole(clientResponse);
    }

    private void clickOnAccountStatemetn(RequestBuilder rb, String contractId) {
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId)).queryParam("from", "2016-01-07").get(AccountStatementDTO.class); // {"openBalance":0,"transactions":[]}
        logToConsole(accountStatementDTO);
    }

    private void drawSS(RequestBuilder rb, String contractId) {

        rb.resource(String.format("contracts/%s/draw", contractId)).put(new DrawDTO(10000)); //  204
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId)).get(ContractDTO.class); // {"id":"26317933504","product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"availableLimit":75000,"drawdowns":1,"nextInvoicingDate":"2016-04-22","creditApplication":{"id":154219795540002201,"product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"state":"APPROVED","firstDueDate":null,"documentRequirements":[],"financialData":{"occupation":"FULL_TIME","netIncome":"247000","employmentDuration":"MONTHS_1_4","residence":"DORM","occupationType":"MIDDLE_MANAGER","livingCosts":"25000","company":"Acme Inc.","householdChildren":"1","education":"SECONDARY","maritalStatus":"MARRIED"},"firstDrawAmount":0,"authenticationPostOfficeDTO":{"authenticationPostOfficeId":16},"channel":"Web"},"drawBlockedReason":"DRAW_PENDING","documentId":"148492056060004559","pendingTransactions":[{"created":"2016-04-07","amount":75000,"type":"PendingDraw","description":"2016-04-07"}],"mmp":0,"totalAmount":0,"referenceNumber":"10215095502"}
        AccountStatementDTO accountStatementDTO = rb.resource(String.format("contracts/%s/account-statement", contractId)).queryParam("from", "2016-01-07").get(AccountStatementDTO.class); // {"openBalance":0,"transactions":[]}
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {});  // [{"id":"8000080","name":"Small","openingFee":0,"principal":10000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":13273,"totalInterest":3273,"fullMaturityMmp":1000,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000081","name":"Small Plus","openingFee":0,"principal":25000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":49082,"totalInterest":24082,"fullMaturityMmp":1361,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000082","name":"Basic","openingFee":0,"principal":50000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":98144,"totalInterest":48144,"fullMaturityMmp":2722,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000083","name":"Basic Plus","openingFee":0,"principal":75000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":147221,"totalInterest":72221,"fullMaturityMmp":4083,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000084","name":"Bronze","openingFee":0,"principal":100000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":196257,"totalInterest":96257,"fullMaturityMmp":5445,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000085","name":"Bronze Plus","openingFee":0,"principal":125000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":245327,"totalInterest":120327,"fullMaturityMmp":6806,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":294401,"totalInterest":144401,"fullMaturityMmp":8167,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000087","name":"Gold","openingFee":0,"principal":200000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":392541,"totalInterest":192541,"fullMaturityMmp":10889,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000088","name":"Gold Plus","openingFee":0,"principal":250000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":490651,"totalInterest":240651,"fullMaturityMmp":13612,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000089","name":"Platinum","openingFee":0,"principal":300000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":588801,"totalInterest":288801,"fullMaturityMmp":16334,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]}]
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId)).get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {}); // {"PAYMENT_HOLIDAY":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false},"CHANGE_DUE_DATE":{"enabled":false,"price":500,"freeOnesLeft":0,"isAvailable":true,"isVisible":true},"UPGRADE_CREDITLINE":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false},"DOWNGRADE_CREDITLINE":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false},"EVENGRADE_CREDITLINE":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false}}

        logToConsole(contractDTO);
        logToConsole(accountStatementDTO);
        logToConsole(clProductsProductWithExtraServicesDTOs);
        logToConsole(extraServices);
    }

    private void changeDueDateInSS(RequestBuilder rb, String contractId) {

        // click on change DD
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range").get(DuedateRangeDTO.class); // {"minDate":"2016-05-08","defaultDate":"2016-05-07","maxDate":"2016-06-07"}

        MMPDTO mmpdto1 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate)); // {"mmp":0}
        MMPDTO mmpdto2 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(1))); // {"mmp":0}
        MMPDTO mmpdto3 = rb.resource("credit-application/mmp").post(MMPDTO.class, new DueDateSelectionDTO(duedateRangeDTO.defaultDate.plusDays(3))); // {"mmp":0}

        logToConsole(duedateRangeDTO);
        logToConsole(mmpdto1);
        logToConsole(mmpdto2);
        logToConsole(mmpdto3);
    }

    private String openSS(RequestBuilder rb) {
        //        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class); // {"customer":{"firstName":"Firstname","lastName":"Lastname","secondLastName":null,"dateOfBirth":null,"countryOfBirth":null,"id":"1509550","email":"test@test.ee","msisdn":"+37256252830","msisdn2":null,"identifier":"39008160326","address":{"street":"Kase 8","postcode":"20606","city":"Narva","province":"Ida-Viru maakond","floor":"","door":""},"bankAccount":"EE361010010050120017","msisdnPending":null,"emailPending":"test@test.ee","canUsePassword":true,"contracts":["26317933504"],"customerNumber":"1509550","communicationSettings":{"invoiceDeliveryMethod":"EMAIL","contractDeliveryMethod":"EMAIL","marketingPermission":false,"preDueDateReminderSMS":false},"communicationSettingsPL":{"dataProcessing":null,"dataProtectionAdministrator":null,"marketingConsent1":null,"marketingConsent2":null,"marketingConsent3":null,"instantor":null,"creditReliabilityVerification":null,"powerOfAttorney":null,"termsOfServiceViaWebsite":null,"customerFlowStep":null,"marketingPermissionPaper":null,"marketingPermissionPhone":null,"marketingPermissionEmailOrSms":null},"isInCollection":false,"collectionDate":null,"bank":null,"preferredLanguage":"et","isOldCustomer":false,"isCLAllowed":false,"isInPendingDrawState":false,"bankAccountVerified":true,"customerRegistered":true,"greetingsName":"Firstname","gender":"MALE","identificationState":"PENDING_IDENTIFICATION","clallowed":false},"authenticationMethod":"DEFAULT"}
        String contractId = authenticationInfoDTO.customer.contracts.get(0);
        ContractDTO contractDTO = rb.resource(String.format("contracts/%s", contractId)).get(ContractDTO.class); //{"id":"26317933504","product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"availableLimit":150000,"drawdowns":0,"nextInvoicingDate":"2016-04-22","creditApplication":{"id":154219795540002201,"product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"state":"APPROVED","firstDueDate":null,"documentRequirements":[],"financialData":{"occupation":"FULL_TIME","netIncome":"247000","employmentDuration":"MONTHS_1_4","residence":"DORM","occupationType":"MIDDLE_MANAGER","livingCosts":"25000","company":"Acme Inc.","householdChildren":"1","education":"SECONDARY","maritalStatus":"MARRIED"},"firstDrawAmount":0,"authenticationPostOfficeDTO":{"authenticationPostOfficeId":16},"channel":"Web"},"drawBlockedReason":null,"documentId":"148492056060004559","pendingTransactions":[],"mmp":0,"totalAmount":0,"referenceNumber":"10215095502"}

        InvoiceDTO openInvoice = null;
        try {
            openInvoice = rb.resource(String.format("contracts/%s/invoices/open", contractId)).get(InvoiceDTO.class);
        } catch (UniformInterfaceException e) {
            e.getResponse().getStatus(); // if empty then 204
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers").get(new GenericType<Collection<LoanIssuerDTO>>() {}); // [{"key":"employer","name":"Tööandja"},{"key":"privatePerson","name":"Eraisik"},{"key":"other","name":"Keegi muu"},{"key":"swedbank","name":"Swedbank"},{"key":"sebPank","name":"SEB pank"},{"key":"danskePank","name":"Danske pank"},{"key":"nordeaPank","name":"Nordea Pank"},{"key":"parexPank","name":"Parex pank"},{"key":"dnbNord","name":"DnB Nord"},{"key":"krediidipank","name":"Krediidipank"},{"key":"bigbank","name":"BIGBANK"},{"key":"amCredit","name":"AmCredit"},{"key":"autokiirlaen","name":"Autokiirlaen"},{"key":"creditmarket","name":"Creditmarket"},{"key":"ferratum","name":"Ferratum"},{"key":"krediidikassa","name":"Krediidikassa"},{"key":"liisiJarelmaks","name":"Liisi järelmaks"},{"key":"monetti","name":"Monetti"},{"key":"raha24","name":"Raha24"},{"key":"smsLaen","name":"SMS laen"},{"key":"smsMoney","name":"SMS Money"},{"key":"timeInvest","name":"Time Invest"}]
        Integer outstandingAmount = rb.resource(String.format("contracts/%s/invoices/outstanding", contractId)).get(Integer.class); // 0
        Map<ExtraService, ContractExtraServiceDTO> extraServices = rb.resource(String.format("contracts/%s/extra-services", contractId)).get(new GenericType<Map<ExtraService, ContractExtraServiceDTO>>() {}); // {"PAYMENT_HOLIDAY":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false},"CHANGE_DUE_DATE":{"enabled":false,"price":500,"freeOnesLeft":0,"isAvailable":true,"isVisible":true},"UPGRADE_CREDITLINE":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false},"DOWNGRADE_CREDITLINE":{"enabled":false,"price":2000,"freeOnesLeft":0,"isAvailable":true,"isVisible":true},"EVENGRADE_CREDITLINE":{"enabled":null,"price":null,"freeOnesLeft":null,"isAvailable":false,"isVisible":false}}
        List<DrawSelectionsDTO> drawSelectionsDTOs = rb.resource(String.format("contracts/%s/draw-selections", contractId)).get(new GenericType<List<DrawSelectionsDTO>>() {}); // arrays of json objects
        Collection<ProductWithExtraServicesDTO> clProductsProductWithExtraServicesDTOs = rb.resource("products/CREDIT_LINE").get(new GenericType<Collection<ProductWithExtraServicesDTO>>() {
        }); // arrays of json objects// [{"id":"8000080","name":"Small","openingFee":0,"principal":10000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":13273,"totalInterest":3273,"fullMaturityMmp":1000,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000081","name":"Small Plus","openingFee":0,"principal":25000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":49082,"totalInterest":24082,"fullMaturityMmp":1361,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000082","name":"Basic","openingFee":0,"principal":50000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":98144,"totalInterest":48144,"fullMaturityMmp":2722,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000083","name":"Basic Plus","openingFee":0,"principal":75000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":147221,"totalInterest":72221,"fullMaturityMmp":4083,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000084","name":"Bronze","openingFee":0,"principal":100000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":196257,"totalInterest":96257,"fullMaturityMmp":5445,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000085","name":"Bronze Plus","openingFee":0,"principal":125000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":245327,"totalInterest":120327,"fullMaturityMmp":6806,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":294401,"totalInterest":144401,"fullMaturityMmp":8167,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000087","name":"Gold","openingFee":0,"principal":200000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":392541,"totalInterest":192541,"fullMaturityMmp":10889,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000088","name":"Gold Plus","openingFee":0,"principal":250000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":490651,"totalInterest":240651,"fullMaturityMmp":13612,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]},{"id":"8000089","name":"Platinum","openingFee":0,"principal":300000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT","totalPayment":588801,"totalInterest":288801,"fullMaturityMmp":16334,"higherPriceProduct":null,"extraServices":[{"id":"PAYMENT_HOLIDAY","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"CHANGE_DUE_DATE","price":500,"type":"COUNTED_USE","freeAmount":0},{"id":"UPGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"DOWNGRADE_CREDITLINE","price":2000,"type":"COUNTED_USE","freeAmount":0},{"id":"EVENGRADE_CREDITLINE","price":0,"type":"COUNTED_USE","freeAmount":0},{"id":"UPSELL_INSTALLMENT","price":0,"type":"COUNTED_USE","freeAmount":0}]}]

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

    private void registrationEESving(RequestBuilder rb) {


        Collection<String> banks = rb.resource("authentication/banks_ee/").get(new GenericType<Collection<String>>() {});

        BankAuthenticationRequestDTO bankAuthenticationRequestDTO = new BankAuthenticationRequestDTO();
        bankAuthenticationRequestDTO.callbackUrl = "https://demo.sving.com/ee/application/";

        IPizzaAuthenticationResponseDTO iPizzaAuthenticationResponseDTO = rb.resource("authentication/banks_ee/dummy_bank_ee_id").post(IPizzaAuthenticationResponseDTO.class, bankAuthenticationRequestDTO);

        try {
            String ssn = GeneratorUtil.generateIdentifier();
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

        logToConsole(banks);
        logToConsole(iPizzaAuthenticationResponseDTO);
    }

    private void acceptCases(RequestBuilder rb, int nrOfCalls) {
        // dev resource accept cases
        for (int count = 0; count < nrOfCalls; count++) {
            rb.resource("developer/cases").post(); // 204 no content
        }
    }

    private void afterApplicationSubmit(RequestBuilder rb) {
        // after submit
        SubmitApplicationDTO submitApplicationDTO = new SubmitApplicationDTO();
        submitApplicationDTO.firstDrawAmount = 0;
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application/submit").post(CreditApplicationDTO.class, submitApplicationDTO); // {"id":154219795540002201,"product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"state":"PENDING","firstDueDate":null,"documentRequirements":[],"financialData":{"occupation":"FULL_TIME","netIncome":"247000","residence":"DORM","employmentDuration":"MONTHS_1_4","occupationType":"MIDDLE_MANAGER","livingCosts":"25000","company":"Acme Inc.","householdChildren":"1","maritalStatus":"MARRIED","education":"SECONDARY"},"firstDrawAmount":0,"authenticationPostOfficeDTO":{"authenticationPostOfficeId":16},"channel":"Web"}
        CreditApplicationDTO creditApplicationDTO2 = rb.resource("credit-application").get(CreditApplicationDTO.class); // {"id":154219795540002201,"product":{"id":"8000086","name":"Silver","openingFee":0,"principal":150000,"invoicingFee":0,"minimumWithdrawalFee":0,"drawFeePercentage":2.99,"interest":4.15,"annualInterest":49.800000000000004,"apr":64.0,"maturityPeriods":36,"maturityPeriodLength":"ONE_MONTH","type":"CREDIT_LINE","withdrawalFixedFee":0,"brand":"CREDIT24","groupName":"DEFAULT"},"state":"PENDING","firstDueDate":null,"documentRequirements":[],"financialData":{"occupation":"FULL_TIME","netIncome":"247000","residence":"DORM","employmentDuration":"MONTHS_1_4","occupationType":"MIDDLE_MANAGER","livingCosts":"25000","company":"Acme Inc.","householdChildren":"1","maritalStatus":"MARRIED","education":"SECONDARY"},"firstDrawAmount":0,"authenticationPostOfficeDTO":{"authenticationPostOfficeId":16},"channel":"Web"}
        // why credit app 2. request

        logToConsole(creditApplicationDTO);
        logToConsole(creditApplicationDTO2);
    }

    private void productSelection(RequestBuilder rb, String randomProductId) {
        // submit view
        rb.resource("credit-application/product").put(new ProductSelectionWithFirstDrawtDTO(Long.valueOf(randomProductId), null)); // 204 no response
        DuedateRangeDTO duedateRangeDTO = rb.resource("credit-application/duedate/range").get(DuedateRangeDTO.class); // {"minDate":"2016-04-22","defaultDate":"2016-05-07","maxDate":"2016-05-21"}
        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class); //

        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class); // {"customer":{"firstName":"Firstname","lastName":"Lastname","secondLastName":null,"dateOfBirth":null,"countryOfBirth":null,"id":"1509550","email":null,"msisdn":"+37256252830","msisdn2":null,"identifier":"39008160326","address":{"street":"Kase 8","postcode":"20606","city":"Narva","province":"Ida-Viru maakond","floor":"","door":""},"bankAccount":"EE361010010050120017","msisdnPending":null,"emailPending":"test@test.ee","canUsePassword":true,"contracts":[],"customerNumber":"1509550","communicationSettings":{"invoiceDeliveryMethod":"EMAIL","contractDeliveryMethod":"EMAIL","marketingPermission":false,"preDueDateReminderSMS":false},"communicationSettingsPL":{"dataProcessing":null,"dataProtectionAdministrator":null,"marketingConsent1":null,"marketingConsent2":null,"marketingConsent3":null,"instantor":null,"creditReliabilityVerification":null,"powerOfAttorney":null,"termsOfServiceViaWebsite":null,"customerFlowStep":null,"marketingPermissionPaper":null,"marketingPermissionPhone":null,"marketingPermissionEmailOrSms":null},"isInCollection":false,"collectionDate":null,"bank":null,"preferredLanguage":"et","isOldCustomer":false,"isCLAllowed":false,"isInPendingDrawState":false,"bankAccountVerified":false,"customerRegistered":false,"greetingsName":"Firstname","gender":"MALE","identificationState":"NOT_STARTED_YET","clallowed":false},"authenticationMethod":"DEFAULT"}
        PaymentPlanDTO paymentPlanDTO = rb.resource("credit-application/paymentplan").get(PaymentPlanDTO.class); // {"paymentPlan":[{"date":"2016-05-09","principalLeft":150000,"amortisation":1532,"allocatedFees":0,"allocatedInterest":6640,"mmp":8172},{"date":"2016-06-07","principalLeft":148468,"amortisation":2216,"allocatedFees":0,"allocatedInterest":5956,"mmp":8172},{"date":"2016-07-07","principalLeft":146252,"amortisation":2103,"allocatedFees":0,"allocatedInterest":6069,"mmp":8172},{"date":"2016-08-08","principalLeft":144149,"amortisation":1791,"allocatedFees":0,"allocatedInterest":6381,"mmp":8172},{"date":"2016-09-07","principalLeft":142358,"amortisation":2264,"allocatedFees":0,"allocatedInterest":5908,"mmp":8172},{"date":"2016-10-07","principalLeft":140094,"amortisation":2358,"allocatedFees":0,"allocatedInterest":5814,"mmp":8172},{"date":"2016-11-07","principalLeft":137736,"amortisation":2265,"allocatedFees":0,"allocatedInterest":5907,"mmp":8172},{"date":"2016-12-07","principalLeft":135471,"amortisation":2550,"allocatedFees":0,"allocatedInterest":5622,"mmp":8172},{"date":"2017-01-09","principalLeft":132921,"amortisation":2104,"allocatedFees":0,"allocatedInterest":6068,"mmp":8172},{"date":"2017-02-07","principalLeft":130817,"amortisation":2924,"allocatedFees":0,"allocatedInterest":5248,"mmp":8172},{"date":"2017-03-07","principalLeft":127893,"amortisation":3218,"allocatedFees":0,"allocatedInterest":4954,"mmp":8172},{"date":"2017-04-07","principalLeft":124675,"amortisation":2826,"allocatedFees":0,"allocatedInterest":5346,"mmp":8172},{"date":"2017-05-08","principalLeft":121849,"amortisation":2947,"allocatedFees":0,"allocatedInterest":5225,"mmp":8172},{"date":"2017-06-07","principalLeft":118902,"amortisation":3238,"allocatedFees":0,"allocatedInterest":4934,"mmp":8172},{"date":"2017-07-07","principalLeft":115664,"amortisation":3372,"allocatedFees":0,"allocatedInterest":4800,"mmp":8172},{"date":"2017-08-07","principalLeft":112292,"amortisation":3357,"allocatedFees":0,"allocatedInterest":4815,"mmp":8172},{"date":"2017-09-07","principalLeft":108935,"amortisation":3501,"allocatedFees":0,"allocatedInterest":4671,"mmp":8172},{"date":"2017-10-09","principalLeft":105434,"amortisation":3505,"allocatedFees":0,"allocatedInterest":4667,"mmp":8172},{"date":"2017-11-07","principalLeft":101929,"amortisation":4083,"allocatedFees":0,"allocatedInterest":4089,"mmp":8172},{"date":"2017-12-07","principalLeft":97846,"amortisation":4111,"allocatedFees":0,"allocatedInterest":4061,"mmp":8172},{"date":"2018-01-08","principalLeft":93735,"amortisation":4023,"allocatedFees":0,"allocatedInterest":4149,"mmp":8172},{"date":"2018-02-07","principalLeft":89712,"amortisation":4449,"allocatedFees":0,"allocatedInterest":3723,"mmp":8172},{"date":"2018-03-07","principalLeft":85263,"amortisation":4869,"allocatedFees":0,"allocatedInterest":3303,"mmp":8172},{"date":"2018-04-09","principalLeft":80394,"amortisation":4502,"allocatedFees":0,"allocatedInterest":3670,"mmp":8172},{"date":"2018-05-07","principalLeft":75892,"amortisation":5232,"allocatedFees":0,"allocatedInterest":2940,"mmp":8172},{"date":"2018-06-07","principalLeft":70660,"amortisation":5142,"allocatedFees":0,"allocatedInterest":3030,"mmp":8172},{"date":"2018-07-09","principalLeft":65518,"amortisation":5272,"allocatedFees":0,"allocatedInterest":2900,"mmp":8172},{"date":"2018-08-07","principalLeft":60246,"amortisation":5755,"allocatedFees":0,"allocatedInterest":2417,"mmp":8172},{"date":"2018-09-07","principalLeft":54491,"amortisation":5835,"allocatedFees":0,"allocatedInterest":2337,"mmp":8172},{"date":"2018-10-08","principalLeft":48656,"amortisation":6085,"allocatedFees":0,"allocatedInterest":2087,"mmp":8172},{"date":"2018-11-07","principalLeft":42571,"amortisation":6405,"allocatedFees":0,"allocatedInterest":1767,"mmp":8172},{"date":"2018-12-07","principalLeft":36166,"amortisation":6671,"allocatedFees":0,"allocatedInterest":1501,"mmp":8172},{"date":"2019-01-07","principalLeft":29495,"amortisation":6907,"allocatedFees":0,"allocatedInterest":1265,"mmp":8172},{"date":"2019-02-07","principalLeft":22588,"amortisation":7203,"allocatedFees":0,"allocatedInterest":969,"mmp":8172},{"date":"2019-03-07","principalLeft":15385,"amortisation":7576,"allocatedFees":0,"allocatedInterest":596,"mmp":8172},{"date":"2019-04-08","principalLeft":7809,"amortisation":7809,"allocatedFees":0,"allocatedInterest":346,"mmp":8155}],"mmp":8172.0,"apr":0.0}

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

    private String productSelectionView(RequestBuilder rb) {
        // Product selection view

        CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class); //


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
            String id = creditLineProducts.toArray(new ProductAvailableDTO[creditLineProducts.size()])[randomIndex].id;
            System.out.println(id);
            return id;
        }

        throw new IllegalStateException("Could find product from list");
    }

    private void submitApplication(RequestBuilder rb) {
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
    }

    private void openApplicationForm(RequestBuilder rb) {
        AuthenticationInfoDTO authenticationInfoDTO = rb.resource("authentication").get(AuthenticationInfoDTO.class); // {"customer":{"firstName":"Firstname","lastName":"Lastname","secondLastName":null,"dateOfBirth":null,"countryOfBirth":null,"id":"1509550","email":null,"msisdn":"+37256252830","msisdn2":null,"identifier":"39008160326","address":{"street":null,"postcode":null,"city":null,"province":null,"floor":null,"door":null},"bankAccount":null,"msisdnPending":null,"emailPending":null,"canUsePassword":true,"contracts":[],"customerNumber":"1509550","communicationSettings":null,"communicationSettingsPL":null,"isInCollection":false,"collectionDate":null,"bank":null,"preferredLanguage":"et","isOldCustomer":false,"isCLAllowed":false,"isInPendingDrawState":false,"bankAccountVerified":false,"customerRegistered":false,"greetingsName":"Firstname","gender":"MALE","identificationState":"NOT_STARTED_YET","clallowed":false},"authenticationMethod":"DEFAULT"}
//        ClientResponse response = rb.resource("staticcontent").get(ClientResponse.class);
        try {
            CreditApplicationDTO creditApplicationDTO = rb.resource("credit-application").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {

        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }
        CreditApplicationDTO creditApplicationPutDTO = rb.resource("credit-application").put(CreditApplicationDTO.class, new NewApplicationDTO());  // {"id":154219795540002201,"product":null,"state":"OPEN","firstDueDate":null,"documentRequirements":[],"financialData":{},"firstDrawAmount":null,"authenticationPostOfficeDTO":null,"channel":"Web"}
        Collection<LoanIssuerDTO> loanIssuerDTOs = rb.resource("credit-application/loanissuers").get(new GenericType<Collection<LoanIssuerDTO>>() {}); // [{"key":"employer","name":"Tööandja"},{"key":"privatePerson","name":"Eraisik"},{"key":"other","name":"Keegi muu"},{"key":"swedbank","name":"Swedbank"},{"key":"sebPank","name":"SEB pank"},{"key":"danskePank","name":"Danske pank"},{"key":"nordeaPank","name":"Nordea Pank"},{"key":"parexPank","name":"Parex pank"},{"key":"dnbNord","name":"DnB Nord"},{"key":"krediidipank","name":"Krediidipank"},{"key":"bigbank","name":"BIGBANK"},{"key":"amCredit","name":"AmCredit"},{"key":"autokiirlaen","name":"Autokiirlaen"},{"key":"creditmarket","name":"Creditmarket"},{"key":"ferratum","name":"Ferratum"},{"key":"krediidikassa","name":"Krediidikassa"},{"key":"liisiJarelmaks","name":"Liisi järelmaks"},{"key":"monetti","name":"Monetti"},{"key":"raha24","name":"Raha24"},{"key":"smsLaen","name":"SMS laen"},{"key":"smsMoney","name":"SMS Money"},{"key":"timeInvest","name":"Time Invest"}]
        Collection<PostOfficeDTO> postOfficeDTOs = rb.resource("authentication/ee/postoffices").get(new GenericType<Collection<PostOfficeDTO>>() {}); //[{"ID":1,"region":"HARJUMAA","name":"Tallinna postkontor","email":"tallinna.postkontor@omniva.ee","address":"Narva mnt. 1, Tallinn","hidden":false},{"ID":2,"region":"HARJUMAA","name":"Lasnamäe Centrum postkontor","email":"lasnamaecentrum.postkontor@omniva.ee","address":"Mustakivi tee 13, Tallinn - Lasnamäe Centrum","hidden":false},{"ID":4,"region":"HARJUMAA","name":"Pallasti äriklienditeenindus","email":"ariklienditeenindus@omniva.ee","address":"Pallasti 28, Tallinn","hidden":false},{"ID":5,"region":"HARJUMAA","name":"Lasnamäe postkontor","email":"lasnamae.postkontor@omniva.ee","address":"Pae 80, Tallinn - Lasnamäe Bravo keskus","hidden":false},{"ID":6,"region":"HARJUMAA","name":"Lilleküla postkontor","email":"lillekula.postkontor@omniva.ee","address":"Mustamäe tee 16, Tallinn - Marienthali keskus","hidden":false},{"ID":7,"region":"HARJUMAA","name":"Õismäe postkontor","email":"oismae.postkontor@omniva.ee","address":"Paldiski mnt 102, Tallinn - Rocca-al-Mare keskus","hidden":false},{"ID":8,"region":"HARJUMAA","name":"Kristiine postkontor","email":"kristiine.postkontor@omniva.ee","address":"Endla 45, Tallinn - Kristiine kaubanduskeskus","hidden":false},{"ID":9,"region":"HARJUMAA","name":"Pelguranna postkontor","email":"pelguranna.postkontor@omniva.ee","address":"Sõle 51, Tallinn - Pelgulinna Selver","hidden":false},{"ID":10,"region":"HARJUMAA","name":"Viimsi postkontor","email":"viimsi.postkontor@omniva.ee","address":"Sõpruse tee 15, Haabneeme - Viimsi Selver","hidden":false},{"ID":11,"region":"HARJUMAA","name":"Maardu postkontor","email":"maardu.postkontor@omniva.ee","address":"Keemikute 2, Maardu","hidden":false},{"ID":12,"region":"HARJUMAA","name":"Keila postkontor","email":"keila.postkontor@omniva.ee","address":"Jaama 1, Keila","hidden":false},{"ID":13,"region":"HARJUMAA","name":"Paldiski postkontor","email":"paldiski.postkontor@omniva.ee","address":"Rae 14B/14C, Paldiski - Maxima kauplus","hidden":false},{"ID":14,"region":"HARJUMAA","name":"Loksa postkontor","email":"loksa.postkontor@omniva.ee","address":"Papli 2, Loksa","hidden":false},{"ID":15,"region":"HARJUMAA","name":"Saku postkontor","email":"saku.postkontor@omniva.ee","address":"Teaduse 1, Saku","hidden":false},{"ID":16,"region":"HARJUMAA","name":"Saue postkontor","email":"saue.postkontor@omniva.ee","address":"Kütise 4, Saue","hidden":false},{"ID":17,"region":"HARJUMAA","name":"Mustamäe postkontor","email":"mustamae.postkontor@omniva.ee","address":"Sõpruse pst 201/203, Tallinn - Magistrali kaubanduskeskus","hidden":false},{"ID":18,"region":"HARJUMAA","name":"Mustika postkontor","email":"mustika.postkontor@omniva.ee","address":"A. H. Tammsaare tee 116, Tallinn - Mustika Kaubanduskeskus","hidden":false},{"ID":20,"region":"RAPLAMAA","name":"Rapla postkontor","email":"rapla.kassa@omniva.ee","address":"Tallinna mnt 17, Rapla","hidden":false},{"ID":21,"region":"RAPLAMAA","name":"Märjamaa postkontor","email":"marjamaa.postkontor@omniva.ee","address":"Veski 6, Märjamaa","hidden":false},{"ID":22,"region":"TARTUMAA","name":"Elva postkontor","email":"elva.postkontor@omniva.ee","address":"Puiestee 1, Elva","hidden":false},{"ID":23,"region":"TARTUMAA","name":"Tartu Kesklinna postkontor","email":"tartu.postkontor@omniva.ee","address":"Riia 4, Tartu","hidden":false},{"ID":24,"region":"PÄRNUMAA","name":"Kilingi- Nõmme postkontor","email":"kilingi-nomme.postkontor@omniva.ee","address":"Pärnu 44, Kilingi-Nõmme","hidden":false},{"ID":25,"region":"PÄRNUMAA","name":"Pärnu postkontor","email":"parnu.postkontor@omniva.ee","address":"Hommiku 4, Pärnu","hidden":false},{"ID":26,"region":"PÄRNUMAA","name":"Pärnu-Jaagupi postkontor","email":"parnu-jaagupi.postkontor@omniva.ee","address":"Pärnu mnt 8, Pärnu-Jaagupi","hidden":false},{"ID":27,"region":"PÄRNUMAA","name":"Sindi postkontor","email":"sindi.postkontor@omniva.ee","address":"Jaama 8, Sindi","hidden":false},{"ID":28,"region":"JÕGEVAMAA","name":"Jõgeva postkontor","email":"jogeva.postkontor@omniva.ee","address":"Aia 8, Jõgeva","hidden":false},{"ID":29,"region":"JÕGEVAMAA","name":"Kallaste postkontor","email":"kallaste.postkontor@omniva.ee","address":"Oja 22, Kallaste","hidden":false},{"ID":30,"region":"JÕGEVAMAA","name":"Põltsamaa postkontor","email":"poltsamaa.postkontor@omniva.ee","address":"Silla 2, Põltsamaa","hidden":false},{"ID":31,"region":"IDA-VIRUMAA","name":"Kiviõli postkontor","email":"kivioli.postkontor@omniva.ee","address":"Metsa 3, Kiviõli","hidden":false},{"ID":32,"region":"IDA-VIRUMAA","name":"Kohtla–Järve postkontor","email":"kohtla-jarve.postkontor@omniva.ee","address":"Järveküla tee 50, Kohtla-Järve - Vironia Kaubanduskeskus","hidden":false},{"ID":33,"region":"IDA-VIRUMAA","name":"Narva–Jõesuu postkontor","email":"narva-joesuu.postkontor@omniva.ee","address":"Pargi 10, Narva-Jõesuu","hidden":false},{"ID":34,"region":"IDA-VIRUMAA","name":"Narva postkontor","email":"narva.postkontor@omniva.ee","address":"Tallinna mnt 41, Narva - Astri Kaubanduskeskus","hidden":false},{"ID":35,"region":"IDA-VIRUMAA","name":"Jõhvi postkontor","email":"johvi.postkontor@omniva.ee","address":"Narva mnt 8, Jõhvi - Jewe kaubanduskeskus","hidden":false},{"ID":36,"region":"IDA-VIRUMAA","name":"Sillamäe postkontor","email":"sillamae.postkontor@omniva.ee","address":"Ivan Pavlovi 2A, Sillamäe","hidden":false},{"ID":37,"region":"LÄÄNEMAA","name":"Haapsalu postkontor","email":"haapsalu.postkontor@omniva.ee","address":"Nurme 2, Haapsalu","hidden":false},{"ID":38,"region":"LÄÄNEMAA","name":"Lihula postkontor","email":"lihula.postkontor@omniva.ee","address":"Tallinna mnt 12, Lihula","hidden":false},{"ID":39,"region":"VALGAMAA","name":"Tõrva postkontor","email":"torva.postkontor@omniva.ee","address":"Valga 1, Tõrva","hidden":false},{"ID":40,"region":"VALGAMAA","name":"Otepää postkontor","email":"otepaa.postkontor@omniva.ee","address":"Tartu mnt 1, Otepää","hidden":false},{"ID":41,"region":"VALGAMAA","name":"Valga postkontor","email":"valga.postkontor@omniva.ee","address":"Kesk 10, Valga","hidden":false},{"ID":42,"region":"VILJANDIMAA","name":"Karksi–Nuia postkontor","email":"karksi-nuia.postkontor@omniva.ee","address":"Rahumäe 2A, Karksi-Nuia","hidden":false},{"ID":43,"region":"VILJANDIMAA","name":"Suure–Jaani postkontor","email":"suure-jaani.postkontor@omniva.ee","address":"Pärnu 3, Suure-Jaani - Konsum","hidden":false},{"ID":44,"region":"VILJANDIMAA","name":"Viljandi postkontor","email":"viljandi.postkontor@omniva.ee","address":"Ilmarise 1, Viljandi","hidden":false},{"ID":45,"region":"VÕRUMAA","name":"Antsla postkontor","email":"antsla.postkontor@omniva.ee","address":"Kreutzwaldi 2, Antsla","hidden":false},{"ID":46,"region":"VÕRUMAA","name":"Võru postkontor","email":"voru.postkontor@omniva.ee","address":"Vilja 14B, Võru","hidden":false},{"ID":47,"region":"LÄÄNE-VIRUMAA","name":"Tapa postkontor","email":"tapa.postkontor@omniva.ee","address":"Pikk 3, Tapa","hidden":false},{"ID":48,"region":"LÄÄNE-VIRUMAA","name":"Kunda postkontor","email":"kunda.postkontor@omniva.ee","address":"Kasemäe 12, Kunda","hidden":false},{"ID":49,"region":"LÄÄNE-VIRUMAA","name":"Rakvere postkontor","email":"rakvere.postkontor@omniva.ee","address":"Ferdinand Gustav Adoffi 11, Rakvere","hidden":false},{"ID":50,"region":"PÕLVAMAA","name":"Põlva postkontor","email":"polva.postkontor@omniva.ee","address":"Kesk 10, Põlva - Põlva Kaubanduskeskus","hidden":false},{"ID":51,"region":"JÄRVAMAA","name":"Paide postkontor","email":"paide.postkontor@omniva.ee","address":"Telliskivi 5, Paide","hidden":false},{"ID":52,"region":"JÄRVAMAA","name":"Türi postkontor","email":"turi.postkontor@omniva.ee","address":"Viljandi 1A, Türi","hidden":false},{"ID":53,"region":"HIIUMAA","name":"Kärdla postkontor","email":"kardla.postkontor@omniva.ee","address":"Keskväljak 3, Kärdla","hidden":false},{"ID":54,"region":"SAAREMAA","name":"Kuressaare postkontor","email":"kuressaare.postkontor@omniva.ee","address":"Torni 1, Kuressaare","hidden":false},{"ID":55,"region":"HARJUMAA","name":"IPF Digital Estonia OÜ kontor","email":"klienditeenindus@credit24.ee","address":"Lõõtsa 5, Tallinn","hidden":false},{"ID":56,"region":"TARTUMAA","name":"Tartu Eedeni postkontor","email":"eeden.postkontor@omniva.ee","address":"Kalda tee 1C, Tartu - Eedeni kaubanduskeskus","hidden":false},{"ID":57,"region":"HARJUMAA","name":"Rae postkontor","email":"rae.postkontor@omniva.ee","address":"Veesaare tee 2 - Peetri Selver","hidden":false},{"ID":58,"region":"HARJUMAA","name":"Järve postkontor","email":"jarve.postpood@omniva.ee","address":"Pärnu mnt 238, Tallinn - Järve Selver","hidden":false}]
        try {
            rb.resource("credit-application/previous").get(CreditApplicationDTO.class); // 404
        } catch (UniformInterfaceException e) {
//            assertTrue(e.getResponse().getStatus() == 404);
        } catch (ClientHandlerException e) {
            e.printStackTrace();
        }

//        logToConsole(response);
        logToConsole(authenticationInfoDTO);
        logToConsole(creditApplicationPutDTO);
        logToConsole(loanIssuerDTOs.toArray());
        logToConsole(postOfficeDTOs.toArray());
    }

    private void registrationEEC24(RequestBuilder rb) {
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
//        logToConsole(response);
    }

    public static void logToConsole(Object obj) {
        System.out.println(ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE));
    }


}

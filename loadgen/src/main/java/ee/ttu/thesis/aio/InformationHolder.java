package ee.ttu.thesis.aio;

/**
 *
 */
public class InformationHolder {

    protected String ssn;
    protected String msisdn;
    protected Long contractId;
    protected Long customerId;
    protected Integer outstandingAmount;
    protected Integer transactionCount;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Integer outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InformationHolder{");
        sb.append("ssn='").append(ssn).append('\'');
        sb.append(", msisdn='").append(msisdn).append('\'');
        sb.append(", contractId=").append(contractId);
        sb.append(", customerId=").append(customerId);
        sb.append(", outstandingAmount=").append(outstandingAmount);
        sb.append(", transactionCount=").append(transactionCount);
        sb.append('}');
        return sb.toString();
    }

    public void print() {
        System.err.println(this);
    }


}

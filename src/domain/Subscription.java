package domain;

import domain.common.Money;
import java.io.Serializable;

public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        PENDING_REVIEW, APPROVED, REJECTED, SUPPLEMENT_REQUIRED;

        public String getLabel() {
            switch (this) {
                case PENDING_REVIEW:      return "심사대기중";
                case APPROVED:            return "인수승인";
                case REJECTED:            return "인수거절";
                case SUPPLEMENT_REQUIRED: return "서류보완요청";
                default:                  return "";
            }
        }
    }

    private String subscriptionNo;
    private String applicantName;
    private String ssn;
    private String address;
    private String carNumber;
    private String chassisNumber;
    private String productName;
    private Money premium;
    private Money basePremium;
    private String subscriptionDate;
    private Status status;
    private String occupation;
    private int age;
    private String coveragesDescription;

    public String getSubscriptionNo()       { return subscriptionNo; }
    public String getApplicantName()        { return applicantName; }
    public String getSsn()                  { return ssn; }
    public String getAddress()              { return address; }
    public String getCarNumber()            { return carNumber; }
    public String getChassisNumber()        { return chassisNumber; }
    public String getProductName()          { return productName; }
    public Money getPremium()               { return premium; }
    public Money getBasePremium()           { return basePremium; }
    public String getSubscriptionDate()     { return subscriptionDate; }
    public Status getStatus()               { return status; }
    public String getOccupation()           { return occupation; }
    public int getAge()                     { return age; }
    public String getCoveragesDescription() { return coveragesDescription; }

    public void setSubscriptionNo(String v)       { this.subscriptionNo = v; }
    public void setApplicantName(String v)        { this.applicantName = v; }
    public void setSsn(String v)                  { this.ssn = v; }
    public void setAddress(String v)              { this.address = v; }
    public void setCarNumber(String v)            { this.carNumber = v; }
    public void setChassisNumber(String v)        { this.chassisNumber = v; }
    public void setProductName(String v)          { this.productName = v; }
    public void setPremium(Money v)               { this.premium = v; }
    public void setBasePremium(Money v)           { this.basePremium = v; }
    public void setSubscriptionDate(String v)     { this.subscriptionDate = v; }
    public void setStatus(Status v)               { this.status = v; }
    public void setOccupation(String v)           { this.occupation = v; }
    public void setAge(int v)                     { this.age = v; }
    public void setCoveragesDescription(String v) { this.coveragesDescription = v; }
}

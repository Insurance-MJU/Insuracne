package domain;

import domain.common.Money;
import java.io.Serializable;

public class RiskAnalysisReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subscriptionNo;
    private double riskScore;
    private int riskGrade;
    private double accidentScore;
    private double drivingExpScore;
    private double creditGradeScore;
    private double trafficViolationScore;
    private double surchargeRate;
    private Money basePremium;
    private Money surchargeAmount;
    private Money totalPremium;
    private String reviewGuide;
    private String reviewerName;
    private String reviewDate;
    private String reviewOpinion;

    public String getRiskGradeLabel() {
        switch (riskGrade) {
            case 1: return "1등급 [안전]";
            case 2: return "2등급 [보통]";
            case 3: return "3등급 [조금 위험]";
            case 4: return "4등급 [위험]";
            case 5: return "5등급 [고위험]";
            default: return riskGrade + "등급";
        }
    }

    public String getSubscriptionNo()       { return subscriptionNo; }
    public double getRiskScore()            { return riskScore; }
    public int getRiskGrade()               { return riskGrade; }
    public double getAccidentScore()        { return accidentScore; }
    public double getDrivingExpScore()      { return drivingExpScore; }
    public double getCreditGradeScore()     { return creditGradeScore; }
    public double getTrafficViolationScore(){ return trafficViolationScore; }
    public double getSurchargeRate()        { return surchargeRate; }
    public Money getBasePremium()           { return basePremium; }
    public Money getSurchargeAmount()       { return surchargeAmount; }
    public Money getTotalPremium()          { return totalPremium; }
    public String getReviewGuide()          { return reviewGuide; }
    public String getReviewerName()         { return reviewerName; }
    public String getReviewDate()           { return reviewDate; }
    public String getReviewOpinion()        { return reviewOpinion; }

    public void setSubscriptionNo(String v)       { this.subscriptionNo = v; }
    public void setRiskScore(double v)            { this.riskScore = v; }
    public void setRiskGrade(int v)               { this.riskGrade = v; }
    public void setAccidentScore(double v)        { this.accidentScore = v; }
    public void setDrivingExpScore(double v)      { this.drivingExpScore = v; }
    public void setCreditGradeScore(double v)     { this.creditGradeScore = v; }
    public void setTrafficViolationScore(double v){ this.trafficViolationScore = v; }
    public void setSurchargeRate(double v)        { this.surchargeRate = v; }
    public void setBasePremium(Money v)           { this.basePremium = v; }
    public void setSurchargeAmount(Money v)       { this.surchargeAmount = v; }
    public void setTotalPremium(Money v)          { this.totalPremium = v; }
    public void setReviewGuide(String v)          { this.reviewGuide = v; }
    public void setReviewerName(String v)         { this.reviewerName = v; }
    public void setReviewDate(String v)           { this.reviewDate = v; }
    public void setReviewOpinion(String v)        { this.reviewOpinion = v; }
}

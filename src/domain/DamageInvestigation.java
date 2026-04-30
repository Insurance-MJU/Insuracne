package domain;

import java.util.Date;

public class DamageInvestigation {
    private String accidentCause;
    private String claimId;
    private String damageDetail;
    private Date investigationDate;
    private String investigationId;
    private String investigationResult;
    private String investigatorName;
    private double liabilityRatio;

    public String getInvestigationResult() { return investigationResult; }
    public boolean investigateDamage() { return false; }
    public boolean updateLiabilityRatio(double ratio) { return false; }
    public boolean updateAccidentDetail(String detail) { this.damageDetail = detail; return true; }

    public String getAccidentCause() { return accidentCause; }
    public String getClaimId() { return claimId; }
    public String getDamageDetail() { return damageDetail; }
    public Date getInvestigationDate() { return investigationDate; }
    public String getInvestigationId() { return investigationId; }
    public String getInvestigatorName() { return investigatorName; }
    public double getLiabilityRatio() { return liabilityRatio; }
}

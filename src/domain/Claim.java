package domain;

import java.util.Date;

public class Claim {
    private String claimantName;
    private Date claimDate;
    private String claimId;
    private ClaimStatus claimStatus;
    private ClaimType claimType;
    private String contractId;
    private String coverageId;
    private String description;

    public enum ClaimStatus {}
    public enum ClaimType {}

    public String createClaim() { return null; }
    public String getClaimInfo() { return null; }
    public boolean updateStatus(ClaimStatus status) { return false; }

    public String getClaimantName() { return claimantName; }
    public Date getClaimDate() { return claimDate; }
    public String getClaimId() { return claimId; }
    public ClaimStatus getClaimStatus() { return claimStatus; }
    public ClaimType getClaimType() { return claimType; }
    public String getContractId() { return contractId; }
    public String getCoverageId() { return coverageId; }
    public String getDescription() { return description; }
}

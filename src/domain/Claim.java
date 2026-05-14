package domain;

import infra.util.FileStore;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Claim implements Serializable {
    private static final long serialVersionUID = 1L;
    private String claimId;
    private String accidentId;
    private String claimantName;
    private Date claimDate;
    private String contractId;
    private String description;
    private String status;
    private String assignedEmployee;
    private int settlement;
    private int deductible;
    private int compensationAmount;
    private String bankName;
    private String accountNumber;

    public enum ClaimStatus { PENDING, INVESTIGATING, ASSESSING, PAYMENT_PENDING, CLOSED }
    public enum ClaimType { PROPERTY, PERSONAL }

    public Claim() {}

    public Claim(String claimId, String accidentId, String claimantName, String claimDate,
                 String contractId, String description, String status) {
        this.claimId = claimId;
        this.accidentId = accidentId;
        this.claimantName = claimantName;
        try { this.claimDate = new SimpleDateFormat("yyyy-MM-dd").parse(claimDate); } catch (Exception e) { this.claimDate = new Date(); }
        this.contractId = contractId;
        this.description = description;
        this.status = status;
    }

    public String createClaim() { return claimId; }
    public String getClaimInfo() { return null; }
    public boolean updateStatus(ClaimStatus s) { return false; }

    /** 손해액 산정 완료: 합의금·자기부담금으로 보상금 계산 후 지급대기 상태로 전환 */
    public void assess(int settlement, int deductible) {
        this.settlement = settlement;
        this.deductible = deductible;
        this.compensationAmount = settlement - deductible;
        this.status = "지급대기";
    }

    /** 보험금 지급 완료: 계좌 정보 저장 후 지급완료 상태로 전환 */
    public void completePayment(String bank, String accountNo) {
        this.bankName = bank;
        this.accountNumber = accountNo;
        this.status = "지급완료";
    }

    /** 계좌번호 숫자 자릿수가 14자리 이하인지 검증 */
    public static boolean isValidAccountNumber(String accountNo) {
        return accountNo.replaceAll("[^0-9]", "").length() <= 14;
    }

    public String getClaimId() { return claimId; }
    public String getAccidentId() { return accidentId; }
    public String getClaimantName() { return claimantName; }
    public Date getClaimDate() { return claimDate; }
    public String getClaimDateDisplay() { return claimDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(claimDate) : ""; }
    public String getContractId() { return contractId; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getAssignedEmployee() { return assignedEmployee; }
    public int getSettlement() { return settlement; }
    public int getDeductible() { return deductible; }
    public int getCompensationAmount() { return compensationAmount; }
    public String getBankName() { return bankName; }
    public String getAccountNumber() { return accountNumber; }

    public void setClaimId(String v) { this.claimId = v; }
    public void setAccidentId(String v) { this.accidentId = v; }
    public void setClaimantName(String v) { this.claimantName = v; }
    public void setClaimDate(Date v) { this.claimDate = v; }
    public void setContractId(String v) { this.contractId = v; }
    public void setDescription(String v) { this.description = v; }
    public void setStatus(String v) { this.status = v; }
    public void setAssignedEmployee(String v) { this.assignedEmployee = v; }
    public void setSettlement(int v) { this.settlement = v; }
    public void setDeductible(int v) { this.deductible = v; }
    public void setCompensationAmount(int v) { this.compensationAmount = v; }
    public void setBankName(String v) { this.bankName = v; }
    public void setAccountNumber(String v) { this.accountNumber = v; }

    // ── 영속성 ────────────────────────────────────────────────
    private static final List<Claim> STORE;
    static {
        List<Claim> loaded = FileStore.load("claims.dat");
        if (loaded != null) { STORE = loaded; }
        else { STORE = new ArrayList<>(); initDefaults(); }
    }
    private static void initDefaults() {
        Claim c = new Claim("CL-00001", "ACC-2026-003", "이영희", "2026-04-18",
                            "CNT-20231210-003", "차량 전손", "지급대기");
        c.setAssignedEmployee("EMP-1023");
        c.setSettlement(1480);
        c.setDeductible(0);
        c.setCompensationAmount(1480);
        STORE.add(c);
        FileStore.save("claims.dat", STORE);
    }
    public static Claim findByAccidentId(String accidentId) {
        return STORE.stream().filter(c -> c.accidentId.equals(accidentId)).findFirst().orElse(null);
    }
    public static Claim findById(String claimId) {
        return STORE.stream().filter(c -> c.claimId.equals(claimId)).findFirst().orElse(null);
    }
    public static List<Claim> findAwaitingPayment() {
        return STORE.stream().filter(c -> "지급대기".equals(c.status)).collect(Collectors.toList());
    }
    public void save() {
        STORE.removeIf(c -> c.claimId.equals(this.claimId));
        STORE.add(this);
        FileStore.save("claims.dat", STORE);
    }
    public static String nextId() {
        return String.format("CL-%05d", STORE.size() + 1);
    }
}

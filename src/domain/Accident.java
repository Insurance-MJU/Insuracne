package domain;

import infra.util.FileStore;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Accident implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accidentId;
    private Date accidentDate;
    private String accidentDetail;
    private String accidentLocation;
    private String reportedBy;
    private String phone;
    private String status;
    private String description;
    private String documents;
    private String contractId;
    private String coverageDescription;
    private String coverageLimit;
    private String personalInjuryLimit;
    private String vehicleInfo;
    private String expectedRepairCost;
    private String regionCode;

    public enum AccidentType {
        COLLISION, REAR_END, SINGLE, HIT_AND_RUN, FIRE, FLOOD, THEFT, NATURAL_DISASTER
    }

    public enum SeverityLevel {
        MINOR, MODERATE, SEVERE, FATAL, TOTAL_LOSS
    }

    public Accident() {}

    public Accident(String accidentId, String accidentDate, String reportedBy, String phone,
                    String description, String accidentLocation, String accidentDetail,
                    String documents, String contractId, String coverageDescription,
                    String coverageLimit, String vehicleInfo, String status) {
        this.accidentId = accidentId;
        try { this.accidentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(accidentDate); } catch (Exception e) { this.accidentDate = null; }
        this.reportedBy = reportedBy;
        this.phone = phone;
        this.description = description;
        this.accidentLocation = accidentLocation;
        this.accidentDetail = accidentDetail;
        this.documents = documents;
        this.contractId = contractId;
        this.coverageDescription = coverageDescription;
        this.coverageLimit = coverageLimit;
        this.vehicleInfo = vehicleInfo;
        this.status = status;
    }

    // ── 정적 팩토리: 고객 보험금 청구 접수 ───────────────────
    public static Accident report(String accidentId, String reportedBy, String phone,
                                   String accidentDate, String accidentLocation,
                                   String accidentDetail, String documents,
                                   Contract contract) {
        Accident a = new Accident();
        a.accidentId          = accidentId;
        a.reportedBy          = reportedBy;
        a.phone               = phone;
        a.description         = "보험금 청구";
        try { a.accidentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(accidentDate); } catch (Exception e) { a.accidentDate = null; }
        a.accidentLocation    = accidentLocation;
        a.accidentDetail      = accidentDetail;
        a.documents           = documents;
        a.contractId          = contract.getContractId();
        a.coverageDescription = contract.getCoveragesDescription();
        a.coverageLimit       = contract.getCoverageLimit();
        a.vehicleInfo         = contract.getCarNumber();
        a.status              = "미처리";
        return a;
    }

    // ── 비즈니스 메서드: 상태 전이 ────────────────────────────
    public void transferToCompensation() { this.status = "보상팀 이관"; }
    public void startProcessing()        { this.status = "처리중"; }
    public void complete()               { this.status = "처리완료"; }
    public boolean isPending()           { return "미처리".equals(status); }

    public boolean updateAccidentDetail(String detail) { this.accidentDetail = detail; return true; }
    public boolean validateAccident() {
        return accidentDate != null && accidentLocation != null && !accidentLocation.isEmpty();
    }

    /** coverageLimit 문자열("2,000만원")에서 숫자(2000)만 추출 */
    public int getCoverageLimitManwon() {
        if (coverageLimit == null) return 2000;
        try {
            return Integer.parseInt(coverageLimit.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 2000;
        }
    }

    public String getAccidentId() { return accidentId; }
    public Date getAccidentDate() { return accidentDate; }
    public String getAccidentDateDisplay() { return accidentDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm").format(accidentDate) : ""; }
    public String getAccidentDetail() { return accidentDetail; }
    public String getAccidentLocation() { return accidentLocation; }
    public String getReportedBy() { return reportedBy; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getDocuments() { return documents; }
    public String getContractId() { return contractId; }
    public String getCoverageDescription() { return coverageDescription; }
    public String getCoverageLimit() { return coverageLimit; }
    public String getPersonalInjuryLimit() { return personalInjuryLimit; }
    public String getVehicleInfo() { return vehicleInfo; }
    public String getExpectedRepairCost() { return expectedRepairCost; }
    public String getRegionCode() { return regionCode; }

    public void setAccidentId(String v) { this.accidentId = v; }
    public void setAccidentDate(Date v) { this.accidentDate = v; }
    public void setAccidentDetail(String v) { this.accidentDetail = v; }
    public void setAccidentLocation(String v) { this.accidentLocation = v; }
    public void setReportedBy(String v) { this.reportedBy = v; }
    public void setPhone(String v) { this.phone = v; }
    public void setStatus(String v) { this.status = v; }
    public void setDescription(String v) { this.description = v; }
    public void setDocuments(String v) { this.documents = v; }
    public void setContractId(String v) { this.contractId = v; }
    public void setCoverageDescription(String v) { this.coverageDescription = v; }
    public void setCoverageLimit(String v) { this.coverageLimit = v; }
    public void setPersonalInjuryLimit(String v) { this.personalInjuryLimit = v; }
    public void setVehicleInfo(String v) { this.vehicleInfo = v; }
    public void setExpectedRepairCost(String v) { this.expectedRepairCost = v; }
    public void setRegionCode(String v) { this.regionCode = v; }

    // ── 영속성 ────────────────────────────────────────────────
    private static final List<Accident> STORE;
    static {
        List<Accident> loaded = FileStore.load("accidents.dat");
        if (loaded != null) { STORE = loaded; }
        else { STORE = new ArrayList<>(); initDefaults(); }
    }
    private static void initDefaults() {
        Accident a1 = new Accident("ACC-2026-001", "2026-04-19 09:32", "홍길동", "010-1234-5678",
            "자동차 대물 사고", "서울 강남구 테헤란로", "신호 대기 중 후방 추돌 사고 발생",
            "사고현장사진.jpg,차량수리견적서.pdf", "CNT-20240315-001", "자동차 대물", "2,000만원",
            "12가 3456 (현대 소나타)", "미처리");
        a1.setPersonalInjuryLimit("1,000만원"); a1.setExpectedRepairCost("850,000원"); a1.setRegionCode("SEOUL-01");
        STORE.add(a1);
        Accident a2 = new Accident("ACC-2026-002", "2026-04-19 11:15", "김철수", "010-9876-5432",
            "차량 파손", "경기도 수원시 팔달구", "주차장 내 차량 문 충돌로 인한 파손",
            "차량파손사진.jpg,수리견적서.pdf", "CNT-20240520-002", "자기차량손해", "3,000만원",
            "34나 5678 (기아 K5)", "미처리");
        a2.setPersonalInjuryLimit("2,000만원"); a2.setExpectedRepairCost("1,200,000원"); a2.setRegionCode("GYEONGGI-01");
        STORE.add(a2);
        Accident a3 = new Accident("ACC-2026-003", "2026-04-18 14:20", "이영희", "010-5555-1234",
            "차량 전손", "인천시 부평구 경인로", "교차로 신호 위반으로 인한 정면 충돌",
            "사고사진.jpg,전손감정서.pdf", "CNT-20231210-003", "자기차량손해", "5,000만원",
            "56다 9012 (현대 그랜저)", "처리중");
        a3.setPersonalInjuryLimit("3,000만원"); a3.setExpectedRepairCost("3,500,000원"); a3.setRegionCode("INCHEON-01");
        STORE.add(a3);
        FileStore.save("accidents.dat", STORE);
    }
    public static List<Accident> findByDateAndStatus(String date, String status) {
        return STORE.stream()
            .filter(a -> a.accidentDate != null
                && new SimpleDateFormat("yyyy-MM-dd").format(a.accidentDate).startsWith(date))
            .filter(a -> status.isEmpty() || (a.status != null && a.status.equals(status)))
            .collect(Collectors.toList());
    }
    public static List<Accident> findPendingAccidents() {
        return STORE.stream().filter(a -> "미처리".equals(a.status)).collect(Collectors.toList());
    }
    public static Accident findById(String accidentId) {
        return STORE.stream().filter(a -> a.accidentId.equals(accidentId)).findFirst().orElse(null);
    }
    public static Accident findByCustomerName(String name) {
        return STORE.stream().filter(a -> a.reportedBy.equals(name)).findFirst().orElse(null);
    }
    public void save() {
        STORE.removeIf(a -> a.accidentId.equals(this.accidentId));
        STORE.add(this);
        FileStore.save("accidents.dat", STORE);
    }
    public static String nextId() {
        return String.format("ACC-2026-%03d", STORE.size() + 1);
    }
}

package infra.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractRepository {

    public static class ContractInfo {
        private final String policyNo;
        private final String contractId;
        private final String productName;
        private final String status;
        private final String issueDate;
        private final String startDate;
        private final String endDate;
        private final long premiumAmount;
        private final String coverages;
        private final String riders;
        private final String carNumber;
        private final String holderName;

        public ContractInfo(String policyNo, String contractId, String productName,
                            String status, String issueDate, String startDate, String endDate,
                            long premiumAmount, String coverages, String riders,
                            String carNumber, String holderName) {
            this.policyNo = policyNo;
            this.contractId = contractId;
            this.productName = productName;
            this.status = status;
            this.issueDate = issueDate;
            this.startDate = startDate;
            this.endDate = endDate;
            this.premiumAmount = premiumAmount;
            this.coverages = coverages;
            this.riders = riders;
            this.carNumber = carNumber;
            this.holderName = holderName;
        }

        public String getPolicyNo()      { return policyNo; }
        public String getContractId()    { return contractId; }
        public String getProductName()   { return productName; }
        public String getStatus()        { return status; }
        public String getIssueDate()     { return issueDate; }
        public String getStartDate()     { return startDate; }
        public String getEndDate()       { return endDate; }
        public long   getPremiumAmount() { return premiumAmount; }
        public String getCoverages()     { return coverages; }
        public String getRiders()        { return riders; }
        public String getCarNumber()     { return carNumber; }
        public String getHolderName()    { return holderName; }
    }

    private static final List<ContractInfo> STORE = new ArrayList<>();

    static {
        STORE.add(new ContractInfo(
            "IN-2026-001", "CNT-20240315-001",
            "MZ세대 다이렉트 개인용자동차보험",
            "유지중", "2026-04-01", "2026-04-01", "2027-04-01",
            2_509_200L,
            "대인배상I, 대인배상II, 대물배상",
            "마일리지 특약",
            "64마0866", "박수현"
        ));
        STORE.add(new ContractInfo(
            "IN-2025-002", "CNT-20240520-002",
            "MZ세대 다이렉트 개인용자동차보험",
            "유지중", "2025-06-15", "2025-06-15", "2026-06-15",
            1_980_000L,
            "대인배상I, 대인배상II, 대물배상, 자기차량손해",
            "블랙박스 할인특약",
            "12가3456", "김직원"
        ));
        STORE.add(new ContractInfo(
            "IN-2023-003", "CNT-20231210-003",
            "MZ세대 다이렉트 개인용자동차보험",
            "만기", "2023-12-10", "2023-12-10", "2024-12-10",
            2_100_000L,
            "대인배상I, 대물배상, 자기차량손해",
            "없음",
            "56다9012", "이영희"
        ));
    }

    public static List<ContractInfo> findAll() {
        return new ArrayList<>(STORE);
    }

    public static ContractInfo findByPolicyNo(String policyNo) {
        return STORE.stream()
            .filter(c -> c.getPolicyNo().equals(policyNo))
            .findFirst().orElse(null);
    }

    public static List<ContractInfo> findByCondition(String holderName, String periodChoice, String statusChoice) {
        String cutoff1 = LocalDate.now().minusYears(1).toString();
        String cutoff3 = LocalDate.now().minusYears(3).toString();

        return STORE.stream()
            .filter(c -> holderName.isEmpty() || c.getHolderName().equals(holderName))
            .filter(c -> {
                if ("2".equals(periodChoice)) return c.getIssueDate().compareTo(cutoff1) >= 0;
                if ("3".equals(periodChoice)) return c.getIssueDate().compareTo(cutoff3) >= 0;
                return true;
            })
            .filter(c -> {
                if ("1".equals(statusChoice)) return "유지중".equals(c.getStatus());
                if ("2".equals(statusChoice)) return "만기".equals(c.getStatus());
                if ("3".equals(statusChoice)) return "해지".equals(c.getStatus());
                return true;
            })
            .collect(Collectors.toList());
    }
}

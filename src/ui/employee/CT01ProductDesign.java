package ui.employee;

import domain.*;
import infra.Context;
import infra.repository.ProductRepository;
import java.text.SimpleDateFormat;
import java.util.*;

public class CT01ProductDesign {
    private final Scanner sc = Context.getInstance().scanner();
    private final ProductRepository productRepo = new ProductRepository();
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static final String[] COVERAGE_NAMES = {
        "대인배상 I", "대인배상 II", "대물배상",
        "자동차상해", "자기차량손해", "무보험차상해"
    };
    private static final String[][] COVERAGE_OPTIONS = {
        {"기본 옵션"},
        {"한도5억", "무한"},
        {"기본옵션"},
        {"기본옵션"},
        {"기본옵션"},
        {"기본옵션"}
    };
    private static final boolean[] COVERAGE_MANDATORY = {
        true, false, false, false, false, false
    };
    private static final String[] RIDER_NAMES = {"블랙박스할인특약", "마일리지할인특약"};

    public void run() {
        System.out.println("\n========================================");
        System.out.println(" CT-01: 상품을 설계한다");
        System.out.println("========================================");
        System.out.println("[상품관리 > 신규 상품 등록]");

        // ── Step 2~3: 상품 기본정보 입력 ─────────────────────────
        System.out.println("\n── 신규 상품 등록 폼 ──────────────────");
        System.out.print(" 상품명: ");
        String productName = sc.nextLine().trim();

        // E1: 상품코드 중복 검사
        String productCode;
        while (true) {
            System.out.print(" 상품코드 (예: CAR-2026-MZ): ");
            productCode = sc.nextLine().trim();
            if (productRepo.existsByCode(productCode)) {
                System.out.println("[오류] 이미 사용 중인 상품코드입니다.");
            } else {
                break;
            }
        }

        System.out.println(" 보험종목:");
        System.out.println("  1. 개인용자동차보험");
        System.out.println("  2. 업무용자동차보험");
        System.out.println("  3. 영업용자동차보험");
        System.out.print(" 선택: ");
        String lobChoice = sc.nextLine().trim();
        Product.Target target;
        if ("2".equals(lobChoice))      target = Product.Target.BUSINESS;
        else if ("3".equals(lobChoice)) target = Product.Target.COMMERCIAL;
        else                            target = Product.Target.PERSONAL;

        System.out.print(" 판매시작일 (yyyy-MM-dd): ");
        String startStr = sc.nextLine().trim();
        System.out.print(" 판매종료일 (yyyy-MM-dd): ");
        String endStr = sc.nextLine().trim();
        Date saleStart = null, saleEnd = null;
        try { saleStart = SDF.parse(startStr); } catch (Exception ignored) {}
        try { saleEnd   = SDF.parse(endStr);   } catch (Exception ignored) {}

        System.out.print(" 가입대상 (예: 만 20세 이상 39세 이하 운전자): ");
        String targetDesc = sc.nextLine().trim();
        System.out.print(" 설명: ");
        String description = sc.nextLine().trim();

        System.out.print("\n[다음] (Enter): ");
        sc.nextLine();

        // ── Step 4~5: 담보 선택 ───────────────────────────────
        List<Integer> selectedCoverageIdxs;
        while (true) {
            System.out.println("\n── 담보 목록 ──────────────────────────");
            for (int i = 0; i < COVERAGE_NAMES.length; i++) {
                System.out.printf("  %d. %s%s%n", i + 1, COVERAGE_NAMES[i],
                        COVERAGE_MANDATORY[i] ? " (필수)" : "");
            }
            System.out.print("\n선택할 담보 번호 (쉼표 구분, 예: 1,2,3): ");
            selectedCoverageIdxs = parseNumbers(sc.nextLine().trim(), COVERAGE_NAMES.length);

            // A1: 대인배상 I 필수 검사
            if (!selectedCoverageIdxs.contains(0)) {
                System.out.println("[경고] 대인배상 I은 자동차보험의 필수 담보입니다. 담보 리스트에 추가해 주세요.");
            } else {
                break;
            }
        }

        // 가입 옵션 선택
        Map<String, String> coverageOptionMap = new LinkedHashMap<>();
        for (int idx : selectedCoverageIdxs) {
            String[] opts = COVERAGE_OPTIONS[idx];
            if (opts.length == 1) {
                coverageOptionMap.put(COVERAGE_NAMES[idx], opts[0]);
            } else {
                System.out.printf("%n[%s 가입 옵션]%n", COVERAGE_NAMES[idx]);
                for (int j = 0; j < opts.length; j++) {
                    System.out.printf("  %d. %s%n", j + 1, opts[j]);
                }
                System.out.print(" 선택 (쉼표로 복수 선택): ");
                List<Integer> optIdxs = parseNumbers(sc.nextLine().trim(), opts.length);
                StringJoiner sj = new StringJoiner(", ");
                for (int oi : optIdxs) sj.add(opts[oi]);
                coverageOptionMap.put(COVERAGE_NAMES[idx], sj.length() > 0 ? sj.toString() : opts[0]);
            }
        }

        System.out.print("\n[다음] (Enter): ");
        sc.nextLine();

        // ── Step 6~7: 특약 선택 ───────────────────────────────
        System.out.println("\n── 특약 목록 ──────────────────────────");
        for (int i = 0; i < RIDER_NAMES.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, RIDER_NAMES[i]);
        }
        System.out.print("선택 번호 (없으면 Enter): ");
        String riderInput = sc.nextLine().trim();
        List<Integer> selectedRiderIdxs = riderInput.isEmpty()
                ? new ArrayList<>() : parseNumbers(riderInput, RIDER_NAMES.length);

        System.out.print("\n[다음] (Enter): ");
        sc.nextLine();

        // ── Step 8: 설계 요약 ─────────────────────────────────
        System.out.println("\n── 상품 설계 요약 ──────────────────────");
        System.out.printf(" 상품명    : %s%n", productName);
        System.out.printf(" 상품코드  : %s%n", productCode);
        System.out.printf(" 보험종목  : %s%n", target.getAutoLabel());
        System.out.printf(" 판매기간  : %s ~ %s%n", startStr, endStr);
        System.out.printf(" 가입대상  : %s%n", targetDesc);
        System.out.println(" 선택 담보:");
        for (Map.Entry<String, String> e : coverageOptionMap.entrySet()) {
            System.out.printf("   - %s [%s]%n", e.getKey(), e.getValue());
        }
        if (!selectedRiderIdxs.isEmpty()) {
            System.out.println(" 선택 특약:");
            for (int idx : selectedRiderIdxs) {
                System.out.printf("   - %s%n", RIDER_NAMES[idx]);
            }
        }

        // ── Step 9: <<include>> CT-02 ─────────────────────────
        System.out.print("\n[보험료 산출] (Enter): ");
        sc.nextLine();

        List<String> selectedCoverageNames = new ArrayList<>();
        for (int idx : selectedCoverageIdxs) selectedCoverageNames.add(COVERAGE_NAMES[idx]);

        long[] premiumResult = new CT02PremiumCalculation().runAsInclude(productName, selectedCoverageNames, saleEnd);
        if (premiumResult == null) { returnToMenu(); return; }

        long finalPremium = premiumResult[0];
        long reserve      = premiumResult[1];

        // ── Step 10: 최종 결과 ────────────────────────────────
        System.out.println("\n── 최종 산출 결과 ──────────────────────");
        System.out.printf(" 기본형 최종 보험료   : %,d원%n", finalPremium);
        System.out.printf(" 준비금 적립 필요액   : %,d원%n", reserve);

        // ── Step 11: 상품 확정 ────────────────────────────────
        System.out.print("\n[상품 확정] (Enter): ");
        sc.nextLine();

        // Product 객체 생성 및 저장
        Product product = new Product();
        product.setProductId("PROD-" + System.currentTimeMillis());
        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setDescription(description);
        product.setTarget(target);
        product.setLineOfBusiness(Product.LineOfBusiness.AUTO);
        product.setSaleStartDate(saleStart);
        product.setSaleEndDate(saleEnd);
        product.setDocuments(new ArrayList<>());
        product.setCoverages(new ArrayList<>());
        product.setRiders(buildProductRiders(selectedRiderIdxs));
        product.completeDesign();
        product.setCreatedAt(new Date());
        productRepo.save(product);

        // ── Step 12: 완료 팝업 ────────────────────────────────
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│    상품 설계가 완료되었습니다.         │");
        System.out.println("└──────────────────────────────────────┘");
        returnToMenu();
    }

    private List<ProductRider> buildProductRiders(List<Integer> idxs) {
        List<ProductRider> list = new ArrayList<>();
        for (int idx : idxs) {
            ProductRider pr = new ProductRider();
            pr.setProductRiderId("PR-NEW-" + idx);
            pr.setRiderName(RIDER_NAMES[idx]);
            pr.setRiderCode("RC-NEW-" + idx);
            list.add(pr);
        }
        return list;
    }

    private List<Integer> parseNumbers(String input, int max) {
        List<Integer> result = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) return result;
        for (String s : input.split(",")) {
            try {
                int n = Integer.parseInt(s.trim()) - 1;
                if (n >= 0 && n < max && !result.contains(n)) result.add(n);
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private void returnToMenu() {
        System.out.print("\nEnter를 누르면 메인 메뉴로 돌아갑니다...");
        sc.nextLine();
        System.out.println();
    }
}

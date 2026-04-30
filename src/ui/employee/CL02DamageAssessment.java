package ui.employee;

import domain.Claim;
import infra.Context;
import java.util.Scanner;

public class CL02DamageAssessment {
    private final Scanner sc = Context.getInstance().scanner();

    private static final int LIMIT_PROPERTY = 2000; // 대물 한도 (만원)

    public void run() {
        System.out.println("\n[CL-02] 손해액을 산정한다");
        System.out.println("========================================");

        // Step 2: 입력란 출력 / Step 3 + E1/A1 발생 시 재시작
        while (true) {
            System.out.println("\n[ 손해액 산정 조회 ]");
            System.out.print("담당자 사번 (예: EMP-1023): ");
            String empNo = sc.nextLine().trim();
            System.out.print("접수 번호   (예: ACC-2026-001): ");
            String accNo = sc.nextLine().trim();
            System.out.println("[산정 대상 조회]");

            // Step 4: 사고 초기 접수 내역 출력
            System.out.println("\n[ 손해액 산정 폼 - 사고 초기 접수 내역 ]");
            System.out.println("------------------------------------------------------------");
            System.out.println("  접수 번호  : " + accNo);
            System.out.println("  담당자     : " + empNo);
            System.out.println("  사고 일시  : 2026-04-19 08:45");
            System.out.println("  사고 유형  : 자동차 대물 사고");
            System.out.println("  사고 장소  : 서울 강남구 테헤란로");
            System.out.println("  피해 차량  : 12가 3456 (현대 소나타)");
            System.out.println("------------------------------------------------------------");

            // Step 5 + A1: 피해 내역 조사 진행 여부
            System.out.print("\n조사 진행 여부를 입력하세요 (Y/N): ");
            String proceed = sc.nextLine().trim();
            System.out.println("[피해 내역 조사 실행]");

            // A1: 조사 미완료
            if (!"Y".equalsIgnoreCase(proceed)) {
                System.out.println("\n[경고] 손해 조사가 완료되지 않아 산정을 진행할 수 없습니다. 조사를 완료해 주세요.\n");
                continue;
            }

            // <<include>> CL-03
            new CL03DamageInvestigation().run();

            // Step 6: 보상 한도액 및 입력란 출력 (CL-03 완료 후 진입)
            System.out.println("\n[ 보상 한도액 ]");
            System.out.println("------------------------------------------------------------");
            System.out.println("  대인 한도 : 1,000만원");
            System.out.println("  대물 한도 : " + LIMIT_PROPERTY + "만원");
            System.out.println("------------------------------------------------------------");

            // Step 7 + E1: 합의금·자기부담금 입력
            System.out.println("\n[ 최종 손해액 산출 ]");
            System.out.print("최종 합의금 (만원 단위 숫자, 예: 1500): ");
            String settlementInput = sc.nextLine().trim();
            System.out.print("자기부담금  (만원 단위 숫자, 예: 20): ");
            String deductibleInput = sc.nextLine().trim();
            System.out.println("[최종 손해액 산출]");

            int settlement;
            int deductible;
            try {
                settlement = Integer.parseInt(settlementInput);
                deductible = Integer.parseInt(deductibleInput);
            } catch (NumberFormatException e) {
                System.out.println("\n[오류] >>> 합의금 / 자기부담금 <<< 입력된 합의금 한도 값이 허용 범위를 초과하였습니다.\n");
                continue;
            }

            // E1: 대물 한도 초과
            if (settlement > LIMIT_PROPERTY) {
                System.out.println("\n[오류] >>> 최종 합의금 <<< 입력된 합의금 한도 값이 허용 범위를 초과하였습니다.");
                System.out.println("       대물 한도(" + LIMIT_PROPERTY + "만원) 이하로 입력해 주세요.\n");
                continue;
            }

            // Step 8: 최종 결정 손해액 출력
            int finalAmount = settlement - deductible;
            System.out.println("\n[ 최종 결정 손해액 ]");
            System.out.println("------------------------------------------------------------");
            System.out.println("  최종 합의금     : " + settlement + "만원");
            System.out.println("  자기부담금 공제  : -" + deductible + "만원");
            System.out.println("  실 지급 보상금   : " + finalAmount + "만원");
            System.out.println("------------------------------------------------------------");

            // Step 9: 담당자 결제 의견 입력
            System.out.println("\n[ 산정 내역 승인 ]");
            System.out.print("담당자 결제 의견 (예: 합의 완료 승인): ");
            String opinion = sc.nextLine().trim();
            System.out.println("[산정 내역 승인]");

            // Step 10: 승인 완료 및 지급 대기 상태 출력
            Claim claim = new Claim();
            claim.updateStatus(null);

            System.out.println("\n[ 산정 내역 승인 완료 ]");
            System.out.println("------------------------------------------------------------");
            System.out.println("  결제 의견  : " + opinion);
            System.out.println("  지급 금액  : " + finalAmount + "만원");
            System.out.println("  상태       : 지급 대기");
            System.out.println("------------------------------------------------------------");

            // Step 11: 보험금 지급 실행 (<<extend>> CL-04)
            System.out.print("\n[보험금 지급 실행] 버튼을 누르려면 Enter를 입력하세요...");
            sc.nextLine();

            new CL04InsurancePayment().run();

            // Step 12: 지급 완료 팝업 (CL-04 완료 후 진입)
            System.out.println("\n┌──────────────────────────────────────────────────┐");
            System.out.println("│  보험금이 지급 완료되었습니다. 상태: 종결        │");
            System.out.println("└──────────────────────────────────────────────────┘");

            break;
        }

        System.out.print("\nEnter를 누르면 메인 메뉴로 돌아갑니다...");
        sc.nextLine();
        System.out.println();
    }
}

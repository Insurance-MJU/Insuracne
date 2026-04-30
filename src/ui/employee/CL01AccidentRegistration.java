package ui.employee;

import domain.Accident;
import domain.Claim;
import infra.Context;
import java.util.Scanner;

public class CL01AccidentRegistration {
    private final Scanner sc = Context.getInstance().scanner();

    public void run() {
        System.out.println("\n[CL-01] 사고를 접수한다");
        System.out.println("========================================");

        // Step 2: 신규사고접수 입력란 출력
        String period;
        String status;

        // Step 3 + E1: 형식 오류 시 재입력
        while (true) {
            System.out.println("\n[ 신규 사고 접수 조회 ]");
            System.out.print("기간 (예: 2026-04-19): ");
            period = sc.nextLine().trim();
            System.out.print("상태 (예: 미처리 / 처리중 / 완료): ");
            status = sc.nextLine().trim();
            System.out.println("[조회]");

            // E1: 날짜 형식 허용 범위 초과
            if (!period.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("\n[오류] >>> 기간 <<< 입력된 사고 정보의 값이 허용 범위를 초과하였습니다.");
                System.out.println("       날짜 형식을 확인해 주세요. (예: 2026-04-19)\n");
                continue;
            }
            break;
        }

        // Step 4: 미처리 사고 청구 목록 출력
        System.out.println("\n[ 미처리 사고 청구 목록 ]");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-22s %-12s %-20s%n", "접수 일시", "고객명", "청구 사유");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-22s %-12s %-20s%n", "2026-04-19 09:32", "홍길동", "자동차 대물 사고");
        System.out.printf("%-22s %-12s %-20s%n", "2026-04-19 11:15", "김철수", "차량 파손");
        System.out.println("------------------------------------------------------------");

        // Step 5 + A1: 고객명·전화번호 필수 검색 조건 검증
        String customerName;
        String phone;

        while (true) {
            System.out.println("\n[ 상세 조회 ]");
            System.out.print("고객명: ");
            customerName = sc.nextLine().trim();
            System.out.print("전화번호: ");
            phone = sc.nextLine().trim();
            System.out.println("[상세 조회]");

            // A1: 필수 조건 누락
            if (customerName.isEmpty() || phone.isEmpty()) {
                System.out.println("\n[경고] 고객명과 전화번호는 필수 검색 조건입니다. 검색 조건을 추가해 주세요.\n");
                continue;
            }
            break;
        }

        // Step 6: 사고 상세 정보 출력
        Accident accident = new Accident();
        accident.getAccidentInfo();

        System.out.println("\n[ 사고 상세 정보 - " + customerName + " / " + phone + " ]");
        System.out.println("------------------------------------------------------------");
        System.out.println("[제출된 사고 경위서]");
        System.out.println("  - 발생일시: 2026-04-19 08:45");
        System.out.println("  - 사고 내용: 신호 대기 중 후방 추돌 사고 발생");
        System.out.println("[증빙 서류 뷰어]");
        System.out.println("  - 사고현장사진.jpg");
        System.out.println("  - 차량수리견적서.pdf");
        System.out.println("[계약 원장 정보]");
        System.out.println("  - 계약번호: CNT-20240315-001");
        System.out.println("  - 담보: 자동차 대물 / 한도: 2,000만원");
        System.out.println("------------------------------------------------------------");

        // Step 7: 배당 담당자 검색 조건 입력
        System.out.println("\n[ 배당 담당자 검색 ]");
        System.out.print("사고 발생 지역 코드 (예: SEOUL-01): ");
        String regionCode = sc.nextLine().trim();
        System.out.print("전문 분야 (예: 자동차 대물): ");
        String specialty = sc.nextLine().trim();
        System.out.println("[배당 담당자 검색]");

        // Step 8: 현장조사역 후보 목록 출력
        System.out.println("\n[ 현장조사역 후보 목록 - " + regionCode + " / " + specialty + " ]");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-14s %-10s%n", "직원 번호", "직원명", "미결 건수");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-14s %-10s%n", "EMP-1023", "이현수", "2건");
        System.out.printf("%-15s %-14s %-10s%n", "EMP-1045", "박지영", "4건");
        System.out.println("------------------------------------------------------------");

        // Step 9: 담당자 배당 확정
        System.out.println("\n[ 배당 및 접수 확정 ]");
        System.out.print("담당자 직원 번호: ");
        String empNo = sc.nextLine().trim();
        System.out.println("[배당 및 접수 확정]");

        // Step 10: 배당 완료 안내
        Claim claim = new Claim();
        claim.createClaim();

        System.out.println("\n담당자가 배당되었습니다.");
        System.out.println("  - 배당 직원: " + empNo);
        System.out.println("  - 접수 번호: CL-" + String.format("%05d", System.currentTimeMillis() % 100000));

        System.out.print("\nEnter를 누르면 메인 메뉴로 돌아갑니다...");
        sc.nextLine();
        System.out.println();
    }
}

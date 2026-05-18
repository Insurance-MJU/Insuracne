SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS insurance_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE insurance_db;

-- ─────────────────────────────────────────────────────────────────
-- users
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    user_id  VARCHAR(50)  NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (user_id, password, name, role) VALUES
    ('customer1', '1234', '박수현', 'CUSTOMER'),
    ('employee1', '1234', '김직원', 'EMPLOYEE'),
    ('admin1',    '1234', '관리자', 'ADMIN')
ON DUPLICATE KEY UPDATE password=VALUES(password), name=VALUES(name), role=VALUES(role);

-- ─────────────────────────────────────────────────────────────────
-- accidents
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS accidents (
    accident_id           VARCHAR(50)  NOT NULL PRIMARY KEY,
    accident_date         DATETIME,
    reported_by           VARCHAR(100),
    phone                 VARCHAR(30),
    description           VARCHAR(500),
    accident_location     VARCHAR(300),
    accident_detail       VARCHAR(1000),
    documents             VARCHAR(500),
    contract_id           VARCHAR(50),
    coverage_description  VARCHAR(200),
    coverage_limit        BIGINT DEFAULT 0,
    personal_injury_limit BIGINT DEFAULT 0,
    vehicle_info          VARCHAR(200),
    expected_repair_cost  BIGINT DEFAULT 0,
    region_code           VARCHAR(50),
    status                VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO accidents (accident_id, accident_date, reported_by, phone, description,
    accident_location, accident_detail, documents, contract_id, coverage_description,
    coverage_limit, personal_injury_limit, vehicle_info, expected_repair_cost, region_code, status)
VALUES
    ('ACC-2026-001', '2026-04-19 09:32:00', '홍길동', '010-1234-5678',
     '자동차 대물 사고', '서울 강남구 테헤란로', '신호 대기 중 후방 추돌 사고 발생',
     '사고현장사진.jpg,차량수리견적서.pdf', 'CNT-20240315-001', '자동차 대물',
     20000000, 10000000, '12가 3456 (현대 소나타)', 850000, 'SEOUL-01', 'PENDING'),
    ('ACC-2026-002', '2026-04-19 11:15:00', '김철수', '010-9876-5432',
     '차량 파손', '경기도 수원시 팔달구', '주차장 내 차량 문 충돌로 인한 파손',
     '차량파손사진.jpg,수리견적서.pdf', 'CNT-20240520-002', '자기차량손해',
     30000000, 20000000, '34나 5678 (기아 K5)', 1200000, 'GYEONGGI-01', 'PENDING'),
    ('ACC-2026-003', '2026-04-18 14:20:00', '이영희', '010-5555-1234',
     '차량 전손', '인천시 부평구 경인로', '교차로 신호 위반으로 인한 정면 충돌',
     '사고사진.jpg,전손감정서.pdf', 'CNT-20231210-003', '자기차량손해',
     50000000, 30000000, '56다 9012 (현대 그랜저)', 3500000, 'INCHEON-01', 'IN_PROGRESS')
ON DUPLICATE KEY UPDATE
    accident_date=VALUES(accident_date), reported_by=VALUES(reported_by), phone=VALUES(phone),
    description=VALUES(description), accident_location=VALUES(accident_location),
    accident_detail=VALUES(accident_detail), documents=VALUES(documents),
    contract_id=VALUES(contract_id), coverage_description=VALUES(coverage_description),
    coverage_limit=VALUES(coverage_limit), personal_injury_limit=VALUES(personal_injury_limit),
    vehicle_info=VALUES(vehicle_info), expected_repair_cost=VALUES(expected_repair_cost),
    region_code=VALUES(region_code), status=VALUES(status);

-- ─────────────────────────────────────────────────────────────────
-- claims
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS claims (
    claim_id            VARCHAR(50)  NOT NULL PRIMARY KEY,
    accident_id         VARCHAR(50),
    claimant_name       VARCHAR(100),
    claim_date          DATETIME,
    contract_id         VARCHAR(50),
    description         VARCHAR(500),
    claim_status        VARCHAR(30),
    assigned_employee   VARCHAR(50),
    settlement_amount   BIGINT DEFAULT 0,
    deductible_amount   BIGINT DEFAULT 0,
    compensation_amount BIGINT DEFAULT 0,
    bank_name           VARCHAR(100),
    account_number      VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO claims (claim_id, accident_id, claimant_name, claim_date, contract_id,
    description, claim_status, assigned_employee,
    settlement_amount, deductible_amount, compensation_amount)
VALUES
    ('CL-00001', 'ACC-2026-003', '이영희', '2026-04-18 00:00:00', 'CNT-20231210-003',
     '차량 전손', 'PAYMENT_PENDING', 'EMP-1023',
     14800000, 0, 14800000)
ON DUPLICATE KEY UPDATE
    accident_id=VALUES(accident_id), claimant_name=VALUES(claimant_name),
    claim_date=VALUES(claim_date), contract_id=VALUES(contract_id),
    description=VALUES(description), claim_status=VALUES(claim_status),
    assigned_employee=VALUES(assigned_employee), settlement_amount=VALUES(settlement_amount),
    deductible_amount=VALUES(deductible_amount),
    compensation_amount=VALUES(compensation_amount);

-- ─────────────────────────────────────────────────────────────────
-- contracts
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contracts (
    contract_id           VARCHAR(50)  NOT NULL PRIMARY KEY,
    policy_no             VARCHAR(50),
    product_name          VARCHAR(200),
    subscription_no       VARCHAR(50),
    premium               BIGINT DEFAULT 0,
    car_number            VARCHAR(30),
    coverages_description VARCHAR(500),
    coverage_limit        VARCHAR(200),
    riders_description    VARCHAR(500),
    issue_date            DATETIME,
    start_date            DATETIME,
    end_date              DATETIME,
    status                VARCHAR(30),
    holder_name           VARCHAR(100),
    holder_party_id       VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO contracts (contract_id, policy_no, product_name, premium, car_number,
    coverages_description, coverage_limit, riders_description,
    issue_date, start_date, end_date, status, holder_name, holder_party_id)
VALUES
    ('CNT-20240315-001', 'IN-2026-001', 'MZ세대 다이렉트 개인용자동차보험', 2509200, '64마0866',
     '대인배상I, 대인배상II, 대물배상', '', '마일리지 특약',
     '2026-04-01 00:00:00', '2026-04-01 00:00:00', '2027-04-01 00:00:00',
     'ACTIVE', '박수현', 'PARTY-CNT-20240315-001'),
    ('CNT-20240520-002', 'IN-2025-002', 'MZ세대 다이렉트 개인용자동차보험', 1980000, '12가3456',
     '대인배상I, 대인배상II, 대물배상, 자기차량손해', '', '블랙박스 할인특약',
     '2025-06-15 00:00:00', '2025-06-15 00:00:00', '2026-06-15 00:00:00',
     'ACTIVE', '김직원', 'PARTY-CNT-20240520-002'),
    ('CNT-20231210-003', 'IN-2023-003', 'MZ세대 다이렉트 개인용자동차보험', 2100000, '56다9012',
     '대인배상I, 대물배상, 자기차량손해', '', '없음',
     '2023-12-10 00:00:00', '2023-12-10 00:00:00', '2024-12-10 00:00:00',
     'EXPIRED', '이영희', 'PARTY-CNT-20231210-003')
ON DUPLICATE KEY UPDATE
    policy_no=VALUES(policy_no), product_name=VALUES(product_name),
    premium=VALUES(premium), car_number=VALUES(car_number),
    coverages_description=VALUES(coverages_description), coverage_limit=VALUES(coverage_limit),
    riders_description=VALUES(riders_description), issue_date=VALUES(issue_date),
    start_date=VALUES(start_date), end_date=VALUES(end_date),
    status=VALUES(status), holder_name=VALUES(holder_name), holder_party_id=VALUES(holder_party_id);

-- ─────────────────────────────────────────────────────────────────
-- contract_selected_coverages
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contract_selected_coverages (
    id                 VARCHAR(100) NOT NULL PRIMARY KEY,
    contract_id        VARCHAR(50)  NOT NULL,
    coverage_master_id VARCHAR(50),
    coverage_name      VARCHAR(100),
    mandatory          TINYINT(1) DEFAULT 0,
    deductible_type    VARCHAR(20),
    deductible_amount  BIGINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO contract_selected_coverages
    (id, contract_id, coverage_master_id, coverage_name, mandatory, deductible_type, deductible_amount)
VALUES
    ('CNT-20240315-001-COV-001', 'CNT-20240315-001', 'COV-001', '대인배상 I',  1, 'NONE', 0),
    ('CNT-20240315-001-COV-002', 'CNT-20240315-001', 'COV-002', '대인배상 II', 0, 'NONE', 0),
    ('CNT-20240315-001-COV-003', 'CNT-20240315-001', 'COV-003', '대물배상',    0, 'NONE', 0),
    ('CNT-20240520-002-COV-001', 'CNT-20240520-002', 'COV-001', '대인배상 I',  1, 'NONE', 0),
    ('CNT-20240520-002-COV-002', 'CNT-20240520-002', 'COV-002', '대인배상 II', 0, 'NONE', 0),
    ('CNT-20240520-002-COV-003', 'CNT-20240520-002', 'COV-003', '대물배상',    0, 'NONE', 0),
    ('CNT-20240520-002-COV-005', 'CNT-20240520-002', 'COV-005', '자기차량손해',0, 'FIXED', 200000),
    ('CNT-20231210-003-COV-001', 'CNT-20231210-003', 'COV-001', '대인배상 I',  1, 'NONE', 0),
    ('CNT-20231210-003-COV-003', 'CNT-20231210-003', 'COV-003', '대물배상',    0, 'NONE', 0),
    ('CNT-20231210-003-COV-005', 'CNT-20231210-003', 'COV-005', '자기차량손해',0, 'FIXED', 200000)
ON DUPLICATE KEY UPDATE
    coverage_name=VALUES(coverage_name), mandatory=VALUES(mandatory),
    deductible_type=VALUES(deductible_type), deductible_amount=VALUES(deductible_amount);

-- ─────────────────────────────────────────────────────────────────
-- subscriptions
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS subscriptions (
    subscription_no       VARCHAR(50)  NOT NULL PRIMARY KEY,
    applicant_name        VARCHAR(100),
    ssn                   VARCHAR(20),
    address               VARCHAR(300),
    car_number            VARCHAR(30),
    chassis_number        VARCHAR(50),
    product_name          VARCHAR(200),
    premium               BIGINT DEFAULT 0,
    base_premium          BIGINT DEFAULT 0,
    subscription_date     DATETIME,
    status                VARCHAR(30),
    occupation            VARCHAR(100),
    age                   INT DEFAULT 0,
    coverages_description VARCHAR(500),
    reject_reason         VARCHAR(500),
    supplement_documents  VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO subscriptions (subscription_no, applicant_name, ssn, address, car_number,
    product_name, premium, base_premium, subscription_date, status, age, coverages_description)
VALUES
    ('20260418-0001', '홍길동', '020101-3123456', '서울시 강남구', '64마0866',
     'MZ세대 다이렉트 개인용자동차보험', 2509200, 2200000, '2026-04-18 00:00:00',
     'PENDING_REVIEW', 24, '대인배상I,대인배상II,대물배상')
ON DUPLICATE KEY UPDATE
    applicant_name=VALUES(applicant_name), ssn=VALUES(ssn), address=VALUES(address),
    car_number=VALUES(car_number), product_name=VALUES(product_name),
    premium=VALUES(premium), base_premium=VALUES(base_premium),
    subscription_date=VALUES(subscription_date), status=VALUES(status),
    age=VALUES(age), coverages_description=VALUES(coverages_description);

-- ─────────────────────────────────────────────────────────────────
-- coverages (master)
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS coverages (
    coverage_id   VARCHAR(50)  NOT NULL PRIMARY KEY,
    coverage_name VARCHAR(100) NOT NULL,
    coverage_type VARCHAR(50),
    mandatory     TINYINT(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO coverages (coverage_id, coverage_name, coverage_type, mandatory) VALUES
    ('COV-001', '대인배상 I',   'PERSONAL_INJURY_MANDATORY', 1),
    ('COV-002', '대인배상 II',  'PERSONAL_INJURY_OPTIONAL',  0),
    ('COV-003', '대물배상',     'PROPERTY_DAMAGE',           0),
    ('COV-004', '자동차상해',   'AUTO_INJURY',               0),
    ('COV-005', '자기차량손해', 'OWN_VEHICLE_DAMAGE',        0),
    ('COV-006', '무보험차상해', 'UNINSURED_VEHICLE',         0)
ON DUPLICATE KEY UPDATE coverage_name=VALUES(coverage_name), coverage_type=VALUES(coverage_type), mandatory=VALUES(mandatory);

-- ─────────────────────────────────────────────────────────────────
-- coverage_limit_options
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS coverage_limit_options (
    option_id          VARCHAR(50) NOT NULL PRIMARY KEY,
    coverage_master_id VARCHAR(50) NOT NULL,
    seq                INT DEFAULT 1,
    option_name        VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO coverage_limit_options (option_id, coverage_master_id, seq, option_name) VALUES
    ('OPT-001-1', 'COV-001', 1, '기본 옵션'),
    ('OPT-002-1', 'COV-002', 1, '한도5억'),
    ('OPT-002-2', 'COV-002', 2, '무한'),
    ('OPT-003-1', 'COV-003', 1, '기본옵션'),
    ('OPT-004-1', 'COV-004', 1, '기본옵션'),
    ('OPT-005-1', 'COV-005', 1, '기본옵션'),
    ('OPT-006-1', 'COV-006', 1, '기본옵션')
ON DUPLICATE KEY UPDATE option_name=VALUES(option_name), seq=VALUES(seq);

-- ─────────────────────────────────────────────────────────────────
-- employees (field investigators)
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employees (
    employee_id     VARCHAR(50)  NOT NULL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    specialty       VARCHAR(100),
    open_case_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO employees (employee_id, name, specialty, open_case_count) VALUES
    ('EMP-1023', '김현수', '자동차 대물',  3),
    ('EMP-1024', '이수진', '자동차 대인',  1),
    ('EMP-1025', '박민준', '자기차량손해', 5),
    ('EMP-1026', '최영희', '자동차 대물',  2)
ON DUPLICATE KEY UPDATE name=VALUES(name), specialty=VALUES(specialty), open_case_count=VALUES(open_case_count);

-- ─────────────────────────────────────────────────────────────────
-- riders
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS riders (
    rider_id      VARCHAR(50)  NOT NULL PRIMARY KEY,
    rider_code    VARCHAR(50)  NOT NULL,
    rider_name    VARCHAR(100) NOT NULL,
    description   TEXT,
    rider_type    VARCHAR(30),
    mandatory     TINYINT(1) DEFAULT 0,
    discount_rate DOUBLE DEFAULT 0.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO riders (rider_id, rider_code, rider_name, description, rider_type, mandatory, discount_rate) VALUES
    ('R-001', 'RC-MILEAGE',  '마일리지 특약',
     '연간 주행거리 1만km 이하 시 보험료 10% 할인', 'MILEAGE', 0, 0.10),
    ('R-002', 'RC-SAFETY',   '안전장치 할인특약',
     '에어백·ABS 장착 차량 보험료 5% 할인', 'SAFETY', 0, 0.05),
    ('R-003', 'RC-BLACKBOX', '블랙박스 할인특약',
     '블랙박스 장착 차량 보험료 3% 할인', 'DISCOUNT', 0, 0.03)
ON DUPLICATE KEY UPDATE
    rider_code=VALUES(rider_code), rider_name=VALUES(rider_name), description=VALUES(description),
    rider_type=VALUES(rider_type), mandatory=VALUES(mandatory), discount_rate=VALUES(discount_rate);

-- ─────────────────────────────────────────────────────────────────
-- products
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS products (
    product_id       VARCHAR(50)  NOT NULL PRIMARY KEY,
    product_code     VARCHAR(50)  NOT NULL,
    product_name     VARCHAR(200) NOT NULL,
    description      TEXT,
    line_of_business VARCHAR(30),
    sale_start_date  DATETIME,
    sale_end_date    DATETIME,
    status           VARCHAR(30),
    target           VARCHAR(30),
    created_at       DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO products (product_id, product_code, product_name, description, line_of_business,
    sale_start_date, sale_end_date, status, target, created_at)
VALUES
    ('PROD-001', 'AUTO-MZ-2024', 'MZ세대 다이렉트 개인용자동차보험',
     'MZ 세대를 위한 다이렉트 자동차보험입니다.',
     'AUTO', '2024-01-01 00:00:00', '2027-12-31 00:00:00', 'ON_SALE', 'PERSONAL', NOW())
ON DUPLICATE KEY UPDATE
    product_code=VALUES(product_code), product_name=VALUES(product_name),
    description=VALUES(description), line_of_business=VALUES(line_of_business),
    sale_start_date=VALUES(sale_start_date), sale_end_date=VALUES(sale_end_date),
    status=VALUES(status), target=VALUES(target);

-- ─────────────────────────────────────────────────────────────────
-- product_coverages
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_coverages (
    product_coverage_id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id          VARCHAR(50)  NOT NULL,
    coverage_master_id  VARCHAR(50),
    coverage_name       VARCHAR(100),
    coverage_type       VARCHAR(50),
    mandatory           TINYINT(1) DEFAULT 0,
    limit_amount        BIGINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO product_coverages (product_coverage_id, product_id, coverage_master_id, coverage_name, mandatory) VALUES
    ('PC-PROD-001-COV-001', 'PROD-001', 'COV-001', '대인배상 I',   1),
    ('PC-PROD-001-COV-002', 'PROD-001', 'COV-002', '대인배상 II',  0),
    ('PC-PROD-001-COV-003', 'PROD-001', 'COV-003', '대물배상',     0),
    ('PC-PROD-001-COV-004', 'PROD-001', 'COV-004', '자동차상해',   0),
    ('PC-PROD-001-COV-005', 'PROD-001', 'COV-005', '자기차량손해', 0),
    ('PC-PROD-001-COV-006', 'PROD-001', 'COV-006', '무보험차상해', 0)
ON DUPLICATE KEY UPDATE coverage_name=VALUES(coverage_name), mandatory=VALUES(mandatory);

-- ─────────────────────────────────────────────────────────────────
-- product_riders
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_riders (
    product_rider_id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id       VARCHAR(50)  NOT NULL,
    rider_id         VARCHAR(50),
    rider_code       VARCHAR(50),
    rider_name       VARCHAR(100),
    discount_rate    DOUBLE DEFAULT 0.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO product_riders (product_rider_id, product_id, rider_id, rider_code, rider_name, discount_rate) VALUES
    ('PR-PROD-001-R-001', 'PROD-001', 'R-001', 'RC-MILEAGE',  '마일리지 특약',     0.10),
    ('PR-PROD-001-R-002', 'PROD-001', 'R-002', 'RC-SAFETY',   '안전장치 할인특약', 0.05),
    ('PR-PROD-001-R-003', 'PROD-001', 'R-003', 'RC-BLACKBOX', '블랙박스 할인특약', 0.03)
ON DUPLICATE KEY UPDATE rider_name=VALUES(rider_name), discount_rate=VALUES(discount_rate);

-- ─────────────────────────────────────────────────────────────────
-- product_documents
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_documents (
    product_document_id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id          VARCHAR(50)  NOT NULL,
    doc_type            VARCHAR(50),
    title               VARCHAR(200),
    note                TEXT,
    created_at          DATETIME,
    submitted_at        DATETIME,
    received_at         DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO product_documents (product_document_id, product_id, doc_type, title, note, created_at) VALUES
    ('DOC-001', 'PROD-001', 'GENERAL_TERMS', '보통약관',
     '■ 보통약관\n\n제1조 (보험계약의 성립)\n  보험계약은 계약자가 청약하고 보험자가 승낙함으로써 성립합니다.\n',
     NOW()),
    ('DOC-002', 'PROD-001', 'SPECIAL_TERMS', '특별약관',
     '■ 특별약관\n\n제1조 (마일리지 특약)\n  연간 주행거리에 따라 보험료를 환급합니다.\n',
     NOW())
ON DUPLICATE KEY UPDATE title=VALUES(title), note=VALUES(note);

-- ─────────────────────────────────────────────────────────────────
-- damage_investigations
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS damage_investigations (
    investigation_id     VARCHAR(50)  NOT NULL PRIMARY KEY,
    accident_id          VARCHAR(50)  NOT NULL,
    claim_id             VARCHAR(50),
    investigator_name    VARCHAR(100),
    investigation_date   DATETIME,
    opinion              VARCHAR(1000),
    damage_code          VARCHAR(50),
    injury_grade         INT DEFAULT 0,
    our_fault            INT DEFAULT 0,
    other_fault          INT DEFAULT 0,
    liability            VARCHAR(200),
    expected_repair_cost BIGINT DEFAULT 0,
    compensation_limit   BIGINT DEFAULT 0,
    final_opinion        VARCHAR(1000),
    saved_at             DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─────────────────────────────────────────────────────────────────
-- risk_analysis_reports
-- ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS risk_analysis_reports (
    subscription_no         VARCHAR(50) NOT NULL PRIMARY KEY,
    risk_score              DOUBLE DEFAULT 0.0,
    risk_grade              INT DEFAULT 0,
    accident_score          DOUBLE DEFAULT 0.0,
    driving_exp_score       DOUBLE DEFAULT 0.0,
    credit_grade_score      DOUBLE DEFAULT 0.0,
    traffic_violation_score DOUBLE DEFAULT 0.0,
    surcharge_rate          DOUBLE DEFAULT 0.0,
    base_premium            BIGINT DEFAULT 0,
    surcharge_amount        BIGINT DEFAULT 0,
    total_premium           BIGINT DEFAULT 0,
    review_guide            VARCHAR(500),
    reviewer_name           VARCHAR(100),
    review_date             DATETIME,
    review_opinion          VARCHAR(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

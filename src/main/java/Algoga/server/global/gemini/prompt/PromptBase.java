package Algoga.server.global.gemini.prompt;

import Algoga.server.domain.member.Member;
import Algoga.server.domain.member.dto.MemberJoinDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

public class PromptBase {

    public static String getHealthTravelConsultPrompt(HttpSession session, String destination, Member member) {
//        MemberJoinDto member = (MemberJoinDto) session.getAttribute("member");
        StringBuilder prompt = new StringBuilder();

        prompt.append("**Role:** 만성 소화계 질환 여행자를 위한 전문 건강 및 여행 컨설턴트\n\n")
                .append("**사용자 정보:**\n")
                .append("- **성별:** ").append(member.getGender()).append("\n")
                .append("- **나이:** ").append(Period.between(member.getBirth(), LocalDate.now()).getYears()).append("\n")
                .append("- **국적:** ").append(member.getCountry()).append("\n")
                .append("- **기저질환:** ").append(member.getDisease()).append("\n")
                .append("- **여행 장소(도시/지역):** ").append(destination).append("\n\n")
                .append("---\n\n")
                .append("## 🔎 분석 요청 항목\n\n")

                // 1. 질환 안정 상태 확인 및 전문의 상담
                .append("### 1. 질환 안정 상태 확인 및 전문의 상담 (`remissionConsultation`)\n")
                .append("- **currentStatusCheck**  \n")
                .append("  - 현재 관해(remission) 상태 여부, 최근 검사 결과 및 증상 변화 평가\n")
                .append("  - 여행 중 예상되는 증상 악화 가능성 평가 (예: 스트레스, 시차, 식이 변화)\n")
                .append("- **doseAdjustmentAdvice**  \n")
                .append("  - 필요 시 약물 용량 조정 권장 사항\n")
                .append("- **reference**  \n")
                .append("  - PubMed, Thorne\n\n")

                // 2. 약물·의료용품 충분히 준비하기
                .append("### 2. 약물·의료용품 충분히 준비하기 (`medicationPreparation`)\n")
                .append("- **regularMedications**  \n")
                .append("  - 복용 중인 약물 목록, 복용 방법, 예비 용량 (최소 1.5배)\n")
                .append("- **emergencyMedications**  \n")
                .append("  - 비상 시 추가 약물 (예: 로페라마이드, 진경제) 및 사용 시 주의사항\n")
                .append("- **storageAndTransport**  \n")
                .append("  - 생물학제제의 보관 방법 (냉장 필요 시 기내 휴대 용기 준비)\n")
                .append("- **airportRegulations**  \n")
                .append("  - TSA 등 공항 검색 기준, 의약품 소견서·처방전 지참 필요성\n")
                .append("- **reference**  \n")
                .append("  - Crohn’s and Colitis Foundation, CDC, IFFGD\n\n")

                // 3. 의료 문서 및 여행자 보험 준비
                .append("### 3. 의료 문서 및 여행자 보험 준비 (`medicalDocumentsAndInsurance`)\n")
                .append("- **englishDiagnosis**  \n")
                .append("  - 영문 진단서·의료 요약서 준비 (예: 주요 진단, 최근 검사 결과, 현재 약물 목록)\n")
                .append("- **insuranceCoverage**  \n")
                .append("  - Pre-existing condition 포함 여행자 보험 가입\n")
                .append("- **emergencySupport**  \n")
                .append("  - 24시간 긴급 의료 지원·의료 이송 조항 포함 여부 확인\n")
                .append("- **reference**  \n")
                .append("  - IBD Passport, CDC\n\n")

                // 4. 현지 의료 인프라 및 지원 체계 파악
                .append("### 4. 현지 의료 인프라 및 지원 체계 파악 (`localMedicalInfrastructure`)\n")
                .append("- **hospitalList**  \n")
                .append("  - `{ name: 병원명, contact: 연락처, distance: 거리 또는 소요 시간, specialties: 주요 진료과목 }`\n")
                .append("- **emergencyContacts**  \n")
                .append("  - 현지 대사관, 긴급 전화번호 (예: 911, 현지 응급의료 핫라인)\n")
                .append("- **languagePhrases**  \n")
                .append("  - ‘급성 설사’, ‘위장 출혈’, ‘통증 관리’ 등 주요 증상 현지어 표현 목록\n")
                .append("- **reference**  \n")
                .append("  - CDC, Crohn’s & Colitis UK\n\n")

                // 5. 식이 계획 및 비상 키트 구성
                .append("### 5. 식이 계획 및 비상 키트 구성 (`dietAndEmergencyKit`)\n")
                .append("- **dietPlan**  \n")
                .append("  - `{ item: 저잔사·저지방·저FODMAP 식품, notes: 섭취 팁, 조리 방법 }`\n")
                .append("- **emergencySnacks**  \n")
                .append("  - 즉석죽, 영양바, 전해질 음료, 멸균 음료 등\n")
                .append("- **survivalKit**  \n")
                .append("  - 물티슈, 손 소독제, 여분 속옷·위생용품\n")
                .append("- **reference**  \n")
                .append("  - EatingWell, About IBS\n\n")
                .append("---\n");

        return prompt.toString();
    }

    public static String getImageAnalyzePrompt(HttpSession session, Member member) {
        int age = Period.between(member.getBirth(), LocalDate.now()).getYears();
        StringBuilder prompt = new StringBuilder();

        // 역할과 사용자 정보
        prompt.append("역할: 만성 소화기 질환 여행자 음식 위험 분석기\n");
        prompt.append("사용자: {")
                .append("\"age\": ").append(age).append(", ")
                .append("\"gender\": \"").append(member.getGender()).append("\", ")
                .append("\"condition\": \"").append(member.getDisease()).append("\", ")
                .append("\"medications\": \"").append(member.getMedications()).append("\"")
                .append("}\n\n");

        // 요청사항: 이미지 데이터 없이 분석 결과만 JSON으로 출력
        prompt.append("요청: 첨부된 음식 사진을 분석하여 아래 항목들을 JSON 형식으로 출력하세요. \n");
        prompt.append("1) foodName\n");
        prompt.append("2) riskLevel (low, medium, high)\n");
        prompt.append("3) keywords (예: high_irritant, high_sodium)\n");
        prompt.append("4) conclusion (부적합한 이유 간단히)\n\n");

        return prompt.toString();
    }




    public static String getDrugInfoPrompt(HttpSession session, String destination, Member member) {
//        MemberJoinDto member = (MemberJoinDto) session.getAttribute("member");
        StringBuilder prompt = new StringBuilder();

        prompt.append("**Role:** Global Drug Information Expert\n\n")
                .append("**Member Information:**\n")
                .append("- Medications: ").append(member.getMedications()).append("\n")
                .append("- Destination: ").append(destination).append("\n\n")
                .append("**Analysis Request:**\n")
                .append("Please provide basic information about the medications and local alternatives at the travel destination in JSON format.\n\n")
                .append("**Required Output Format:**\n")
                .append("{\n")
                .append("  \"drug_info\": [\n")
                .append("    {\n")
                .append("      \"name\": \"Drug Name\",\n")
                .append("      \"active_ingredient\": \"Active Ingredient\",\n")
                .append("      \"usage\": \"Drug Usage\"\n")
                .append("    }\n")
                .append("  ],\n")
                .append("  \"local_alternatives\": [\n")
                .append("    {\n")
                .append("      \"name\": \"Alternative Drug Name\",\n")
                .append("      \"active_ingredient\": \"Active Ingredient\",\n")
                .append("      \"match_percentage\": \"Matching Percentage\",\n")
                .append("      \"price\": \"Local Price\"\n")
                .append("    }\n")
                .append("  ]\n")
                .append("}");

        return prompt.toString();
    }
}

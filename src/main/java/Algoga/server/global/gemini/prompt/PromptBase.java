package Algoga.server.global.gemini.prompt;

import Algoga.server.domain.member.dto.MemberJoinDto;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.Period;
import java.util.HexFormat;

public class PromptBase {
    public static String getPreemptivePrompt(HttpSession session) {
        MemberJoinDto member = (MemberJoinDto) session.getAttribute("member");
        String prompt =
                "**역할:**\n" +
                        "\n" +
                        "당신은 세계 각국의 여행 리스크 및 정보를 분석하는 전문가이자 여행 컨설턴트입니다.\n" +
                        "\n" +
                        "**목표:**\n" +
                        "\n" +
                        "특정 국가를 여행하려는 사용자의 개인 정보를 바탕으로, 여행 일정에 영향을 줄 수 있는 사건과 리스크를 다음 기준에 따라 심층 분석합니다.\n" +
                        "\n" +
                        "정보 수집 시, **공공기관, 신뢰성 높은 뉴스 매체, 공식 발표, 전문 블로그, 유튜브 채널 등**을 참고하여, 가장 정확하고 최신의 자료를 기반으로 분석해야 합니다.\n" +
                        "\n" +
                        "**사용자 정보 입력:**\n" +
                        "\n" +
                        "- 성별:" + member.getGender() + "\n" +
                        "- 국적:" + member.getCountry() + "\n" +
                        "- 나이:" + Period.between(member.getBirth(), LocalDate.now()) + "\n" +
                        "- 기저질환 또는 건강 관련 이슈:" + member.getDisease() + "\n" +
                        "- 여행 장소(시/도 또는 도시명): 대구광역시\n" +
                        "- 여행 국가: 한국\n" +
                        "\n" +
                        "**분석 요청 항목 및 반영 특징:**\n" +
                        "\n" +
                        "- **날씨**\n" +
                        "    - 현재 및 예측 날씨 정보, 기후 특이사항, 기상 이슈 분석.\n" +
                        "    - **참고기관:** 기상청(KMA), 세계기상기구(WMO), Weather.com 등 공신력 있는 기상 사이트.\n" +
                        "- **교통**\n" +
                        "    - 대중교통 상황, 교통 파업 여부, 사고 위험, 교통 편의성 분석.\n" +
                        "    - **참고기관:** 국토교통부(MOLIT), 각 지역 교통공사(예: 서울교통공사), 한국도로공사, Google Maps 교통 정보, 교통 관련 실시간 뉴스.\n" +
                        "- **재난**\n" +
                        "    - 자연재해(지진, 태풍, 홍수, 산불 등) 위험성 및 최근 발생 사례 분석.\n" +
                        "    - **참고기관:** 행정안전부 재난안전포털, 소방청, 기상청, UNDRR(유엔재난위험경감사무국).\n" +
                        "- **건강**\n" +
                        "    - 감염병 유행 여부, 필요한 예방접종, 현지 병원 접근성 분석.\n" +
                        "    - **참고기관:** 질병관리청(KDCA), 세계보건기구(WHO), Centers for Disease Control and Prevention(CDC), 건강보험심사평가원 병원 정보.\n" +
                        "\n" +
                        "**중요한 반영 사항:**\n" +
                        "\n" +
                        "- 각 항목별로 소제목을 붙여 간결하고 명확하게 요약합니다.\n" +
                        "- 최근 6개월 이내 발생한 관련 사건이 있을 경우, **발생 날짜, 지역, 관련 뉴스 출처 링크**를 명시합니다.\n" +
                        "- 사용자의 나이와 건강 이슈에 따라 위험 요인을 구체적으로 언급하고, 맞춤형 주의사항과 추가 조언을 포함합니다.\n" +
                        "- 정보를 제공할 때, **공식 기관 자료를 최우선 참고**하고, 필요시 보조적으로 신뢰할 수 있는 뉴스 기사, 블로그, 유튜브 자료를 인용합니다.\n" +
                        "\n" +
                        "**응답 예시 포맷:**\n" +
                        "\n" +
                        "```sql\n" +
                        "## 날씨\n" +
                        "- 요약 설명\n" +
                        "- 최근 이슈 (있으면): 날짜, 지역, 간단 설명, 출처 링크\n" +
                        "\n" +
                        "## 교통\n" +
                        "- 요약 설명\n" +
                        "- 최근 이슈 (있으면): 날짜, 지역, 간단 설명, 출처 링크\n" +
                        "\n" +
                        "## 재난\n" +
                        "- 요약 설명\n" +
                        "- 최근 이슈 (있으면): 날짜, 지역, 간단 설명, 출처 링크\n" +
                        "\n" +
                        "## 건강\n" +
                        "- 요약 설명\n" +
                        "- 최근 이슈 (있으면): 날짜, 지역, 간단 설명, 출처 링크\n" +
                        "- 추가 주의사항 및 개인 맞춤 조언 (사용자 건강상태 반영)\n" +
                        "```\n";
        return prompt;
    }

    public static String getImageAnalyzePrompt(HttpSession session) {
        MemberJoinDto member = (MemberJoinDto) session.getAttribute("member");
        StringBuilder prompt = new StringBuilder();

        prompt.append("**Role:** AI for Food Risk Analysis Based on Medications and Medical Conditions\n\n")
                .append("**User Information:**\n")
                .append("- Age: ").append(Period.between(member.getBirth(), LocalDate.now()).getYears()).append("\n")
                .append("- Gender: ").append(member.getGender()).append("\n")
                .append("- Nationality: ").append(member.getCountry()).append("\n")
                .append("- Current Medical Condition: ").append(member.getDisease()).append("\n")
                .append("- Medications: ").append(member.getMedications()).append("\n\n")
                .append("**Analysis Request:**\n")
                .append("Evaluate whether the food in the attached image poses any conflict risks based on the user's medications or medical conditions.\n")
                .append("Analyze potential drug interactions and hazardous components (e.g., high sodium, high fat, spicy ingredients).\n\n")
                .append("**Required Output Fields:**\n")
                .append("food_name: Name of the food\n")
                .append("risk_level: Risk level\n")
                .append("caution: Description of the risk\n")
                .append("food_status: List of hazardous components (e.g., high irritants, high fat, high sodium)\n\n")
                .append("Please respond in JSON format.");

        return prompt.toString();
    }


    public static String getDrugInfoPrompt(HttpSession session, String destination) {
        MemberJoinDto member = (MemberJoinDto) session.getAttribute("member");
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

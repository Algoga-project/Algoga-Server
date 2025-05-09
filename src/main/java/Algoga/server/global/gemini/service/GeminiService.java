package Algoga.server.global.gemini.service;

import Algoga.server.domain.member.dto.MemberJoinDto;
import Algoga.server.global.gemini.prompt.PromptBase;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GeminiService {
    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String MODEL_NAME = "gemini-1.5-flash-latest";

    public String getDrugAnalyze(HttpSession session, String destination) throws IOException {
        // 프롬프트 준비
        String prompt = PromptBase.getDrugInfoPrompt(session, destination);

        // API 요청 URL 생성
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent?key=" + apiKey;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // HTTP 연결 설정
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // 요청 본문(JSON) 준비 및 전송
        // 문자열 이스케이핑 처리 개선
        String escapedPrompt = prompt.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}";

        // 서버로 데이터 전송
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 서버 응답 읽기
        int responseCode = conn.getResponseCode();

        // 응답 코드에 따라 적절한 스트림 선택
        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        // 응답 데이터 파싱
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // 에러 응답 처리
            if (responseCode >= 400) {
                System.err.println("API Error Response: " + response.toString());
                return "Error analyzing drug data: HTTP " + responseCode + " - " + response.toString();
            }

            // JSON 응답 파싱
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("candidates")) {
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject content = candidates.getJSONObject(i).getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    for (int j = 0; j < parts.length(); j++) {
                        message.append(parts.getJSONObject(j).getString("text")).append("\n");
                    }
                }
                // 최종 결과 반환
                return message.toString().trim();
            } else {
                return "No drug analysis results found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing drug data: " + e.getMessage();
        }
    }


    public String getFoodAnalyze(HttpSession session, MultipartFile imageFile) throws IOException {
        // prompt ready
        String prompt = PromptBase.getImageAnalyzePrompt(session);

        // API request URL generate (공백 제거)
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent?key=" + apiKey;
        urlString = urlString.replaceAll("\\s+", ""); // URL에서 공백 제거

        try {
            // 디버깅 정보 출력
            System.out.println("URL: " + urlString);

            // 이미지 크기 체크
            long imageSize = imageFile.getSize();
            System.out.println("Image size: " + imageSize + " bytes");
            if (imageSize > 20 * 1024 * 1024) { // 20MB 제한
                return "Error: Image size exceeds API limits (max 20MB)";
            }

            // HTTP 연결 준비
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // 이미지 Base64 인코딩
            String base64Img = Base64.getEncoder().encodeToString(imageFile.getBytes());

            // 프롬프트 이스케이프 처리 개선
            String safePrompt = prompt.replace("\\", "\\\\")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\"", "\\\"")
                    .replace("\t", "\\t");

            // JSON 요청 구성 - Gemini API 형식에 맞게 수정
            String jsonInputString = String.format(
                    "{\"contents\":[{\"role\":\"user\",\"parts\":[{\"text\":\"%s\"},{\"inline_data\":{\"mime_type\":\"image/jpeg\",\"data\":\"%s\"}}]}]}",
                    safePrompt, base64Img
            );

            // 디버깅 - 요청 내용 일부 출력 (이미지는 너무 길어서 제외)
            System.out.println("Request JSON (partial): " +
                    jsonInputString.substring(0, Math.min(500, jsonInputString.length())) + "...");

            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 응답 상태 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 오류 응답 처리
            if (responseCode != 200) {
                // 오류 메시지 읽기
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                }
                System.out.println("Error Response: " + errorResponse.toString());
                return "API Error: " + responseCode + " - " + errorResponse.toString();
            }

            // 성공 응답 처리
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // 응답 파싱
                String responseStr = response.toString();
                System.out.println("Response (partial): " +
                        responseStr.substring(0, Math.min(500, responseStr.length())) + "...");

                JSONObject jsonResponse = new JSONObject(responseStr);
                if (jsonResponse.has("candidates")) {
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < candidates.length(); i++) {
                        JSONObject candidate = candidates.getJSONObject(i);
                        if (candidate.has("content")) {
                            JSONObject content = candidate.getJSONObject("content");
                            if (content.has("parts")) {
                                JSONArray parts = content.getJSONArray("parts");
                                for (int j = 0; j < parts.length(); j++) {
                                    JSONObject part = parts.getJSONObject(j);
                                    if (part.has("text")) {
                                        message.append(part.getString("text")).append("\n");
                                    }
                                }
                            }
                        }
                    }
                    return message.toString().trim();
                } else {
                    return "No data analysis results found in response.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error analyzing data: " + e.getMessage();
        }
    }


    public String getPreemptiveGuide(HttpSession session) throws IOException {
        // prompt ready
        String prompt = PromptBase.getPreemptivePrompt(session);

        // API request URL generate
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent?key=" + apiKey;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        // HTTP connection setting
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // 요청 본문(JSON) 준비 및 전송
        String jsonInputString = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                prompt.replace("\n", "\\n")
        );

        //서버로 데이터 전송하는 출력 스트림 가져오기
        //위에서 만든 JsonInputString 바이트 형식으로 input에 저장후
        //이를 os에 기록
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            //바이트 배열을 서버로 전송
            os.write(input, 0, input.length);
        }

        //서버로부터 응답 읽기 및 데이터 파싱
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            //응답 한 줄씩 읽어서 response에 추가
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            //StringBuilder 객체를 jsonResponse로 파싱
            JSONObject jsonResponse = new JSONObject(response.toString());
            //gemini 응답이 candidates 구조 안으로 전송되기 때문에 이를 확인
            if (jsonResponse.has("candidates")) {
                //candidates로부터 JSONArray 형식으로 받기
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject content = candidates.getJSONObject(i).getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    //각 parts에서 text 필드 추출해서 StringBuilder 부분에 추가
                    for (int j = 0; j < parts.length(); j++) {
                        message.append(parts.getJSONObject(j).getString("text")).append("\n");
                    }
                }
                //이후 String으로 변환된 분석 결과 출력
                System.out.println(message.toString().trim());
                return message.toString().trim();
            } else {
                return "No data analysis results found.";
            }
        } catch (Exception e) {
            return "Error analyzing data: " + e.getMessage();
        }
    }
}

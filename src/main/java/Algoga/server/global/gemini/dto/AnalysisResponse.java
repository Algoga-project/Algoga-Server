package Algoga.server.global.gemini.dto;

public class AnalysisResponse {
    private String result;

    public AnalysisResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

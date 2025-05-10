package Algoga.server.global.gemini.controller;
import Algoga.server.global.gemini.service.GeminiService;
import Algoga.server.global.gemini.service.dto.FoodAnalysisDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping(
            value = "/analyze/food/{memberId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FoodAnalysisDto analyzeImage(HttpSession session, @PathVariable Long memberId, @RequestPart("image") MultipartFile image) throws IOException {
        return geminiService.getFoodAnalyze(session, image, memberId);
    }

    @GetMapping("/analyze/drug/{memberId}")
    public String analyzeDrug(HttpSession session, @PathVariable Long memberId, @RequestParam("destination") String destination) throws IOException {
        return geminiService.getDrugAnalyze(session, destination, memberId);
    }

    @GetMapping("/analyze/health-travel/{memberId}")
    public String analyzeHealthTravel(HttpSession session, @PathVariable Long memberId,
                                      @RequestParam("destination") String destination) throws IOException {
        return geminiService.getHealthTravelConsult(session, destination, memberId);
    }
}

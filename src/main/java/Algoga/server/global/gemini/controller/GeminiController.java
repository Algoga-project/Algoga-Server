package Algoga.server.global.gemini.controller;
import Algoga.server.global.gemini.dto.AnalysisResponse;
import Algoga.server.global.gemini.service.GeminiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/analyze/preemption")
    public String analyzePreemptiveData(HttpSession session) throws IOException {
        return geminiService.getPreemptiveGuide(session);
    }

    @PostMapping(
            value = "/analyze/food",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String analyzeImage(HttpSession session, @RequestPart("image") MultipartFile image) throws IOException {
        return geminiService.getFoodAnalyze(session, image);
    }

    @GetMapping("/analyze/drug")
    public String analyzeDrug(HttpSession session, @RequestParam("destination") String destination) throws IOException {
        return geminiService.getDrugAnalyze(session, destination);
    }
}

package Algoga.server.global.gemini.controller;
import Algoga.server.global.gemini.service.GeminiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8000")
public class GeminiController {

    private final GeminiService geminiService;

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

    @GetMapping("/analyze/health-travel")
    public String analyzeHealthTravel(HttpSession session,
                                      @RequestParam("destination") String destination) throws IOException {
        return geminiService.getHealthTravelConsult(session, destination);
    }
}

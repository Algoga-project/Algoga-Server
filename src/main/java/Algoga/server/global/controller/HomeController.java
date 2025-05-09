package Algoga.server.global.controller;

import Algoga.server.domain.member.Country;
import Algoga.server.domain.member.Disease;
import Algoga.server.domain.member.Gender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @GetMapping("/member/join")
    public String joinform() {
        return "member/join";
    }

    @GetMapping("/member/form-data")
    @ResponseBody
    public Map<String, List<String>> getFormData() {
        Map<String, List<String>> formData = new HashMap<>();

        // Gender enum 값들을 문자열 리스트로 변환
        List<String> genders = Arrays.stream(Gender.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Disease enum 값들을 문자열 리스트로 변환
        List<String> diseases = Arrays.stream(Disease.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Country enum 값들을 문자열 리스트로 변환
        List<String> countries = Arrays.stream(Country.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        formData.put("genders", genders);
        formData.put("diseases", diseases);
        formData.put("countries", countries);

        return formData;
    }
}

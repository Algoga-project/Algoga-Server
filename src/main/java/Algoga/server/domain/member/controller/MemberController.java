package Algoga.server.domain.member.controller;

import Algoga.server.domain.member.Member;
import Algoga.server.domain.member.dto.MemberJoinDto;
import Algoga.server.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity<Map<String, Long>> memberJoin(@ModelAttribute MemberJoinDto memberJoinDto, HttpSession session) {
        Member member = memberService.MemberJoin(memberJoinDto);
        Map<String, Long> result = new HashMap<>();
        result.put("memberId", member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/member/{memberId}/travel-locations")
    public ResponseEntity<String> getTravelLocation(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);
        String travelLocation = member.getTravelLocations();
        return ResponseEntity.ok(travelLocation);
    }
}

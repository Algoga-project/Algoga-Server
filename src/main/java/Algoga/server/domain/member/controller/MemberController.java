package Algoga.server.domain.member.controller;

import Algoga.server.domain.member.dto.MemberJoinDto;
import Algoga.server.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/join")
    public void memberJoin(@ModelAttribute MemberJoinDto memberJoinDto, HttpSession session) {
        memberService.MemberJoin(memberJoinDto);

        session.setAttribute("member", memberJoinDto);
    }
}

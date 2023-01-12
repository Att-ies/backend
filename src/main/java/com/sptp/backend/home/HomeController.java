package com.sptp.backend.home;

import com.sptp.backend.member.service.MemberService;
import com.sptp.backend.member.web.dto.request.MemberLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(String username, String password) {
        System.out.println(username + " " + password);
        memberService.login(MemberLoginRequestDto.builder()
                        .userId(username)
                        .password(password)
                        .build());

        return "redirect:/index";
    }
}

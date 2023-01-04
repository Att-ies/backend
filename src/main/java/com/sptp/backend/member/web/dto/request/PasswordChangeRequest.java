package com.sptp.backend.member.web.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequest {

    @Length(min = 8, message = "비밀번호는 8자 이상 입력해주세요.")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
    private String checkPassword;
}

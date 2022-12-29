package com.sptp.backend.email.service;

import com.sptp.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final MemberRepository memberRepository;

    private MimeMessage createMessage(String to, String subject, String text) throws Exception {
        System.out.println("보내는 대상 : " + to);
//        System.out.println("아이디 : " + UserId);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject(subject);//제목

        message.setText(text, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("sgb8154@gmail.com", "Atties"));//보내는 사람

        return message;
    }

    @Override
    public void sendIdMessage(String to) throws Exception {
        String UserId = memberRepository.findByEmail(to).get().getUserId();
        MimeMessage message = createMessage(to, "Atties 아이디 찾기", getIdText(UserId));
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void sendNewPasswordMessage(String email, String password) throws Exception {
        MimeMessage message = createMessage(email, "Atties 임시 비밀번호 발급", getNewPasswordText(password));
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private String getIdText(String UserId) {
        String text = "";
        text += "<div style='margin:20px;'>";
        text += "<h1> 안녕하세요 Atties입니다. </h1>";
        text += "<br>";
        text += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        text += "<h3 style='color:blue;'>아래는 회원님의 아이디입니다.</h3>";
        text += "<div style='font-size:130%'>";
        text += "회원ID : <strong>";
        text += UserId + "</strong><div><br/> ";
        text += "</div>";
        return text;
    }

    private String getNewPasswordText(String password) {
        String text = "";
        text += "<div style='margin:20px;'>";
        text += "<h1> 안녕하세요 Atties입니다. </h1>";
        text += "<br>";
        text += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        text += "<h3 style='color:blue;'>아래는 회원님의 비밀번호입니다.</h3>";
        text += "<div style='font-size:130%'>";
        text += "회원 Password : <strong>";
        text += password + "</strong><div><br/> ";
        text += "</div>";
        return text;
    }
}

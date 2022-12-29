package com.sptp.backend.email.service;

import com.sptp.backend.email.service.EmailService;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final MemberRepository memberRepository;

    private MimeMessage createMessage(String to, String UserId)throws Exception{
        System.out.println("보내는 대상 : "+ to);
        System.out.println("아이디 : "+ UserId);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 Atties입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>아래는 회원님의 아이디입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "회원ID : <strong>";
        msgg+= UserId+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("sgb8154@gmail.com","Atties"));//보내는 사람

        return message;
    }

    @Override
    public void sendMessage(String to)throws Exception {
        String UserId = memberRepository.findByEmail(to).get().getUserId();
        MimeMessage message = createMessage(to, UserId);
        try{
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}

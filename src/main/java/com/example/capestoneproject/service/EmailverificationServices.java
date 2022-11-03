package com.example.capestoneproject.service;

import com.example.capestoneproject.entity.User;
import com.example.capestoneproject.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class EmailverificationServices {
    private final UserRepo userRepo;
    private final JavaMailSender mailSender;
    public void register(User user) throws MessagingException, UnsupportedEncodingException {
        String verifyCode;
        Random r = new Random();
        int n = r.nextInt();
        verifyCode = Integer.toHexString(n);
        user.setVerifyCode(verifyCode);
        userRepo.save(user);
        String url = "http://localhost:8080";
        sendVerificationEmail(user.getEmail(), user.getFirstname(),url,verifyCode);
    }

    private void sendVerificationEmail(String email,String name,String url,String verifyCode) throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "chindrasuKudumbam@gmail.com";
        String senderName = "CapeStone Media";
        String subject = "Please verify your registration - CapeStone Media";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "CapeStone Media";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]",name);
        String verifyURL = url +"/api/verify/"+email+"/?code=" + verifyCode;
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
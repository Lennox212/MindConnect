package com.example.MindConnect.Service;


import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Utils.UtilityClass;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UtilityClass utilityClass;

    @Value("${spring.mail.username")
    private String senderEmail;


    public void sendOtpEmail(UserEntity user, String otp){
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("LOGIN OTP CODE");

            String message = utilityClass.buildOtpEmail(user, otp);

            helper.setText(message, true);

            javaMailSender.send(mimeMessage);




        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }




    }


package com.example.MindConnect.Utils;

import com.example.MindConnect.Entity.UserEntity;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityClass {

    public  String buildOtpEmail(UserEntity user, String otp){
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);'>" +
                "    <h2 style='color: #4a90e2;'>Your OTP Code</h2>" +
                "    <p>Hey " + user.getFirstName() + ",</p>" +
                "    <p>Please use the following One-Time Password (OTP) to complete your verification:</p>" +
                "    <div style='text-align: center; margin: 20px 0;'>" +
                "      <span style='display: inline-block; background-color: #4a90e2; color: #ffffff; padding: 12px 24px; font-size: 24px; font-weight: bold; letter-spacing: 2px; border-radius: 6px;'>" +
                otp +
                "      </span>" +
                "    </div>" +
                "    <p>This OTP is valid for a limited time only. Do not share it with anyone.</p>" +
                "    <p>If you did not request this code, please ignore this email or contact our support team.</p>" +
                "    <br>" +
                "    <p style='color: #888;'>Thank you,<br>The Mind Connect Team</p>" +
                "  </div>" +
                "</body>" +
                "</html>";

    }
}

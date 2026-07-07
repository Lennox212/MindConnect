package com.example.MindConnect.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OtpService {

    public final static Map<String, String> otpStorage = new ConcurrentHashMap<>();


    public String generateOtp(String email){

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email,otp);

        log.info("OTP generate for {}: {}", email, otp);

        return otp;

    }


    public boolean validateOtp(String email, String otp){

        String storedOtp = otpStorage.get(email);

        log.info("Validate OTP for {}: Input OTP = {}, Stored OTP = {}", email, otp, storedOtp);

        return otp != null && otp.trim().equals(storedOtp);

    }

    public void clearOtp(String email){

        otpStorage.remove(email);
        log.info("Cleared OTP for {}", email);


    }





}

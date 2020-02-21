package com.otp.app.service;

public interface OtpService {

    String sendOtp(String mobileNumber);

    String resendOtp(String mobileNumber);

    String verifyOtp(String mobileNumber, String otpValue);

}

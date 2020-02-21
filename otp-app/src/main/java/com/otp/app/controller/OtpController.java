package com.otp.app.controller;

import com.otp.app.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpController {
    @Autowired
    OtpService otpService;

    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody String mobileNumber) {
        String response=otpService.sendOtp(mobileNumber);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<?> resendOtp(@RequestBody String mobileNumber) {
        String response=otpService.resendOtp(mobileNumber);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PutMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String,String> body){
        String mobileNumber=body.get("mobileNumber");
        String otpValue=body.get("otpValue");
        String response=otpService.verifyOtp(mobileNumber,otpValue);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}

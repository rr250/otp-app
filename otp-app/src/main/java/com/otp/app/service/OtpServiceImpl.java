package com.otp.app.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;


import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.otp.app.domain.Otp;
import com.otp.app.domain.infrastructure.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service("otpService")
@Transactional
public class OtpServiceImpl implements OtpService {

    @Value("${otp.message}")
    private  String otpMessage;

    @Value("${otp.senderId}")
    private String otpSenderId;

    @Autowired
    private AmazonSNS amazonSNS;

    @Autowired
    private OtpRepository otpRepository;

    public static void sendSMSMessage(AmazonSNS snsClient, String message,
                                      String phoneNumber, String otpSenderId) {
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue(otpSenderId)
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.50")
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Transactional")
                .withDataType("String"));
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
    }

    @Override
    public String sendOtp(String mobileNumber) {
        if(!mobileNumber.matches("\\d{10}")){
            return "Mobile Number Invalid";
        }
        String phoneNumber = "+91"+mobileNumber;
        Optional<Otp> otpOptional=Optional.ofNullable(otpRepository.findOtpByMobileNumber(mobileNumber));
        LocalDateTime currentTime= LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        if(otpOptional.isPresent()){
            Otp otp=otpOptional.get();
            if(otp.isVerified()){
                return "Otp already verified";
            }
            else if(otp.getExpiresAt().isBefore(currentTime)){
                otp.setOtpValue(Integer.toString(new Random().nextInt(900000) +100000));
                otp.setSentAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
                otp.setExpiresAt(otp.getSentAt().plusMinutes(10));
                otpRepository.save(otp);
                sendSMSMessage(amazonSNS, otpMessage+otp.getOtpValue(), phoneNumber, otpSenderId);
                return "New Otp sent successfully";
            }
            else{
                sendSMSMessage(amazonSNS, otpMessage+otp.getOtpValue(), phoneNumber, otpSenderId);
                return "Otp sent successfully";
            }
        }
        else{
            Otp otp = new Otp(mobileNumber);
            otpRepository.save(otp);
            sendSMSMessage(amazonSNS, otpMessage+otp.getOtpValue(), phoneNumber, otpSenderId);
            return "Otp sent successfully";
        }
    }

    @Override
    public String resendOtp(String mobileNumber) {
        if(!mobileNumber.matches("\\d{10}")){
            return "Mobile Number Invalid";
        }
        String phoneNumber = "+91"+mobileNumber;
        Optional<Otp> otpOptional=Optional.ofNullable(otpRepository.findOtpByMobileNumber(mobileNumber));
        LocalDateTime currentTime= LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        if(otpOptional.isPresent()){
            Otp otp=otpOptional.get();
            if(otp.isVerified()){
                return "Otp already verified";
            }
            else if(otp.getExpiresAt().isBefore(currentTime)){
                otp.setOtpValue(Integer.toString(new Random().nextInt(900000) +100000));
                otp.setSentAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
                otp.setExpiresAt(otp.getSentAt().plusMinutes(10));
                otpRepository.save(otp);
                sendSMSMessage(amazonSNS, otpMessage+otp.getOtpValue(), phoneNumber, otpSenderId);
                return "New Otp sent successfully";
            }
            else{
                sendSMSMessage(amazonSNS, otpMessage+otp.getOtpValue(), phoneNumber, otpSenderId);
                return "Otp sent successfully";
            }
        }
        else{
            return "Mobile Number not present";
        }
    }

    @Override
    public String verifyOtp(String mobileNumber, String otpValue) {
        if(!mobileNumber.matches("\\d{10}")){
            return "Mobile Number Invalid";
        }
        Optional<Otp> otpOptional=Optional.ofNullable(otpRepository.findOtpByMobileNumber(mobileNumber));
        LocalDateTime currentTime= LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        if(otpOptional.isPresent()){
            Otp otp=otpOptional.get();
            if(otp.isVerified()){
                return "Otp already verified";
            }
            else if(otp.getExpiresAt().isBefore(currentTime)){
                return "Otp expired";
            }
            else if(otp.getOtpValue().equals(otpValue)) {
                otp.setVerified(true);
                otpRepository.save(otp);
                return "Otp Verified";
            }
            else{
                return "Otp Wrong";
            }
        }
        else{
            return "Mobile Number not present";
        }
    }

}

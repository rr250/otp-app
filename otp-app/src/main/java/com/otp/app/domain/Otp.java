package com.otp.app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name="otp")
public class Otp {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column
    private String mobileNumber;

    @Column
    private LocalDateTime sentAt = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

    @Column
    private LocalDateTime expiresAt = sentAt.plusMinutes(10);

    @Column
    private boolean verified = false;

    @Column
    private String otpValue = Integer.toString(new Random().nextInt(900000) +100000);


    public Otp() {
    }

    public Otp(String mobileNumber) {
        this.mobileNumber=mobileNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }
}

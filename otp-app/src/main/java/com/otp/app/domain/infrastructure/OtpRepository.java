package com.otp.app.domain.infrastructure;

import com.otp.app.domain.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("otpRepository")
public interface OtpRepository extends JpaRepository<Otp, String> {
    Otp findOtpByMobileNumber(String mobileNumber);
}

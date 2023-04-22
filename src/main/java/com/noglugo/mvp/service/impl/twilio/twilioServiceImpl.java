package com.noglugo.mvp.service.impl.twilio;

import com.noglugo.mvp.service.TwilioService;
import com.noglugo.mvp.web.rest.errors.BadRequestAlertException;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class twilioServiceImpl implements TwilioService {

    @Value("${twilio.creds.svsid}")
    private String svsid;

    @Value("${twilio.creds.username}")
    private String username;

    @Value("${twilio.creds.password}")
    private String password;

    @Override
    public Verification sendOtpCode(String phoneNumber) {
        Twilio.init(username, password);
        try {
            return Verification.creator(svsid, "+216" + phoneNumber, "sms").create();
        } catch (Exception e) {
            throw new BadRequestAlertException(
                "Invalid Tunisian phone format, must be 8 digits: xxxxxxxx",
                "invalid-phone-format",
                "invalid-phone-format"
            );
        }
    }

    @Override
    public VerificationCheck verifyOtpCode(String phoneNumber, String otpCode) {
        Twilio.init(username, password);
        try {
            return VerificationCheck.creator(svsid).setTo("+216" + phoneNumber).setCode(otpCode).create();
        } catch (Exception e) {
            throw new BadRequestAlertException("OTP code has expired", "otp-expired", "otp-expired");
        }
    }
}

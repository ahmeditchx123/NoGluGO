package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.twilio.OtpVerificationResponse;
import com.noglugo.mvp.service.dto.twilio.SendOtpResponse;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public interface TwilioService {
    Verification sendOtpCode(String phoneNumber);

    VerificationCheck verifyOtpCode(String phoneNumber, String otpCode);
}

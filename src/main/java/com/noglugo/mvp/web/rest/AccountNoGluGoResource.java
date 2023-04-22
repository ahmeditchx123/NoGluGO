package com.noglugo.mvp.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noglugo.mvp.security.jwt.JWTFilter;
import com.noglugo.mvp.service.AccountNoglugoService;
import com.noglugo.mvp.service.DropSignService;
import com.noglugo.mvp.service.TwilioService;
import com.noglugo.mvp.service.dto.JwtDTO;
import com.noglugo.mvp.service.dto.RegisterDTO;
import com.noglugo.mvp.service.dto.hellosign.HelloSignEvent;
import com.noglugo.mvp.web.rest.vm.LoginVM;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts-no-glu-go")
public class AccountNoGluGoResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountNoGluGoResource.class);

    private final AccountNoglugoService accountNoglugoService;

    private final TwilioService twilioService;

    private final DropSignService dropSignService;

    public AccountNoGluGoResource(
        AccountNoglugoService accountNoglugoService,
        TwilioService twilioService,
        DropSignService dropSignService
    ) {
        this.accountNoglugoService = accountNoglugoService;
        this.twilioService = twilioService;
        this.dropSignService = dropSignService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterDTO registerDTO) {
        LOGGER.debug("Rest controller to register user with email {} ", registerDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountNoglugoService.registerUser(registerDTO));
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> login(@RequestBody @Valid LoginVM loginVM) {
        LOGGER.debug("Rest controller to login user with email {} ", loginVM.getUsername());
        JwtDTO jwt = accountNoglugoService.authenticateUser(loginVM);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("send-otp/{phoneNumber}")
    public ResponseEntity<?> sendOtpCode(@PathVariable("phoneNumber") String phoneNumber) {
        LOGGER.debug("Rest controller to send OTP Code to user with phone number {} ", phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(twilioService.sendOtpCode(phoneNumber));
    }

    @GetMapping("verify-otp/{phoneNumber}/{otpCode}")
    public ResponseEntity<?> sendOtpCode(@PathVariable("phoneNumber") String phoneNumber, @PathVariable("otpCode") String otpCode) {
        LOGGER.debug("Rest controller to validate otp with phone number {} ", phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(twilioService.verifyOtpCode(phoneNumber, otpCode));
    }

    @GetMapping("sign-contract/{email}/{name}")
    public ResponseEntity<String> getSigningUrl(@PathVariable("email") String email, @PathVariable("name") String name) {
        LOGGER.debug("Rest controller to get contract to sign for user with email {} and name {} ", email, name);
        return ResponseEntity.status(HttpStatus.OK).body(dropSignService.getEmbeddedSigningUrl(email, name));
    }

    @PostMapping(value = "sign-callback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> webhookHelloSign(HttpServletRequest request) throws Exception {
        LOGGER.debug("Rest controller webhook hello  sign");
        String payload = request.getParameter("json");
        ObjectMapper mapper = new ObjectMapper();
        HelloSignEvent helloSignEvent = mapper.readValue(payload, HelloSignEvent.class);
        LOGGER.debug("{}", helloSignEvent);
        return ResponseEntity.ok().body("Hello API Event Received");
    }
}

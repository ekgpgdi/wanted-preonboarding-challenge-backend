package com.example.wantedpreonboardingchallengebackend.member.controller;

import com.example.wantedpreonboardingchallengebackend.common.dto.response.ServerResponse;
import com.example.wantedpreonboardingchallengebackend.common.entity.ResponseCode;
import com.example.wantedpreonboardingchallengebackend.member.dto.request.EmailRequest;
import com.example.wantedpreonboardingchallengebackend.member.dto.request.EmailVerifyRequest;
import com.example.wantedpreonboardingchallengebackend.member.dto.request.LoginRequest;
import com.example.wantedpreonboardingchallengebackend.member.service.AuthService;
import com.example.wantedpreonboardingchallengebackend.member.service.EmailService;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "AUTH")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final EmailService emailService;

  @Operation(summary = "이메일 비밀번호로 로그인, 응답 : jwt")
  @PostMapping("/login")
  public ServerResponse<String> login(
      @Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
    }

    return ServerResponse.successResponse(
        authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword()));
  }

  @Operation(summary = "인증 코드 요청")
  @PostMapping("/send-verification-code")
  public ServerResponse<String> sendVerificationCode(
      @RequestBody @Valid EmailRequest emailRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
    }
    String code = authService.generateVerificationCode(emailRequest.getEmail());
    emailService.sendMail(emailRequest.getEmail(), code);

    return ServerResponse.successResponse(code);
  }

  @Operation(summary = "인증 코드 확인")
  @PostMapping("/verify-code")
  public ServerResponse<ResponseCode> checkVerificationCode(
      @RequestBody @Valid EmailVerifyRequest emailVerifyRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
    }

    final boolean verify =
        authService.verifyCertCode(emailVerifyRequest.getEmail(), emailVerifyRequest.getCertCode());

    if (!verify) {
      throw new IllegalArgumentException("VERIFIED_CODE_ERROR");
    }

    return ServerResponse.successResponse(ResponseCode.SUCCESS);
  }
}

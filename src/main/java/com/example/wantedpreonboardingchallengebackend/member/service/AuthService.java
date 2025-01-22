package com.example.wantedpreonboardingchallengebackend.member.service;

import com.example.wantedpreonboardingchallengebackend.common.exception.InvalidPasswordException;
import com.example.wantedpreonboardingchallengebackend.common.exception.MemberNotFoundException;
import com.example.wantedpreonboardingchallengebackend.common.service.JwtService;
import com.example.wantedpreonboardingchallengebackend.member.domain.Member;
import com.example.wantedpreonboardingchallengebackend.member.dto.request.SignRequest;
import com.example.wantedpreonboardingchallengebackend.member.repository.MemberQueryDslRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final MemberQueryDslRepository memberRepository;
  private final RedisTemplate<String, String> certCodeRedisTemplate;
  private final RedisTemplate<String, Boolean> checkVerifyEmailRedisTemplate;

  @Transactional(readOnly = true)
  public String authenticate(String email, String password) {
    Member member =
        memberRepository
            .findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("NOT_FOUND_MEMBER"));

    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new InvalidPasswordException("INVALID_PASSWORD");
    }

    return jwtService.generateJwt(email);
  }

  public void clearCertCodeRedisIfExists(String email) {
    // 코드가 존재하면 삭제
    certCodeRedisTemplate.delete(email);
  }

  public void clearVerifyEmailRedisIfExists(String email) {
    // 코드가 존재하면 삭제
    checkVerifyEmailRedisTemplate.delete(email);
  }

  public String generateVerificationCode(String email) {
    clearCertCodeRedisIfExists(email);

    String code = UUID.randomUUID().toString().substring(0, 6); // 6자리 인증번호 생성
    certCodeRedisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    return code;
  }

  public boolean verifyCertCode(String email, String certCode) {
    final String verifyCertCode = certCodeRedisTemplate.opsForValue().get(email);
    checkVerifyEmailRedisTemplate.opsForValue().set(email, true, 5, TimeUnit.MINUTES);
    return StringUtils.equals(certCode, verifyCertCode);
  }

  @Transactional
  public String sign(SignRequest signRequest) {
    if (!checkVerifyEmailRedisTemplate.hasKey(signRequest.getEmail())
        || !checkVerifyEmailRedisTemplate.opsForValue().get(signRequest.getEmail())) {
      throw new IllegalArgumentException("REQUIRED_EMAIL_VERIFIED");
    }

    Member member =
        Member.builder()
            .email(signRequest.getEmail())
            .nickname(signRequest.getNickname())
            .password(passwordEncoder.encode(signRequest.getPassword()))
            .build();

    memberRepository.save(member);
    clearVerifyEmailRedisIfExists(signRequest.getEmail());

    return jwtService.generateJwt(member.getEmail());
  }
}

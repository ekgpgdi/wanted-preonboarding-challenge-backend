package com.example.wantedpreonboardingchallengebackend.member.service;

import com.example.wantedpreonboardingchallengebackend.common.exception.InvalidPasswordException;
import com.example.wantedpreonboardingchallengebackend.common.exception.MemberNotFoundException;
import com.example.wantedpreonboardingchallengebackend.common.service.JwtService;
import com.example.wantedpreonboardingchallengebackend.member.domain.Member;
import com.example.wantedpreonboardingchallengebackend.member.repository.MemberRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
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
  private final MemberRepository memberRepository;

  private final RedisTemplate<String, String> redisTemplate;

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

  public void clearCodeIfExists(String email) {
    // 코드가 존재하면 삭제
    redisTemplate.delete(email);
  }

  public String generateVerificationCode(String email) {
    clearCodeIfExists(email);

    String code = UUID.randomUUID().toString().substring(0, 6); // 6자리 인증번호 생성
    redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES); // 5분간 유효
    return code;
  }

  public boolean verifyCertCode(String email, String certCode) {
    final String verifyCertCode = redisTemplate.opsForValue().get(email);
    return StringUtils.equals(certCode, verifyCertCode);
  }
}

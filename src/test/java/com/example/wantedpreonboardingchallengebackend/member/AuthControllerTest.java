package com.example.wantedpreonboardingchallengebackend.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.wantedpreonboardingchallengebackend.common.dto.response.ServerResponse;
import com.example.wantedpreonboardingchallengebackend.common.exception.InvalidPasswordException;
import com.example.wantedpreonboardingchallengebackend.common.exception.MemberNotFoundException;
import com.example.wantedpreonboardingchallengebackend.member.dto.request.LoginRequest;
import com.example.wantedpreonboardingchallengebackend.member.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Mock private AuthService authService;
  @Autowired MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // openMocks 대신 initMocks 사용
  }

  @Test
  @DisplayName("로그인 - 성공")
  public void testLogin_Success() throws Exception {
    // Given
    String email = "test@example.com";
    String password = "password123";
    String jwt = "mockJwtToken";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(email);
    loginRequest.setPassword(password);

    when(authService.authenticate(email, password)).thenReturn(jwt);

    // When & Then: MockMvc를 통해 요청을 보내고 검증
    mockMvc
        .perform(
            post("/api/v1/auth/login") // 요청을 보낼 URL 설정
                .contentType(MediaType.APPLICATION_JSON) // 요청 본문의 Content-Type 지정
                .content(objectMapper.writeValueAsString(loginRequest))) // 객체를 JSON 문자열로 변환
        .andExpect(status().isOk()) // 응답 상태 코드 검증
        .andExpect(jsonPath("$.content").value(jwt)); // 응답 내용 검증 (JSONPath 사용)

    // AuthService의 authenticate 메서드가 호출되었는지 확인
    verify(authService, times(1)).authenticate(email, password);
  }

  //  @Test
  //  @DisplayName("로그인 - 비밀번호 불일치")
  //  public void testLogin_InvalidCredentials() {
  //    // Given
  //    String email = "test@example.com";
  //    String password = "wrongPassword";
  //
  //    LoginRequest loginRequest = new LoginRequest();
  //    loginRequest.setEmail(email);
  //    loginRequest.setPassword(password);
  //
  //    doThrow(new InvalidPasswordException("INVALID_PASSWORD"))
  //        .when(authService)
  //        .authenticate(email, password);
  //
  //    // When & Then
  //    InvalidPasswordException exception =
  //        assertThrows(
  //            InvalidPasswordException.class, () -> authController.login(loginRequest, null));
  //    assertEquals("INVALID_PASSWORD", exception.getMessage());
  //    verify(authService).authenticate(email, password);
  //  }
  //
  //  @Test
  //  @DisplayName("로그인 - 이메일 불일치")
  //  public void testLogin_EmailNotFound() {
  //    // Given
  //    String email = "nonexistent@example.com";
  //    String password = "password123";
  //
  //    LoginRequest loginRequest = new LoginRequest();
  //    loginRequest.setEmail(email);
  //    loginRequest.setPassword(password);
  //
  //    doThrow(new MemberNotFoundException("NOT_FOUND_MEMBER"))
  //        .when(authService)
  //        .authenticate(email, password);
  //
  //    // When & Then
  //    MemberNotFoundException exception =
  //        assertThrows(MemberNotFoundException.class, () -> authController.login(loginRequest,
  // null));
  //    assertEquals("NOT_FOUND_MEMBER", exception.getMessage());
  //    verify(authService).authenticate(email, password);
  //  }
}

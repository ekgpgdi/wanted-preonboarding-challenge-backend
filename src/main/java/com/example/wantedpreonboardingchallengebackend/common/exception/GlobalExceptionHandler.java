package com.example.wantedpreonboardingchallengebackend.common.exception;

import com.example.wantedpreonboardingchallengebackend.common.dto.response.ServerResponse;
import com.example.wantedpreonboardingchallengebackend.common.entity.ResponseCode;

import jakarta.servlet.http.HttpServletRequest;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Object> resourceNotFoundException(HttpServletRequest request, Exception e) {
    log.error("Resource not found: {}", e.getMessage());
    return ResponseEntity.notFound().build();
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler({
    IllegalArgumentException.class,
    IllegalStateException.class,
  })
  public ResponseEntity<Object> clientExceptionHandler(HttpServletRequest request, Exception e) {
    log.error("Client error: {}", e.getMessage());
    return ResponseEntity.ok(ServerResponse.errorResponse(ResponseCode.valueOf(e.getMessage())));
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ServerResponse<?> authenticationExceptionHandler(HttpServletRequest request, Exception e) {
    log.error("Authentication error: {}", e.getMessage());
    return ServerResponse.errorResponse(ResponseCode.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ServerResponse<?> accessDeniedExceptionHandler(HttpServletRequest request, Exception e) {
    log.error("Access denied: {}", e.getMessage());
    return ServerResponse.errorResponse(ResponseCode.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ServerResponse<?> methodArgumentNotValidExceptionHandler(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    log.error(
        "Validation error: {}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    return ServerResponse.errorResponse(
        ResponseCode.valueOf(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ServerResponse<?> serverExceptionHandler(HttpServletRequest request, Exception e) {
    log.error("Internal server error: {}", e.getMessage());
    return ServerResponse.errorResponse(ResponseCode.SERVER_ERROR);
  }

  // 사용자 정의 예외 클래스 생성
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(MemberNotFoundException.class)
  public ServerResponse<?> memberNotFoundExceptionHandler(HttpServletRequest request, MemberNotFoundException e) {
    return ServerResponse.errorResponse(ResponseCode.NOT_FOUND_MEMBER);
  }

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(InvalidPasswordException.class)
  public ServerResponse<?> invalidPasswordExceptionHandler(HttpServletRequest request, InvalidPasswordException e) {
    return ServerResponse.errorResponse(ResponseCode.INVALID_PASSWORD);
  }
}

package com.example.wantedpreonboardingchallengebackend.common.exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException(String message) {
    super(message);
  }
}
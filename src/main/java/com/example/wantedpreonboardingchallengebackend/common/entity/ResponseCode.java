package com.example.wantedpreonboardingchallengebackend.common.entity;

public enum ResponseCode {
  // success
  SUCCESS(false),

  // auth
  UNAUTHORIZED(true),
  FORBIDDEN(true),

  // validation
  REQUIRED_EMAIL(true),
  REQUIRED_PASSWORD(true),
  REQUIRED_NICKNAME(true),
  EMAIL_FORMAT_ERROR(true),
  CHECK_CERT_CODE(false),
  VERIFIED_CODE_ERROR(false),
  REQUIRED_EMAIL_VERIFIED(true),

  // not found
  NOT_FOUND_MEMBER(false),
  INVALID_PASSWORD(false),

  // sever error
  SERVER_ERROR(true);

  public final boolean isFatality;

  ResponseCode(boolean isFatality) {
    this.isFatality = isFatality;
  }
}

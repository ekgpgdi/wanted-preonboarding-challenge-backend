package com.example.wantedpreonboardingchallengebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WantedPreonboardingChallengeBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WantedPreonboardingChallengeBackendApplication.class, args);
  }
}

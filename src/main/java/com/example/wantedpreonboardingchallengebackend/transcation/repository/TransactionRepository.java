package com.example.wantedpreonboardingchallengebackend.transcation.repository;

import com.example.wantedpreonboardingchallengebackend.transcation.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}

package com.example.wantedpreonboardingchallengebackend.member.repository;

import com.example.wantedpreonboardingchallengebackend.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}

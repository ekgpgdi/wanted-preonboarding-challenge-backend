package com.example.wantedpreonboardingchallengebackend.member.repository;

import com.example.wantedpreonboardingchallengebackend.member.domain.Member;
import com.example.wantedpreonboardingchallengebackend.member.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {
  private final JPAQueryFactory queryFactory;
  private final EntityManager entityManager;

  public Optional<Member> findByEmail(String email) {
    QMember member = QMember.member;

    Member result = queryFactory.selectFrom(member).where(member.email.eq(email)).fetchOne();

    return Optional.ofNullable(result);
  }

  public void save(Member member) {
    entityManager.persist(member);
  }
}

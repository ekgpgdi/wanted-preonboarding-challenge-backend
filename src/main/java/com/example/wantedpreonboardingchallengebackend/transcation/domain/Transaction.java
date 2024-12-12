package com.example.wantedpreonboardingchallengebackend.transcation.domain;

import com.example.wantedpreonboardingchallengebackend.common.entity.BaseTimeEntity;
import com.example.wantedpreonboardingchallengebackend.member.domain.Member;
import com.example.wantedpreonboardingchallengebackend.product.domain.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_transaction")
@Entity
public class Transaction extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_status", nullable = false)
  private TransactionStatus transactionStatus;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "seller_id", insertable = false, updatable = false)
  private Member seller;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "buyer_id", insertable = false, updatable = false)
  private Member buyer;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  private Product product;
}

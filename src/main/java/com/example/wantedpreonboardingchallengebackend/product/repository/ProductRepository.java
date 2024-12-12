package com.example.wantedpreonboardingchallengebackend.product.repository;

import com.example.wantedpreonboardingchallengebackend.product.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}

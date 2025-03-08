package com.example.ballup_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ballup_backend.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>{
    
}

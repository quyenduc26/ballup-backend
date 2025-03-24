package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.PaymentMethodEntity;
import com.example.ballup_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
    List<PaymentMethodEntity> findByOwner(UserEntity owner);

    @Modifying
    @Query("UPDATE PaymentMethodEntity p SET p.isActive = false WHERE p.owner.id = :ownerId")
    void deactivateAllByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT p FROM PaymentMethodEntity p WHERE p.owner.id = :ownerId AND p.isActive = true")
    Optional<PaymentMethodEntity> findActivePaymentMethodByOwnerId(@Param("ownerId") Long ownerId);


}

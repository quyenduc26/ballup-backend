package com.example.ballup_backend.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.ballup_backend.entity.PlayingCenterEntity;

public class PlayingCenterSpecification {
    public static Specification<PlayingCenterEntity> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<PlayingCenterEntity> filterByAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null || address.isEmpty()) return null;
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("address")), address.toLowerCase());
        };
    }

   
}

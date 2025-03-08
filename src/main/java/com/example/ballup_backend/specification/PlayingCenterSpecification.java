package com.example.ballup_backend.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingCenterEntity.PlayingCenterType;

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

    public static Specification<PlayingCenterEntity> excludeCenters(List<Long> excludedCenterIds) {
        return (root, query, criteriaBuilder) -> 
            root.get("id").in(excludedCenterIds).not();
    }

    public static Specification<PlayingCenterEntity> filterBySport(PlayingCenterType sport) {
        return (root, query, criteriaBuilder) -> {
            if (sport == null) return null;
            return criteriaBuilder.equal(root.get("type"), sport);
        };
    }

   
}

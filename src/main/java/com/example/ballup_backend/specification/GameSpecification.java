package com.example.ballup_backend.specification;

import org.springframework.data.jpa.domain.Specification;
import com.example.ballup_backend.entity.GameEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class GameSpecification {
    public static Specification<GameEntity> filterGames(String name, String address, String sport) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (address != null && !address.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + address.toLowerCase() + "%"));
            }

            if (sport != null && !sport.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), GameEntity.GameType.valueOf(sport.toUpperCase())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

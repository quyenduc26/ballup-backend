package com.example.ballup_backend.specification;
import org.springframework.data.jpa.domain.Specification;

import com.example.ballup_backend.entity.TeamEntity;

public class TeamSpecification {

    public static Specification<TeamEntity> filterByName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<TeamEntity> filterByLocation(String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<TeamEntity> filterBySport(TeamEntity.SportType sport) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("sport"), sport);
    }
}


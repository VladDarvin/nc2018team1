package com.nc.airport.backend.repository;

import com.nc.airport.backend.model.entities.Authority;
import com.nc.airport.backend.model.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserFilter implements Specification<User> {

    private List<Map<String, Object>> criterias;

    public UserFilter(List<Map<String, Object>> criterias) {
        this.criterias = criterias;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicates = buildPredicates(root, criteriaQuery, builder);

        return predicates.size() > 1
                ? builder.and(predicates.toArray(new Predicate[predicates.size()]))
                : predicates.get(0);
    }

    private List<Predicate> buildPredicates(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        criterias.forEach(criteria -> predicates.add(buildPredicate(criteria, root, criteriaQuery, builder)));
        return predicates;
    }

    public Predicate buildPredicate(Map<String, Object> criteria, Root root, CriteriaQuery criteriaQuery, CriteriaBuilder builder) {
        for (String key:
            criteria.keySet()) {
            if (key.equals("authority")) {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.get(key);
                String role = map.get("name");
                final Subquery<Authority> subQuery = criteriaQuery.subquery(Authority.class);
                final Root<Authority> authorityRoot = subQuery.from(Authority.class);
                Predicate userIdPredicate = builder.equal(authorityRoot.get("id"), root.<String> get("authority"));
                Predicate rolePredicate = builder.equal(authorityRoot.get("name"), role);
                subQuery.select(authorityRoot).where(userIdPredicate, rolePredicate);

                return builder.exists(subQuery);
            } else {
                return builder.like(root.get(key), "%" + criteria.get(key) + "%");
            }


        }
        return null;
    }
}

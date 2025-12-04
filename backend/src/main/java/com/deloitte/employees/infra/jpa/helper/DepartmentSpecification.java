package com.deloitte.employees.infra.jpa.helper;

import com.deloitte.employees.common.valueobject.Search;
import com.deloitte.employees.infra.jpa.entities.departments.DepartmentEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class DepartmentSpecification {

    public static Specification<DepartmentEntity> fromSearch(Search search) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // no search criteria → no filtering
            if (search == null || !search.hasCriteria()) {
                return predicate;
            }

            search.getCriteria().forEach((field, value) -> {
                if (field.equals("title")) {
                    predicate.getExpressions().add(
                            cb.like(cb.lower(root.get("title")), "%" + value.toLowerCase() + "%")
                    );
                }
            });

            return predicate;
        };
    }
}

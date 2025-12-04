package com.deloitte.employees.infra.jpa.mapper;

import com.deloitte.employees.common.mapper.DataMapper;
import com.deloitte.employees.domain.departments.entities.Designation;
import com.deloitte.employees.infra.jpa.entities.departments.DepartmentEntity;
import com.deloitte.employees.infra.jpa.entities.departments.DesignationEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DesignationMapper implements DataMapper<Designation, DesignationEntity> {

    private final EntityManager entityManager;

    @Override
    public DesignationEntity toEntity(Designation designation) {
        String departmentId = designation.getDepartmentId();
        var ref = entityManager.getReference(DepartmentEntity.class, departmentId);
        return DesignationEntity.builder()
                .id(designation.getId())
                .title(designation.getTitle())
                .description(designation.getDescription())
                .department(ref)
                .build();
    }

    @Override
    public Designation toDomain(DesignationEntity designationEntity) {
        String departmentId = null;
        if (designationEntity.getDepartment() != null) {
            departmentId = designationEntity.getDepartment().getId();
        }
        return Designation.builder()
                .id(designationEntity.getId())
                .title(designationEntity.getTitle())
                .description(designationEntity.getDescription())
                .departmentId(departmentId)
                .build();
    }
}

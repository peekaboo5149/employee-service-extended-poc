package com.deloitte.employees.infra.jpa.mapper;

import com.deloitte.employees.common.mapper.DataMapper;
import com.deloitte.employees.domain.departments.entities.Department;
import com.deloitte.employees.infra.jpa.entities.departments.DepartmentEntity;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper implements DataMapper<Department, DepartmentEntity> {

    @Override
    public DepartmentEntity toEntity(Department department) {
        return DepartmentEntity.builder()
                .id(department.getId())
                .title(department.getTitle())
                .description(department.getDescription())
                .build();
    }

    @Override
    public Department toDomain(DepartmentEntity departmentEntity) {
        return Department.builder()
                .id(departmentEntity.getId())
                .title(departmentEntity.getTitle())
                .description(departmentEntity.getDescription())
                .build();
    }
}

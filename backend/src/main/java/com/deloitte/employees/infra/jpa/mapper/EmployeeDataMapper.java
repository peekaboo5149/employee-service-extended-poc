package com.deloitte.employees.infra.jpa.mapper;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.mapper.DataMapper;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.Password;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import com.deloitte.employees.infra.jpa.entities.auth.EmployeeEntity;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDataMapper implements DataMapper<Employee, EmployeeEntity> {

    @Override
    public Employee toDomain(EmployeeEntity entity) {
        Either<ValidationFailure.ValueObjectFailure, UniqueId> uniqueIds = UniqueId.create(entity.getId());
        Either<ValidationFailure.ValueObjectFailure, Email> emails = Email.create(entity.getEmail());
        Either<ValidationFailure.ValueObjectFailure, Password> passwords = Password.create(entity.getPassword());
        return Employee.builder()
                .id(uniqueIds.get())
                .email(emails.get())
                .password(passwords.get())
                .isVerified(entity.isVerified())
                .status(entity.getStatus())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .lastUpdatedAt(entity.getUpdatedAt())
                .build();

    }

    @Override
    public EmployeeEntity toEntity(Employee obj) {
        return EmployeeEntity.builder()
                .id(obj.getId().getValue())
                .email(obj.getEmail().getValue())
                .password(obj.getPassword().getValue())
                .isVerified(obj.isVerified())
                .role(obj.getRole())
                .status(obj.getStatus())
                .build();
    }
}

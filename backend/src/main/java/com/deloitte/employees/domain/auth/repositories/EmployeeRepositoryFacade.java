package com.deloitte.employees.domain.auth.repositories;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface EmployeeRepositoryFacade {

    Either<OperationFailure, Option<Employee>> findByEmail(Email email);

    Either<OperationFailure, Option<Employee>> findById(UniqueId id);

    Either<OperationFailure, Employee> save(Employee employee);

    Either<OperationFailure, Employee> update(Employee employee);

}

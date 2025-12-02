package com.deloitte.employees.infra.repositories.auth;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ResourceOperationFailure;
import com.deloitte.employees.common.exceptions.SystemFailure;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.repositories.EmployeeRepositoryFacade;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import com.deloitte.employees.infra.jpa.mapper.EmployeeDataMapper;
import com.deloitte.employees.infra.jpa.repositories.auth.EmployeeEntityRepository;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
class EmployeeRepository implements EmployeeRepositoryFacade {

    private final EmployeeEntityRepository employeeEntityRepository;
    private final EmployeeDataMapper dataMapper;

    @Override
    public Either<OperationFailure, Option<Employee>> findByEmail(Email email) {
        return Try.of(() -> employeeEntityRepository.findByEmail(email.getValue()))
                .map(optionalEntity ->
                        optionalEntity
                                .map(dataMapper::toDomain)
                                .map(Option::some)
                                .orElse(Option.none())
                )
                .toEither()
                .mapLeft(SystemFailure.UnexpectedFailure::fromException);

    }

    @Override
    public Either<OperationFailure, Option<Employee>> findById(UniqueId id) {
        return Try.of(() -> employeeEntityRepository.findById(id.getValue()))
                .map(optionalEntity ->
                        optionalEntity
                                .map(dataMapper::toDomain)
                                .map(Option::some)
                                .orElse(Option.none())
                )
                .toEither()
                .mapLeft(SystemFailure.UnexpectedFailure::fromException);
    }

    @Override
    public Either<OperationFailure, Employee> save(Employee employee) {
        return findByEmail(employee.getEmail())
                .flatMap(existingOpt ->
                        existingOpt.isDefined()
                                ? Either.left(ResourceOperationFailure.alreadyExists(
                                "employee", employee.getEmail().getValue()))
                                : Try.of(() -> {
                                    var entity = dataMapper.toEntity(employee);
                                    var saved = employeeEntityRepository.save(entity);
                                    return dataMapper.toDomain(saved);
                                })
                                .toEither()
                                .mapLeft(SystemFailure.UnexpectedFailure::fromException)
                );
    }

    @Override
    public Either<OperationFailure, Employee> update(Employee employee) {
        return findById(employee.getId())
                .flatMap(existingOpt ->
                        existingOpt.isEmpty()
                                ? Either.left(ResourceOperationFailure.notFound(
                                "employee", employee.getId().getValue()))
                                : Try.of(() -> {
                                    var entity = dataMapper.toEntity(employee);
                                    var updated = employeeEntityRepository.save(entity);
                                    return dataMapper.toDomain(updated);
                                })
                                .toEither()
                                .mapLeft(SystemFailure.UnexpectedFailure::fromException)
                );
    }
}

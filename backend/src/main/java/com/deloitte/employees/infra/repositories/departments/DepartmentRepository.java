package com.deloitte.employees.infra.repositories.departments;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ResourceOperationFailure;
import com.deloitte.employees.common.exceptions.SystemFailure;
import com.deloitte.employees.common.valueobject.Search;
import com.deloitte.employees.domain.departments.entities.Department;
import com.deloitte.employees.domain.departments.repositories.DepartmentRepositoryFacade;
import com.deloitte.employees.infra.jpa.helper.DepartmentSpecification;
import com.deloitte.employees.infra.jpa.mapper.DepartmentMapper;
import com.deloitte.employees.infra.jpa.repositories.departments.DepartmentEntityRepository;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
class DepartmentRepository implements DepartmentRepositoryFacade {

    private final DepartmentEntityRepository departmentEntityRepository;
    private final DepartmentMapper dataMapper;

    @Override
    public Either<OperationFailure, List<Department>> findAll(Search search) {
        return Try.of(() -> {
                    boolean noSearch = (search == null) || !search.hasCriteria();
                    if (noSearch) {
                        return departmentEntityRepository.findAll();
                    }
                    return departmentEntityRepository.findAll(
                            DepartmentSpecification.fromSearch(search)
                    );
                })
                .map(list -> list.stream().map(dataMapper::toDomain).toList())
                .toEither()
                .mapLeft(SystemFailure.UnexpectedFailure::fromException);
    }


    @Override
    public Either<OperationFailure, Option<Department>> findById(String id) {
        return Try.of(() -> departmentEntityRepository.findById(id))
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
    public Either<OperationFailure, Option<Department>> findByTitle(String title) {
        return Try.of(() -> departmentEntityRepository.findByTitle(title))
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
    public Either<OperationFailure, Department> save(Department department) {
        return findByTitle(department.getTitle())
                .flatMap(existingOpt ->
                        existingOpt.isDefined()
                                ? Either.left(ResourceOperationFailure.alreadyExists(
                                "department", department.getTitle()))
                                : Try.of(() -> {
                                    var entity = dataMapper.toEntity(department);
                                    var saved = departmentEntityRepository.save(entity);
                                    return dataMapper.toDomain(saved);
                                })
                                .toEither()
                                .mapLeft(SystemFailure.UnexpectedFailure::fromException)
                );
    }
}

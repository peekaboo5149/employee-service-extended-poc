package com.deloitte.employees.domain.departments.repositories;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.valueobject.Search;
import com.deloitte.employees.domain.departments.entities.Department;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;

public interface DepartmentRepositoryFacade {

    Either<OperationFailure, List<Department>> findAll(Search search);

    Either<OperationFailure, Option<Department>> findById(String id);

    Either<OperationFailure, Option<Department>> findByTitle(String title);

    Either<OperationFailure, Department> save(Department department);


    default Either<OperationFailure, List<Department>> findAll() {
        return findAll(Search.empty());
    }
}

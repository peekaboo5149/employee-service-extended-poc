package com.deloitte.employees.domain.departments.repositories;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.domain.departments.entities.Designation;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;

public interface DesignationRepository {

    Either<OperationFailure, List<Designation>> findAllByDepartmentId(String departmentId);

    Either<OperationFailure, Designation> save(Designation designation);

    Either<OperationFailure, Option<Designation>> findById(String id);

    Either<OperationFailure, Option<Designation>> findByTitle(String title);

}

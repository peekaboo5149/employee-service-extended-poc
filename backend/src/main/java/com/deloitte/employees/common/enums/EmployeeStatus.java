package com.deloitte.employees.common.enums;


import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.validation.EnumValidation;
import io.vavr.control.Either;

/**
 * Employee Status
 */
public enum EmployeeStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    public Either<ValidationFailure.EnumValidationFailure, EmployeeStatus> fromString(String status) {
        return EnumValidation.fromString(EmployeeStatus.class, "status", status);
    }
}

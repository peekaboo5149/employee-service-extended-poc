package com.deloitte.employees.common.enums;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.validation.EnumValidation;
import io.vavr.control.Either;
import lombok.Getter;

@Getter
public enum Role {
    ADMIN,
    USER;

    public static Either<ValidationFailure.EnumValidationFailure, Role> fromString(String role) {
        return EnumValidation.fromString(Role.class, "role", role);
    }

}

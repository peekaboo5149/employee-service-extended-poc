package com.deloitte.employees.common.valueobject;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.validation.EnumValidation;
import io.vavr.control.Either;
import lombok.Getter;

@Getter
public enum SortDirection {
    ASC,
    DESC;

    public static Either<ValidationFailure.EnumValidationFailure, SortDirection> fromString(String direction) {
        return EnumValidation.fromString(SortDirection.class, "direction", direction);
    }

}

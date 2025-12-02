package com.deloitte.employees.common.enums;


import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.validation.EnumValidation;
import io.vavr.control.Either;
import lombok.Getter;

@Getter
public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public static Either<ValidationFailure.EnumValidationFailure, Gender> fromString(String gender) {
        return EnumValidation.fromString(Gender.class, "gender", gender);
    }

}

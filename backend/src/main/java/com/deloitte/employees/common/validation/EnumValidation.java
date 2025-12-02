package com.deloitte.employees.common.validation;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.common.utils.StringUtils;
import io.vavr.control.Either;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EnumValidation {

    public static <E extends Enum<E>> Either<ValidationFailure.EnumValidationFailure, E>
    fromString(Class<E> enumClass, String field, String value) {

        if (StringUtils.isEmpty(value)) {
            return Either.left(
                    new ValidationFailure.EnumValidationFailure(
                            List.of(ErrorDetail.builder()
                                    .field(field)
                                    .message("Enum value cannot be empty")
                                    .code("ERR_EMPTY_STRING")
                                    .build())
                    )
            );
        }

        try {
            return Either.right(Enum.valueOf(enumClass, value.trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {

            String allowedValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            return Either.left(
                    new ValidationFailure.EnumValidationFailure(
                            List.of(ErrorDetail.builder()
                                    .field(field)
                                    .message("Invalid value '" + value + "'. Allowed values: " + allowedValues)
                                    .code("ERR_INVALID_ENUM")
                                    .build())
                    )
            );
        }
    }
}

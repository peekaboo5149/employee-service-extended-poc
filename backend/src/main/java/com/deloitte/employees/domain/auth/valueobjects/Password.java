package com.deloitte.employees.domain.auth.valueobjects;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.common.utils.StringUtils;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode
public class Password {

    String value;

    private Password(String value) {
        this.value = value;
    }

    public static Either<ValidationFailure.ValueObjectFailure, Password> create(String raw) {
        if (StringUtils.isEmpty(raw)) {
            return Either.left(new ValidationFailure.ValueObjectFailure(
                    List.of(ErrorDetail.builder()
                            .code("ERR_EMPTY_PASSWORD")
                            .message("Password cannot be empty")
                            .field("password")
                            .build())
            ));
        }

        if (raw.length() < 3) {
            return Either.left(new ValidationFailure.ValueObjectFailure(
                    List.of(ErrorDetail.builder()
                            .code("ERR_PASS_MIN_CONSTRAINT_VIOLATION")
                            .message("Password cannot be less than 3 characters long")
                            .field("password")
                            .build())
            ));
        }

        return Either.right(new Password(raw));
    }

    @Override
    public String toString() {
        return "********";
    }
}

package com.deloitte.employees.domain.auth.valueobjects;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.common.utils.StringUtils;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.regex.Pattern;

@Value
@EqualsAndHashCode
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    String value;

    public static Either<ValidationFailure.ValueObjectFailure, Email> create(String email) {
        if (StringUtils.isEmpty(email)) {
            return Either.left(new ValidationFailure.ValueObjectFailure(
                    List.of(ErrorDetail.builder()
                            .code("ERR_EMPTY_EMAIL")
                            .message("Email cannot be empty")
                            .field("email")
                            .build())
            ));
        }

        if (!EMAIL_REGEX.matcher(email).matches()) {
            return Either.left(new ValidationFailure.ValueObjectFailure(
                    List.of(ErrorDetail.builder()
                            .code("ERR_INVALID_EMAIL")
                            .message("Invalid email format")
                            .field("email")
                            .build())
            ));
        }

        return Either.right(new Email(email.trim().toLowerCase()));
    }

    private Email(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

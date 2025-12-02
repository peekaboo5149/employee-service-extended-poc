package com.deloitte.employees.domain.auth.valueobjects;

import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.common.utils.StringUtils;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@EqualsAndHashCode
public class UniqueId {

    String value;

    private UniqueId(String value) {
        this.value = value;
    }

    public static Either<ValidationFailure.ValueObjectFailure, UniqueId> create(String id) {
        if (StringUtils.isEmpty(id)) {
            return Either.left(new ValidationFailure.ValueObjectFailure(
                    List.of(ErrorDetail.builder()
                            .code("ERR_EMPTY_UID")
                            .message("Identifier cannot be empty")
                            .field("id")
                            .build())
            ));
        }

        return Try.of(() -> UUID.fromString(id))
                .map(uuid -> new UniqueId(uuid.toString()))
                .toEither()
                .mapLeft(ex -> new ValidationFailure.ValueObjectFailure(
                        List.of(ErrorDetail.builder()
                                .code("ERR_INVALID_UID")
                                .message("Invalid Identifier format. Must be a valid UUID")
                                .field("id")
                                .build())
                ));
    }

    public static UniqueId generate() {
        return new UniqueId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}

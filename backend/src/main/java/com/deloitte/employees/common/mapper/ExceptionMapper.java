package com.deloitte.employees.common.mapper;

import com.deloitte.employees.common.exceptions.OperationFailure;

public interface ExceptionMapper<E extends RuntimeException> {
    E map(OperationFailure failure);

    default <T> T mapAndThrow(OperationFailure failure) {
        throw map(failure);
    }
}

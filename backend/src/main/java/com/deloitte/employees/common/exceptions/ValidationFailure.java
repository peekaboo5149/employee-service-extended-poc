package com.deloitte.employees.common.exceptions;

import com.deloitte.employees.common.models.ErrorDetail;

import java.util.List;

public sealed interface ValidationFailure extends OperationFailure
        permits ValidationFailure.EnumValidationFailure, ValidationFailure.InputValidationFailure, ValidationFailure.ValueObjectFailure {

    record EnumValidationFailure(List<ErrorDetail> errorDetails) implements ValidationFailure {
    }

    record ValueObjectFailure(List<ErrorDetail> errorDetails) implements ValidationFailure {
    }

    record InputValidationFailure(List<ErrorDetail> errorDetails) implements ValidationFailure {
    }
}

package com.deloitte.employees.common.exceptions;

import com.deloitte.employees.common.models.ErrorDetail;

import java.util.List;

public sealed interface OperationFailure permits ResourceOperationFailure, SystemFailure, ValidationFailure {

    List<ErrorDetail> errorDetails();

}

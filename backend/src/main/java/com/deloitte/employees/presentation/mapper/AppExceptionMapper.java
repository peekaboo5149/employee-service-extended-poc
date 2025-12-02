package com.deloitte.employees.presentation.mapper;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ResourceOperationFailure;
import com.deloitte.employees.common.exceptions.SystemFailure;
import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.mapper.ExceptionMapper;
import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.presentation.exception.AppException;
import com.deloitte.employees.presentation.exception.ErrorCode;
import com.deloitte.employees.presentation.exception.ErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public AppException map(OperationFailure failure) {

        List<ErrorDetail> errorDetails = failure.errorDetails();

        ErrorResponse response = switch (failure) {

            // -----------------------------
            // RESOURCE FAILURES
            // -----------------------------
            case ResourceOperationFailure.ResourceNotFoundFailure f ->
                    ErrorResponse.builder()
                            .message("Resource not found")
                            .code(ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus().value())
                            .errorCode(ErrorCode.RESOURCE_NOT_FOUND)
                            .errorDetails(errorDetails)
                            .build();

            case ResourceOperationFailure.ResourceAlreadyExistsFailure f ->
                    ErrorResponse.builder()
                            .message("Resource already exists")
                            .code(ErrorCode.RESOURCE_CONFLICT.getHttpStatus().value())
                            .errorCode(ErrorCode.RESOURCE_CONFLICT)
                            .errorDetails(errorDetails)
                            .build();

            // -----------------------------
            // SYSTEM FAILURES
            // -----------------------------
            case SystemFailure.UnexpectedFailure f ->
                    ErrorResponse.builder()
                            .message("Unexpected system failure")
                            .code(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
                            .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                            .errorDetails(errorDetails)
                            .build();

            case SystemFailure.SystemNotReachableFailure f ->
                    ErrorResponse.builder()
                            .message("Service unavailable")
                            .code(ErrorCode.SERVICE_UNAVAILABLE.getHttpStatus().value())
                            .errorCode(ErrorCode.SERVICE_UNAVAILABLE)
                            .errorDetails(errorDetails)
                            .build();

            // -----------------------------
            // VALIDATION FAILURES
            // -----------------------------
            case ValidationFailure.EnumValidationFailure f ->
                    ErrorResponse.builder()
                            .message("Invalid enum value")
                            .code(ErrorCode.BAD_REQUEST.getHttpStatus().value())
                            .errorCode(ErrorCode.BAD_REQUEST)
                            .errorDetails(errorDetails)
                            .build();

            case ValidationFailure.ValueObjectFailure f ->
                    ErrorResponse.builder()
                            .message("Invalid input for value object")
                            .code(ErrorCode.BAD_REQUEST.getHttpStatus().value())
                            .errorCode(ErrorCode.BAD_REQUEST)
                            .errorDetails(errorDetails)
                            .build();

            case ValidationFailure.InputValidationFailure f ->
                    ErrorResponse.builder()
                            .message("Input validation failed")
                            .code(ErrorCode.BAD_REQUEST.getHttpStatus().value())
                            .errorCode(ErrorCode.BAD_REQUEST)
                            .errorDetails(errorDetails)
                            .build();
        };

        return AppException.of(response);
    }
}

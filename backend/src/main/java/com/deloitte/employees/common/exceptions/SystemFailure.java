package com.deloitte.employees.common.exceptions;

import com.deloitte.employees.common.models.ErrorDetail;

import java.util.List;

public sealed interface SystemFailure extends OperationFailure
        permits SystemFailure.UnexpectedFailure,
        SystemFailure.SystemNotReachableFailure {

    record UnexpectedFailure(List<ErrorDetail> errorDetails) implements SystemFailure {

        public static UnexpectedFailure fromException(Throwable e) {
            return new UnexpectedFailure(
                    List.of(
                            ErrorDetail.builder()
                                    .code("ERR_DB")
                                    .message(e.getMessage())
                                    .field("database")
                                    .build()
                    )
            );
        }

    }

    record SystemNotReachableFailure(List<ErrorDetail> errorDetails) implements SystemFailure {
    }


}

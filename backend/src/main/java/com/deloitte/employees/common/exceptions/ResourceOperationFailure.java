package com.deloitte.employees.common.exceptions;

import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.common.utils.StringUtils;

import java.util.List;

public sealed interface ResourceOperationFailure extends OperationFailure
        permits ResourceOperationFailure.ResourceNotFoundFailure,
        ResourceOperationFailure.ResourceAlreadyExistsFailure {

    static ResourceNotFoundFailure notFound(String resourceName, String resourceId) {
        return new ResourceNotFoundFailure(resourceName, resourceId);
    }

    static ResourceAlreadyExistsFailure alreadyExists(String resourceName, String resourceId) {
        return new ResourceAlreadyExistsFailure(resourceName, resourceId);
    }

    record ResourceNotFoundFailure(String resourceName, String resourceId) implements ResourceOperationFailure {

        @Override
        public List<ErrorDetail> errorDetails() {
            return List.of(
                    ErrorDetail.builder()
                            .code("ERR_RESOURCE_NOT_FOUND")
                            .message(getMessage(resourceName, resourceId, "not found"))
                            .field(resourceName)
                            .build()
            );
        }
    }

    record ResourceAlreadyExistsFailure(String resourceName, String resourceId) implements ResourceOperationFailure {

        @Override
        public List<ErrorDetail> errorDetails() {
            return List.of(
                    ErrorDetail.builder()
                            .code("ERR_RESOURCE_ALREADY_EXISTS")
                            .message(getMessage(resourceName, resourceId, "already exists"))
                            .field(resourceName)
                            .build()
            );
        }
    }

    private static String getMessage(String resourceName,
                                     String resourceId,
                                     String tag) {
        if (StringUtils.isEmpty(resourceId)) return resourceName + " " + tag;
        return resourceName + " with id " + resourceId + " " + tag;
    }
}

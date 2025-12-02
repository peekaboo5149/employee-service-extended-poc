package com.deloitte.employees.common.valueobject;


import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ValidationFailure;
import com.deloitte.employees.common.models.ErrorDetail;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.List;

public record Query<SORT_FIELD extends Enum<?>>(PageResult pageRequest,
                                                List<SortSpec<SORT_FIELD>> sorts,
                                                Search search
) {

    public static <SORT_FIELD extends Enum<?>> Either<OperationFailure, Query<SORT_FIELD>> of(
            PageResult pageRequest,
            List<SortSpec<SORT_FIELD>> sorts
    ) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (pageRequest == null) {
            errors.add(ErrorDetail.builder()
                    .field("pageRequest")
                    .message("Page request cannot be null")
                    .code("ERR_NULL_PAGE_REQUEST")
                    .build());
        }

        if (sorts == null) {
            errors.add(ErrorDetail.builder()
                    .field("sorts")
                    .message("Sort list cannot be null")
                    .code("ERR_NULL_SORT_LIST")
                    .build());
        }

        if (!errors.isEmpty()) {
            return Either.left(new ValidationFailure.InputValidationFailure(errors));
        }

        return Either.right(new Query<>(pageRequest, sorts, Search.empty()));
    }

    public static <SORT_FIELD extends Enum<?>> Either<OperationFailure, Query<SORT_FIELD>> of(
            PageResult pageRequest,
            List<SortSpec<SORT_FIELD>> sorts,
            Search search
    ) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (pageRequest == null) {
            errors.add(ErrorDetail.builder()
                    .field("pageRequest")
                    .message("Page request cannot be null")
                    .code("ERR_NULL_PAGE_REQUEST")
                    .build());
        }

        if (sorts == null) {
            errors.add(ErrorDetail.builder()
                    .field("sorts")
                    .message("Sort list cannot be null")
                    .code("ERR_NULL_SORT_LIST")
                    .build());
        }

        if (search == null) search = Search.empty();

        if (!errors.isEmpty()) {
            return Either.left(new ValidationFailure.InputValidationFailure(errors));
        }

        return Either.right(new Query<>(pageRequest, sorts, search));
    }


    public static <SORT_FIELD extends Enum<?>> Either<OperationFailure, Query<SORT_FIELD>> defaultQuery() {
        return PageResult.defaultPage()
                .map(page -> new Query<>(page, List.of(), Search.empty()));
    }
}

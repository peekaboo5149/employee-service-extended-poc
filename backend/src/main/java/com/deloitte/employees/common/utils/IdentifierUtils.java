package com.deloitte.employees.common.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdentifierUtils {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}


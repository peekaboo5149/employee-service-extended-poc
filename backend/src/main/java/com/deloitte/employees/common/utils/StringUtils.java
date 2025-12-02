package com.deloitte.employees.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }

}

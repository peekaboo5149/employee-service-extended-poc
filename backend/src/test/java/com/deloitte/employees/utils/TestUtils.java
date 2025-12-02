package com.deloitte.employees.utils;

import com.deloitte.employees.common.enums.EmployeeStatus;
import com.deloitte.employees.common.enums.Role;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.Password;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestUtils {
//    @SuppressWarnings("unchecked")
//    public static <T extends RuntimeException> ExceptionMapper<T> mockExceptionMapper() {
//        return mock(ExceptionMapper.class);
//    }

    /**
     * Generate `count` lexicographically ordered full names.
     * Examples: "Test A", "Test B", ... "Test Z", "Test AA", "Test AB", ...
     */
    public static String[] getLexicographicalFullNames(int count) {
        if (count <= 0) {
            return new String[0];
        }
        List<String> names = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String lastName = toBase26(i);
            names.add("Test " + lastName);
        }
        return names.toArray(new String[0]);
    }

    /**
     * Convert a zero-based index to letters in "A..Z, AA..ZZ, AAA..." style.
     * 0 -> "A", 1 -> "B", ..., 25 -> "Z", 26 -> "AA", 27 -> "AB", ...
     */
    private static String toBase26(int index) {
        if (index < 0) throw new IllegalArgumentException("index must be >= 0");
        StringBuilder sb = new StringBuilder();
        int n = index;
        while (n >= 0) {
            int rem = n % 26;
            sb.append((char) ('A' + rem));
            n = (n / 26) - 1;
        }
        return sb.reverse().toString();
    }

    public static Employee buildEmployee() {
        return Employee.builder()
                .id(UniqueId.generate())
                .email(getRandomEmail())
                .password(getSecurePassword())
                .role(Role.USER)
                .isVerified(true)
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    public static Password getSecurePassword() {
        return Password.create("Secure89102@@3").get();
    }

    public static Email getRandomEmail() {
        return Email.create("example" + UUID.randomUUID().toString()
                .replace("-", "")
                .replace("_", "")
                .substring(0, 8) + "@gmail.com").get();
    }
}

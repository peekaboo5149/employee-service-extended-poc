package com.deloitte.employees.domain.auth.entities;

import com.deloitte.employees.common.enums.EmployeeStatus;
import com.deloitte.employees.common.enums.Role;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.Password;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Employee {

    private UniqueId id;
    private Email email;
    private Password password;
    private Boolean isVerified;
    private EmployeeStatus status;
    private Role role;
    private LocalDateTime lastUpdatedAt;
    private LocalDateTime createdAt;

    public void verify() {
        this.isVerified = true;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void changeStatus(EmployeeStatus newStatus) {
        this.status = newStatus;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void updatePassword(Password newPassword) {
        this.password = newPassword;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}

package com.deloitte.employees.presentation.dto.response;

import com.deloitte.employees.common.enums.EmployeeStatus;
import com.deloitte.employees.common.enums.Role;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Data;

import static com.deloitte.employees.helper.WebConstants.*;

@Data
@Builder(toBuilder = true)
public class EmployeeBasicDetail {

    private String id;
    private String email;
    private boolean isVerified;
    private Role role;
    private EmployeeStatus status;


    public static EmployeeBasicDetail from(Claims employeeEntity) {
        return EmployeeBasicDetail.builder()
                .id(employeeEntity.get(ID, String.class))
                .email(employeeEntity.getSubject())
                .isVerified(employeeEntity.get(IS_VERIFIED, Boolean.class))
                .role(Role.valueOf(employeeEntity.get(ROLE, String.class)))
                .status(EmployeeStatus.valueOf(employeeEntity.get(STATUS, String.class)))
                .build();
    }


}

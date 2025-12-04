package com.deloitte.employees.presentation.seeders.employees;

import com.deloitte.employees.common.enums.EmployeeStatus;
import com.deloitte.employees.common.enums.Role;
import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.repositories.EmployeeRepositoryFacade;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.Password;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepositoryFacade employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... ignored) {
        log.info("🔄 Starting employee seeder...");

        seedAdmin();
        seedVerifiedUser();

        log.info("✅ Employee seeding finished");
        log.info("--------------------------------");
    }

    // -----------------------------------------------------
    // ADMIN USER
    // -----------------------------------------------------
    private void seedAdmin() {
        log.info("Trying to seed ADMIN user...");

        Email email = Email.create("admin@system.com")
                .getOrElseThrow(() -> new RuntimeException("Invalid seed email"));

        if (employeeExists(email)) {
            log.info("Admin already exists → skipping.");
            return;
        }

        Password password = Password.create(passwordEncoder.encode("Admin@123"))
                .getOrElseThrow(() -> new RuntimeException("Invalid password"));

        Employee employee = Employee.builder()
                .id(UniqueId.generate())
                .email(email)
                .password(password)
                .role(Role.ADMIN)
                .status(EmployeeStatus.ACTIVE)
                .isVerified(true)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        saveEmployee(employee, "ADMIN");
    }

    // -----------------------------------------------------
    // VERIFIED USER
    // -----------------------------------------------------
    private void seedVerifiedUser() {
        log.info("Trying to seed VERIFIED USER...");

        Email email = Email.create("verified.user@system.com")
                .getOrElseThrow(() -> new RuntimeException("Invalid seed email"));

        if (employeeExists(email)) {
            log.info("Verified User already exists → skipping.");
            return;
        }

        Password password = Password.create(passwordEncoder.encode("User@123"))
                .getOrElseThrow(() -> new RuntimeException("Invalid password"));

        Employee employee = Employee.builder()
                .id(UniqueId.generate())
                .email(email)
                .password(password)
                .role(Role.USER)
                .status(EmployeeStatus.ACTIVE)
                .isVerified(true)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        saveEmployee(employee, "VERIFIED USER");
    }

    // -----------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------

    private boolean employeeExists(Email email) {
        Either<OperationFailure, Option<Employee>> existing = employeeRepository.findByEmail(email);
        return existing.isRight() && existing.get().isDefined();
    }

    private void saveEmployee(Employee employee, String label) {
        Either<OperationFailure, Employee> result = employeeRepository.save(employee);

        if (result.isLeft()) {
            log.error("❌ Failed to seed {} → {}", label, result.getLeft().errorDetails());
            return;
        }

        log.info("✅ {} seeded successfully → {}", label, result.get().getEmail().getValue());
    }
}

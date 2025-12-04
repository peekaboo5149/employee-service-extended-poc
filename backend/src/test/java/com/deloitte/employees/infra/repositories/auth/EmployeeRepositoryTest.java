package com.deloitte.employees.infra.repositories.auth;

import com.deloitte.employees.common.enums.Role;
import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ResourceOperationFailure;
import com.deloitte.employees.common.exceptions.SystemFailure;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.infra.jpa.mapper.EmployeeDataMapper;
import com.deloitte.employees.infra.jpa.repositories.auth.EmployeeEntityRepository;
import com.deloitte.employees.utils.TestUtils;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({EmployeeRepository.class, EmployeeDataMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Employee employee0;

    @BeforeEach
    void setUp() {
        truncateTable();
        employee0 = TestUtils.buildEmployee();
    }

    ///  SAVE

    @Test
    void createEmployee_shouldSaveEmployee() {
        Either<OperationFailure, Employee> result = employeeRepository.save(employee0);
        assertTrue(result.isRight());
        employeeEntityRepository.flush();

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM employees WHERE id = ?",
                Integer.class,
                employee0.getId().getValue()
        );
        assertEquals(1, count, "Data not saved in DB");

    }

    @Test
    void createEmployee_shouldFailIfEmailExists() {
        save(employee0);
        employeeEntityRepository.flush();
        Either<OperationFailure, Employee> result = employeeRepository.save(employee0);
        employeeEntityRepository.flush();
        assertTrue(result.isLeft());

        OperationFailure left = result.getLeft();
        assertInstanceOf(ResourceOperationFailure.ResourceAlreadyExistsFailure.class, left);
    }

    @DirtiesContext
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void createEmployee_shouldHandleDatabaseErrorGracefully() {
        destroyTable();
        Either<OperationFailure, Employee> result =
                employeeRepository.save(employee0);
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isInstanceOf(SystemFailure.UnexpectedFailure.class);
    }

    ///  FIND BY ID

    @Test
    void findById_shouldReturnEmployeeWhenExists() {
        // Arrange
        save(employee0);
        employeeEntityRepository.flush();

        // Act
        var result = employeeRepository.findById(employee0.getId());

        // Assert
        assertTrue(result.isRight());
        assertTrue(result.get().isDefined());

        Employee found = result.get().get();
        assertEquals(employee0.getId().getValue(), found.getId().getValue());
        assertEquals(employee0.getEmail().getValue(), found.getEmail().getValue());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        // Act
        var result = employeeRepository.findById(employee0.getId());

        // Assert
        assertTrue(result.isRight());
        assertTrue(result.get().isEmpty());
    }

    @DirtiesContext
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void findById_shouldHandleDatabaseErrorGracefully() {
        // Arrange: simulate DB failure
        destroyTable();

        // Act
        var result = employeeRepository.findById(employee0.getId());

        // Assert
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isInstanceOf(SystemFailure.UnexpectedFailure.class);
    }

    ///  FIND BY EMAIL

    @Test
    void findByEmail_shouldReturnEmployeeWhenExists() {
        // Arrange
        save(employee0);
        employeeEntityRepository.flush();

        // Act
        var result = employeeRepository.findByEmail(employee0.getEmail());

        // Assert
        assertTrue(result.isRight());
        assertTrue(result.get().isDefined());

        Employee found = result.get().get();
        assertEquals(employee0.getEmail().getValue(), found.getEmail().getValue());
        assertEquals(employee0.getId().getValue(), found.getId().getValue());
    }

    @Test
    void findByEmail_shouldReturnEmptyWhenNotExists() {
        // Act
        var result = employeeRepository.findByEmail(employee0.getEmail());

        // Assert
        assertTrue(result.isRight());
        assertTrue(result.get().isEmpty());
    }

    @DirtiesContext
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void findByEmail_shouldHandleDatabaseErrorGracefully() {
        // Arrange – simulate DB failure
        destroyTable();

        // Act
        var result = employeeRepository.findByEmail(employee0.getEmail());

        // Assert
        assertTrue(result.isLeft());
        assertInstanceOf(SystemFailure.UnexpectedFailure.class, result.getLeft());
    }

    ///  UPDATE

    @Test
    void updateEmployee_shouldUpdateExistingEmployee() {
        // Arrange — Save employee in DB
        save(employee0);
        employeeEntityRepository.flush();

        // Modify employee (domain-side change)
        Employee updated = employee0.toBuilder()
                .role(Role.ADMIN)
                .isVerified(true)
                .build();

        // Act
        Either<OperationFailure, Employee> result = employeeRepository.update(updated);
        employeeEntityRepository.flush();

        // Assert
        assertTrue(result.isRight());
        Employee updatedEmployee = result.get();

        assertEquals(Role.ADMIN, updatedEmployee.getRole());
        assertTrue(updatedEmployee.getIsVerified());

        // Also verify in DB
        var dbRole = jdbcTemplate.queryForObject(
                "SELECT role FROM employees WHERE id = ?",
                String.class,
                employee0.getId().getValue()
        );
        assertEquals("ADMIN", dbRole);
    }

    @Test
    void updateEmployee_shouldFailIfEmployeeDoesNotExist() {
        // No save() call → employee is not in DB

        Either<OperationFailure, Employee> result = employeeRepository.update(employee0);

        assertTrue(result.isLeft());
        assertInstanceOf(ResourceOperationFailure.ResourceNotFoundFailure.class, result.getLeft());
    }

    @DirtiesContext
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void updateEmployee_shouldHandleDatabaseErrorGracefully() {
        // Break schema
        destroyTable();

        Either<OperationFailure, Employee> result =
                employeeRepository.update(employee0);

        assertTrue(result.isLeft());
        assertInstanceOf(SystemFailure.UnexpectedFailure.class, result.getLeft());
    }


    /// ---- HELPER ------
    private void save(Employee employee) {
        Role role = Objects.isNull(employee.getRole()) ? Role.USER : employee.getRole();
        String status = Objects.isNull(employee.getStatus()) ? "ACTIVE" : employee.getStatus().name();
        LocalDateTime createdAt = Objects.isNull(employee.getCreatedAt()) ? LocalDateTime.now() : employee.getCreatedAt();
        LocalDateTime updatedAt = Objects.isNull(employee.getLastUpdatedAt()) ? LocalDateTime.now() : employee.getLastUpdatedAt();

        jdbcTemplate.update(
                """
                        INSERT INTO employees (id, email, password,is_verified, role, status,created_at, last_updated_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                employee.getId().getValue(),
                employee.getEmail().getValue(),
                employee.getPassword().getValue(),
                employee.getIsVerified(),
                role.name(),
                status,
                createdAt,
                updatedAt
        );

    }

    private void truncateTable() {
        jdbcTemplate.execute("TRUNCATE TABLE employees");
        System.out.println("Truncated employees table done");
    }

    private void destroyTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS employees");
    }
}

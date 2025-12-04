package com.deloitte.employees.infra.repositories.departments;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.common.exceptions.ResourceOperationFailure;
import com.deloitte.employees.common.exceptions.SystemFailure;
import com.deloitte.employees.domain.departments.entities.Department;
import com.deloitte.employees.infra.jpa.mapper.DepartmentMapper;
import com.deloitte.employees.infra.jpa.repositories.departments.DepartmentEntityRepository;
import com.deloitte.employees.infra.jpa.repositories.departments.DesignationEntityRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DepartmentRepository.class, DepartmentMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;

    @Autowired
    private DesignationEntityRepository designationRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Department department0;

    @BeforeEach
    void setUp() {
        truncateTable();
        department0 = TestUtils.buildDepartment();
    }

    @Test
    void save_shouldSaveDepartment() {
        Either<OperationFailure, Department> result = departmentRepository.save(department0);
        departmentEntityRepository.flush();

        assertTrue(result.isRight());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM departments WHERE id = ?",
                Integer.class,
                department0.getId()
        );
        assertEquals(1, count, "Data not saved in DB");

    }

    @Test
    void save_shouldFailIfDepartmentExists() {
        save(department0);
        Either<OperationFailure, Department> result = departmentRepository.save(department0);
        departmentEntityRepository.flush();

        assertTrue(result.isLeft());
        OperationFailure left = result.getLeft();
        assertInstanceOf(ResourceOperationFailure.ResourceAlreadyExistsFailure.class, left);
    }

    @DirtiesContext
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test
    void save_shouldHandleDatabaseErrorGracefully() {
        destroyTable();
        Either<OperationFailure, Department> result = departmentRepository.save(department0);
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isInstanceOf(SystemFailure.UnexpectedFailure.class);
    }


    /// ---- HELPER ------
    private void save(Department department) {
        jdbcTemplate.update(
                """
                        INSERT INTO departments (id, title, description)
                        VALUES (?, ?, ?)
                        """,
                department.getId(),
                department.getTitle(),
                department.getDescription()
        );

    }

    private void truncateTable() {
        designationRepo.deleteAll();
        departmentEntityRepository.deleteAll();
    }

    private void destroyTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS designations");
        jdbcTemplate.execute("DROP TABLE IF EXISTS departments");
        System.out.println("Department table destroyed");

    }

}
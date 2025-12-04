package com.deloitte.employees.infra.jpa.repositories.departments;

import com.deloitte.employees.infra.jpa.entities.departments.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, String>
        , JpaSpecificationExecutor<DepartmentEntity> {

    Optional<DepartmentEntity> findByTitle(String title);


}

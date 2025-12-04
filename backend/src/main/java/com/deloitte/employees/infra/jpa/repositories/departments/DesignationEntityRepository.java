package com.deloitte.employees.infra.jpa.repositories.departments;

import com.deloitte.employees.infra.jpa.entities.departments.DesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationEntityRepository extends JpaRepository<DesignationEntity, String> {

    Optional<DesignationEntity> findByTitle(String title);
}

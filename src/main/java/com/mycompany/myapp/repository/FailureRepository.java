package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Failure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Failure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FailureRepository extends JpaRepository<Failure, Long>, JpaSpecificationExecutor<Failure> {}

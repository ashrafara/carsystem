package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dispatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dispatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, Long>, JpaSpecificationExecutor<Dispatch> {}

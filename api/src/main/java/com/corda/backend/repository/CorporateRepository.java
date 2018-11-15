package com.corda.backend.repository;

import com.corda.backend.domain.Corporate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {

}

package com.landexp.repository;

import com.landexp.domain.ServiceFee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {

}

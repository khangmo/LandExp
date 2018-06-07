package com.landexp.repository;

import com.landexp.domain.PotentialCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PotentialCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PotentialCustomerRepository extends JpaRepository<PotentialCustomer, Long>, JpaSpecificationExecutor<PotentialCustomer> {

    @Query("select potential_customer from PotentialCustomer potential_customer where potential_customer.customer.login = ?#{principal.username}")
    List<PotentialCustomer> findByCustomerIsCurrentUser();

    @Query("select potential_customer from PotentialCustomer potential_customer where potential_customer.createBy.login = ?#{principal.username}")
    List<PotentialCustomer> findByCreateByIsCurrentUser();

    @Query("select potential_customer from PotentialCustomer potential_customer where potential_customer.updateBy.login = ?#{principal.username}")
    List<PotentialCustomer> findByUpdateByIsCurrentUser();

}

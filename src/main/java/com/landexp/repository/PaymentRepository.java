package com.landexp.repository;

import com.landexp.domain.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    @Query("select payment from Payment payment where payment.customer.login = ?#{principal.username}")
    List<Payment> findByCustomerIsCurrentUser();

    @Query("select payment from Payment payment where payment.createBy.login = ?#{principal.username}")
    List<Payment> findByCreateByIsCurrentUser();

    @Query("select payment from Payment payment where payment.updateBy.login = ?#{principal.username}")
    List<Payment> findByUpdateByIsCurrentUser();

}

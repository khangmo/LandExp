package com.landexp.repository;

import com.landexp.domain.UserFinancial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserFinancial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFinancialRepository extends JpaRepository<UserFinancial, Long>, JpaSpecificationExecutor<UserFinancial> {

    @Query("select user_financial from UserFinancial user_financial where user_financial.user.login = ?#{principal.username}")
    List<UserFinancial> findByUserIsCurrentUser();

}

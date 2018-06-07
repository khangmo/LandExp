package com.landexp.repository;

import com.landexp.domain.UserSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, JpaSpecificationExecutor<UserSubscription> {

    @Query("select user_subscription from UserSubscription user_subscription where user_subscription.user.login = ?#{principal.username}")
    List<UserSubscription> findByUserIsCurrentUser();

}

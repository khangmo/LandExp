package com.landexp.repository;

import com.landexp.domain.UserTracking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTrackingRepository extends JpaRepository<UserTracking, Long>, JpaSpecificationExecutor<UserTracking> {

    @Query("select user_tracking from UserTracking user_tracking where user_tracking.user.login = ?#{principal.username}")
    List<UserTracking> findByUserIsCurrentUser();

}

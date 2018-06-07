package com.landexp.repository;

import com.landexp.domain.UserFeed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserFeed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFeedRepository extends JpaRepository<UserFeed, Long> {

    @Query("select user_feed from UserFeed user_feed where user_feed.user.login = ?#{principal.username}")
    List<UserFeed> findByUserIsCurrentUser();

}

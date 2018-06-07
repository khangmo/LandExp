package com.landexp.repository;

import com.landexp.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("select user_profile from UserProfile user_profile where user_profile.user.login = ?#{principal.username}")
    List<UserProfile> findByUserIsCurrentUser();

}

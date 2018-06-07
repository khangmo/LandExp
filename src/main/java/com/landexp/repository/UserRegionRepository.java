package com.landexp.repository;

import com.landexp.domain.UserRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserRegion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {

    @Query("select user_region from UserRegion user_region where user_region.user.login = ?#{principal.username}")
    List<UserRegion> findByUserIsCurrentUser();

    @Query(value = "select distinct user_region from UserRegion user_region left join fetch user_region.regions",
        countQuery = "select count(distinct user_region) from UserRegion user_region")
    Page<UserRegion> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct user_region from UserRegion user_region left join fetch user_region.regions")
    List<UserRegion> findAllWithEagerRelationships();

    @Query("select user_region from UserRegion user_region left join fetch user_region.regions where user_region.id =:id")
    Optional<UserRegion> findOneWithEagerRelationships(@Param("id") Long id);

}

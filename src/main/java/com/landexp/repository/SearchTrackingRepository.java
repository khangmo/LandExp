package com.landexp.repository;

import com.landexp.domain.SearchTracking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SearchTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchTrackingRepository extends JpaRepository<SearchTracking, Long>, JpaSpecificationExecutor<SearchTracking> {

    @Query("select search_tracking from SearchTracking search_tracking where search_tracking.user.login = ?#{principal.username}")
    List<SearchTracking> findByUserIsCurrentUser();

}

package com.landexp.repository;

import com.landexp.domain.HouseTracking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the HouseTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseTrackingRepository extends JpaRepository<HouseTracking, Long>, JpaSpecificationExecutor<HouseTracking> {

    @Query("select house_tracking from HouseTracking house_tracking where house_tracking.user.login = ?#{principal.username}")
    List<HouseTracking> findByUserIsCurrentUser();

}

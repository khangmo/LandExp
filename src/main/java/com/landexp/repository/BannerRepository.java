package com.landexp.repository;

import com.landexp.domain.Banner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Banner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("select banner from Banner banner where banner.createBy.login = ?#{principal.username}")
    List<Banner> findByCreateByIsCurrentUser();

    @Query("select banner from Banner banner where banner.updateBy.login = ?#{principal.username}")
    List<Banner> findByUpdateByIsCurrentUser();

}

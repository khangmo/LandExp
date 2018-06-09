package com.landexp.repository;

import com.landexp.domain.LandProjectPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LandProjectPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandProjectPhotoRepository extends JpaRepository<LandProjectPhoto, Long> {

    @Query("select land_project_photo from LandProjectPhoto land_project_photo where land_project_photo.createBy.login = ?#{principal.username}")
    List<LandProjectPhoto> findByCreateByIsCurrentUser();

    @Query("select land_project_photo from LandProjectPhoto land_project_photo where land_project_photo.updateBy.login = ?#{principal.username}")
    List<LandProjectPhoto> findByUpdateByIsCurrentUser();

}

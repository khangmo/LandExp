package com.landexp.repository;

import com.landexp.domain.LandProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LandProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandProjectRepository extends JpaRepository<LandProject, Long> {

    @Query("select land_project from LandProject land_project where land_project.createBy.login = ?#{principal.username}")
    List<LandProject> findByCreateByIsCurrentUser();

    @Query("select land_project from LandProject land_project where land_project.updateBy.login = ?#{principal.username}")
    List<LandProject> findByUpdateByIsCurrentUser();

}

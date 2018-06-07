package com.landexp.repository;

import com.landexp.domain.LandProjects;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LandProjects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandProjectsRepository extends JpaRepository<LandProjects, Long> {

    @Query("select land_projects from LandProjects land_projects where land_projects.createBy.login = ?#{principal.username}")
    List<LandProjects> findByCreateByIsCurrentUser();

    @Query("select land_projects from LandProjects land_projects where land_projects.updateBy.login = ?#{principal.username}")
    List<LandProjects> findByUpdateByIsCurrentUser();

}

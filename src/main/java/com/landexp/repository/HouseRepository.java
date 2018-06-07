package com.landexp.repository;

import com.landexp.domain.House;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the House entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseRepository extends JpaRepository<House, Long>, JpaSpecificationExecutor<House> {

    @Query("select house from House house where house.createBy.login = ?#{principal.username}")
    List<House> findByCreateByIsCurrentUser();

    @Query("select house from House house where house.updateBy.login = ?#{principal.username}")
    List<House> findByUpdateByIsCurrentUser();

}

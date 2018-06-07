package com.landexp.repository;

import com.landexp.domain.HousePhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HousePhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HousePhotoRepository extends JpaRepository<HousePhoto, Long> {

}

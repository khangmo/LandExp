package com.landexp.repository;

import com.landexp.domain.HousePhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the HousePhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HousePhotoRepository extends JpaRepository<HousePhoto, Long> {

    @Query("select house_photo from HousePhoto house_photo where house_photo.user.login = ?#{principal.username}")
    List<HousePhoto> findByUserIsCurrentUser();

}

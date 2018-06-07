package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.HousePhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HousePhoto and its DTO HousePhotoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HousePhotoMapper extends EntityMapper<HousePhotoDTO, HousePhoto> {



    default HousePhoto fromId(Long id) {
        if (id == null) {
            return null;
        }
        HousePhoto housePhoto = new HousePhoto();
        housePhoto.setId(id);
        return housePhoto;
    }
}

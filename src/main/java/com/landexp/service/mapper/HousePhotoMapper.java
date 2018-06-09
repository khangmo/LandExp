package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.HousePhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HousePhoto and its DTO HousePhotoDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, UserMapper.class})
public interface HousePhotoMapper extends EntityMapper<HousePhotoDTO, HousePhoto> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    HousePhotoDTO toDto(HousePhoto housePhoto);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "userId", target = "user")
    HousePhoto toEntity(HousePhotoDTO housePhotoDTO);

    default HousePhoto fromId(Long id) {
        if (id == null) {
            return null;
        }
        HousePhoto housePhoto = new HousePhoto();
        housePhoto.setId(id);
        return housePhoto;
    }
}

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
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    HousePhotoDTO toDto(HousePhoto housePhoto);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
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

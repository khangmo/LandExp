package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.LandProjectPhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LandProjectPhoto and its DTO LandProjectPhotoDTO.
 */
@Mapper(componentModel = "spring", uses = {LandProjectMapper.class, UserMapper.class})
public interface LandProjectPhotoMapper extends EntityMapper<LandProjectPhotoDTO, LandProjectPhoto> {

    @Mapping(source = "landProject.id", target = "landProjectId")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    LandProjectPhotoDTO toDto(LandProjectPhoto landProjectPhoto);

    @Mapping(source = "landProjectId", target = "landProject")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    LandProjectPhoto toEntity(LandProjectPhotoDTO landProjectPhotoDTO);

    default LandProjectPhoto fromId(Long id) {
        if (id == null) {
            return null;
        }
        LandProjectPhoto landProjectPhoto = new LandProjectPhoto();
        landProjectPhoto.setId(id);
        return landProjectPhoto;
    }
}

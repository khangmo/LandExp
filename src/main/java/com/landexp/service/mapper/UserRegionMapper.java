package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.UserRegionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserRegion and its DTO UserRegionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RegionMapper.class})
public interface UserRegionMapper extends EntityMapper<UserRegionDTO, UserRegion> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserRegionDTO toDto(UserRegion userRegion);

    @Mapping(source = "userId", target = "user")
    UserRegion toEntity(UserRegionDTO userRegionDTO);

    default UserRegion fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRegion userRegion = new UserRegion();
        userRegion.setId(id);
        return userRegion;
    }
}

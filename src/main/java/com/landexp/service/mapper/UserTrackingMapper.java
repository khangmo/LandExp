package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.UserTrackingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserTracking and its DTO UserTrackingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserTrackingMapper extends EntityMapper<UserTrackingDTO, UserTracking> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserTrackingDTO toDto(UserTracking userTracking);

    @Mapping(source = "userId", target = "user")
    UserTracking toEntity(UserTrackingDTO userTrackingDTO);

    default UserTracking fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserTracking userTracking = new UserTracking();
        userTracking.setId(id);
        return userTracking;
    }
}

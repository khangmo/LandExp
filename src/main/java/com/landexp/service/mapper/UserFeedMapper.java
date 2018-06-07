package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.UserFeedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserFeed and its DTO UserFeedDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserFeedMapper extends EntityMapper<UserFeedDTO, UserFeed> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserFeedDTO toDto(UserFeed userFeed);

    @Mapping(source = "userId", target = "user")
    UserFeed toEntity(UserFeedDTO userFeedDTO);

    default UserFeed fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserFeed userFeed = new UserFeed();
        userFeed.setId(id);
        return userFeed;
    }
}

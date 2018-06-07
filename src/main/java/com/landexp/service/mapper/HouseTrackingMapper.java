package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.HouseTrackingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HouseTracking and its DTO HouseTrackingDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, UserMapper.class})
public interface HouseTrackingMapper extends EntityMapper<HouseTrackingDTO, HouseTracking> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    HouseTrackingDTO toDto(HouseTracking houseTracking);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "userId", target = "user")
    HouseTracking toEntity(HouseTrackingDTO houseTrackingDTO);

    default HouseTracking fromId(Long id) {
        if (id == null) {
            return null;
        }
        HouseTracking houseTracking = new HouseTracking();
        houseTracking.setId(id);
        return houseTracking;
    }
}

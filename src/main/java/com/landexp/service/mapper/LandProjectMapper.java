package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.LandProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LandProject and its DTO LandProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, StreetMapper.class, UserMapper.class})
public interface LandProjectMapper extends EntityMapper<LandProjectDTO, LandProject> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "street.id", target = "streetId")
    @Mapping(source = "street.name", target = "streetName")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    LandProjectDTO toDto(LandProject landProject);

    @Mapping(target = "photos", ignore = true)
    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "streetId", target = "street")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    LandProject toEntity(LandProjectDTO landProjectDTO);

    default LandProject fromId(Long id) {
        if (id == null) {
            return null;
        }
        LandProject landProject = new LandProject();
        landProject.setId(id);
        return landProject;
    }
}

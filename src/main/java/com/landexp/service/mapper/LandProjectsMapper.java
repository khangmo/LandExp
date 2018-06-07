package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.LandProjectsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LandProjects and its DTO LandProjectsDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, StreetMapper.class, UserMapper.class})
public interface LandProjectsMapper extends EntityMapper<LandProjectsDTO, LandProjects> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "street.id", target = "streetId")
    @Mapping(source = "street.name", target = "streetName")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    LandProjectsDTO toDto(LandProjects landProjects);

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "streetId", target = "street")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    LandProjects toEntity(LandProjectsDTO landProjectsDTO);

    default LandProjects fromId(Long id) {
        if (id == null) {
            return null;
        }
        LandProjects landProjects = new LandProjects();
        landProjects.setId(id);
        return landProjects;
    }
}

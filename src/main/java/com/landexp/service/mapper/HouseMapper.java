package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.HouseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity House and its DTO HouseDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, StreetMapper.class, LandProjectsMapper.class, UserMapper.class})
public interface HouseMapper extends EntityMapper<HouseDTO, House> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "street.id", target = "streetId")
    @Mapping(source = "street.name", target = "streetName")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    HouseDTO toDto(House house);

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "streetId", target = "street")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    House toEntity(HouseDTO houseDTO);

    default House fromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }
}

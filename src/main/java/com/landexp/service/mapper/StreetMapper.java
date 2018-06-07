package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.StreetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Street and its DTO StreetDTO.
 */
@Mapper(componentModel = "spring", uses = {DistrictMapper.class})
public interface StreetMapper extends EntityMapper<StreetDTO, Street> {

    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "districtName")
    StreetDTO toDto(Street street);

    @Mapping(source = "districtId", target = "district")
    Street toEntity(StreetDTO streetDTO);

    default Street fromId(Long id) {
        if (id == null) {
            return null;
        }
        Street street = new Street();
        street.setId(id);
        return street;
    }
}

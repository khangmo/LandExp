package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.BannerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Banner and its DTO BannerDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BannerMapper extends EntityMapper<BannerDTO, Banner> {

    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    BannerDTO toDto(Banner banner);

    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    Banner toEntity(BannerDTO bannerDTO);

    default Banner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Banner banner = new Banner();
        banner.setId(id);
        return banner;
    }
}

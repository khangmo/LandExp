package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.UserFinancialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserFinancial and its DTO UserFinancialDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserFinancialMapper extends EntityMapper<UserFinancialDTO, UserFinancial> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserFinancialDTO toDto(UserFinancial userFinancial);

    @Mapping(source = "userId", target = "user")
    UserFinancial toEntity(UserFinancialDTO userFinancialDTO);

    default UserFinancial fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserFinancial userFinancial = new UserFinancial();
        userFinancial.setId(id);
        return userFinancial;
    }
}

package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.PotentialCustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PotentialCustomer and its DTO PotentialCustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PotentialCustomerMapper extends EntityMapper<PotentialCustomerDTO, PotentialCustomer> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.login", target = "customerLogin")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    PotentialCustomerDTO toDto(PotentialCustomer potentialCustomer);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    PotentialCustomer toEntity(PotentialCustomerDTO potentialCustomerDTO);

    default PotentialCustomer fromId(Long id) {
        if (id == null) {
            return null;
        }
        PotentialCustomer potentialCustomer = new PotentialCustomer();
        potentialCustomer.setId(id);
        return potentialCustomer;
    }
}

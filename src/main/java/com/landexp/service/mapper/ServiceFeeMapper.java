package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.ServiceFeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServiceFee and its DTO ServiceFeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceFeeMapper extends EntityMapper<ServiceFeeDTO, ServiceFee> {



    default ServiceFee fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setId(id);
        return serviceFee;
    }
}

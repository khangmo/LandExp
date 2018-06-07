package com.landexp.service.mapper;

import com.landexp.domain.*;
import com.landexp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payment and its DTO PaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, UserMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.login", target = "customerLogin")
    @Mapping(source = "createBy.id", target = "createById")
    @Mapping(source = "createBy.login", target = "createByLogin")
    @Mapping(source = "updateBy.id", target = "updateById")
    @Mapping(source = "updateBy.login", target = "updateByLogin")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "createById", target = "createBy")
    @Mapping(source = "updateById", target = "updateBy")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}

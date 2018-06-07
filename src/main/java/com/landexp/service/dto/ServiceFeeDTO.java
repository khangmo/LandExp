package com.landexp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.landexp.domain.enumeration.SaleType;

/**
 * A DTO for the ServiceFee entity.
 */
public class ServiceFeeDTO implements Serializable {

    private Long id;

    private SaleType saleType;

    private Float fee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceFeeDTO serviceFeeDTO = (ServiceFeeDTO) o;
        if (serviceFeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceFeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceFeeDTO{" +
            "id=" + getId() +
            ", saleType='" + getSaleType() + "'" +
            ", fee=" + getFee() +
            "}";
    }
}

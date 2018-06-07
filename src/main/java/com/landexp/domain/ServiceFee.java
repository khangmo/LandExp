package com.landexp.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.landexp.domain.enumeration.SaleType;

/**
 * A ServiceFee.
 */
@Entity
@Table(name = "service_fee")
@Document(indexName = "servicefee")
public class ServiceFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    private SaleType saleType;

    @Column(name = "fee")
    private Float fee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public ServiceFee saleType(SaleType saleType) {
        this.saleType = saleType;
        return this;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public Float getFee() {
        return fee;
    }

    public ServiceFee fee(Float fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceFee serviceFee = (ServiceFee) o;
        if (serviceFee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceFee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceFee{" +
            "id=" + getId() +
            ", saleType='" + getSaleType() + "'" +
            ", fee=" + getFee() +
            "}";
    }
}

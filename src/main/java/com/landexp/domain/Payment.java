package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.landexp.domain.enumeration.PaymentStatusType;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "money")
    private Float money;

    @Column(name = "paid_time")
    private LocalDate paidTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusType paymentStatus;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDate createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private House house;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User customer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User createBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User updateBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Payment code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getMoney() {
        return money;
    }

    public Payment money(Float money) {
        this.money = money;
        return this;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public LocalDate getPaidTime() {
        return paidTime;
    }

    public Payment paidTime(LocalDate paidTime) {
        this.paidTime = paidTime;
        return this;
    }

    public void setPaidTime(LocalDate paidTime) {
        this.paidTime = paidTime;
    }

    public PaymentStatusType getPaymentStatus() {
        return paymentStatus;
    }

    public Payment paymentStatus(PaymentStatusType paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(PaymentStatusType paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Payment createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public Payment updateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public House getHouse() {
        return house;
    }

    public Payment house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getCustomer() {
        return customer;
    }

    public Payment customer(User user) {
        this.customer = user;
        return this;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public User getCreateBy() {
        return createBy;
    }

    public Payment createBy(User user) {
        this.createBy = user;
        return this;
    }

    public void setCreateBy(User user) {
        this.createBy = user;
    }

    public User getUpdateBy() {
        return updateBy;
    }

    public Payment updateBy(User user) {
        this.updateBy = user;
        return this;
    }

    public void setUpdateBy(User user) {
        this.updateBy = user;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", money=" + getMoney() +
            ", paidTime='" + getPaidTime() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}

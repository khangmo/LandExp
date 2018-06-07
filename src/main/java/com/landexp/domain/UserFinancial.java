package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserFinancial.
 */
@Entity
@Table(name = "user_financial")
@Document(indexName = "userfinancial")
public class UserFinancial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_price")
    private Float housePrice;

    @Column(name = "house_acreage")
    private Float houseAcreage;

    @Column(name = "loan_rate")
    private Float loanRate;

    @Column(name = "loan_from_people")
    private Float loanFromPeople;

    @Column(name = "customer_money_have")
    private Float customerMoneyHave;

    @Column(name = "customer_mobile")
    private String customerMobile;

    @Column(name = "customer_email")
    private String customerEmail;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getHousePrice() {
        return housePrice;
    }

    public UserFinancial housePrice(Float housePrice) {
        this.housePrice = housePrice;
        return this;
    }

    public void setHousePrice(Float housePrice) {
        this.housePrice = housePrice;
    }

    public Float getHouseAcreage() {
        return houseAcreage;
    }

    public UserFinancial houseAcreage(Float houseAcreage) {
        this.houseAcreage = houseAcreage;
        return this;
    }

    public void setHouseAcreage(Float houseAcreage) {
        this.houseAcreage = houseAcreage;
    }

    public Float getLoanRate() {
        return loanRate;
    }

    public UserFinancial loanRate(Float loanRate) {
        this.loanRate = loanRate;
        return this;
    }

    public void setLoanRate(Float loanRate) {
        this.loanRate = loanRate;
    }

    public Float getLoanFromPeople() {
        return loanFromPeople;
    }

    public UserFinancial loanFromPeople(Float loanFromPeople) {
        this.loanFromPeople = loanFromPeople;
        return this;
    }

    public void setLoanFromPeople(Float loanFromPeople) {
        this.loanFromPeople = loanFromPeople;
    }

    public Float getCustomerMoneyHave() {
        return customerMoneyHave;
    }

    public UserFinancial customerMoneyHave(Float customerMoneyHave) {
        this.customerMoneyHave = customerMoneyHave;
        return this;
    }

    public void setCustomerMoneyHave(Float customerMoneyHave) {
        this.customerMoneyHave = customerMoneyHave;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public UserFinancial customerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
        return this;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public UserFinancial customerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public User getUser() {
        return user;
    }

    public UserFinancial user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        UserFinancial userFinancial = (UserFinancial) o;
        if (userFinancial.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userFinancial.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserFinancial{" +
            "id=" + getId() +
            ", housePrice=" + getHousePrice() +
            ", houseAcreage=" + getHouseAcreage() +
            ", loanRate=" + getLoanRate() +
            ", loanFromPeople=" + getLoanFromPeople() +
            ", customerMoneyHave=" + getCustomerMoneyHave() +
            ", customerMobile='" + getCustomerMobile() + "'" +
            ", customerEmail='" + getCustomerEmail() + "'" +
            "}";
    }
}

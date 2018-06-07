package com.landexp.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the UserFinancial entity. This class is used in UserFinancialResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /user-financials?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserFinancialCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private FloatFilter housePrice;

    private FloatFilter houseAcreage;

    private FloatFilter loanRate;

    private FloatFilter loanFromPeople;

    private FloatFilter customerMoneyHave;

    private StringFilter customerMobile;

    private StringFilter customerEmail;

    private LongFilter userId;

    public UserFinancialCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(FloatFilter housePrice) {
        this.housePrice = housePrice;
    }

    public FloatFilter getHouseAcreage() {
        return houseAcreage;
    }

    public void setHouseAcreage(FloatFilter houseAcreage) {
        this.houseAcreage = houseAcreage;
    }

    public FloatFilter getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(FloatFilter loanRate) {
        this.loanRate = loanRate;
    }

    public FloatFilter getLoanFromPeople() {
        return loanFromPeople;
    }

    public void setLoanFromPeople(FloatFilter loanFromPeople) {
        this.loanFromPeople = loanFromPeople;
    }

    public FloatFilter getCustomerMoneyHave() {
        return customerMoneyHave;
    }

    public void setCustomerMoneyHave(FloatFilter customerMoneyHave) {
        this.customerMoneyHave = customerMoneyHave;
    }

    public StringFilter getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(StringFilter customerMobile) {
        this.customerMobile = customerMobile;
    }

    public StringFilter getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(StringFilter customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserFinancialCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (housePrice != null ? "housePrice=" + housePrice + ", " : "") +
                (houseAcreage != null ? "houseAcreage=" + houseAcreage + ", " : "") +
                (loanRate != null ? "loanRate=" + loanRate + ", " : "") +
                (loanFromPeople != null ? "loanFromPeople=" + loanFromPeople + ", " : "") +
                (customerMoneyHave != null ? "customerMoneyHave=" + customerMoneyHave + ", " : "") +
                (customerMobile != null ? "customerMobile=" + customerMobile + ", " : "") +
                (customerEmail != null ? "customerEmail=" + customerEmail + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}

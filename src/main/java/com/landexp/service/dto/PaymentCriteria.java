package com.landexp.service.dto;

import java.io.Serializable;
import com.landexp.domain.enumeration.PaymentStatusType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Payment entity. This class is used in PaymentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /payments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable {
    /**
     * Class for filtering PaymentStatusType
     */
    public static class PaymentStatusTypeFilter extends Filter<PaymentStatusType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter code;

    private FloatFilter money;

    private LocalDateFilter paidTime;

    private PaymentStatusTypeFilter paymentStatus;

    private LocalDateFilter createAt;

    private LocalDateFilter updateAt;

    private LongFilter houseId;

    private LongFilter customerId;

    private LongFilter createById;

    private LongFilter updateById;

    public PaymentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public FloatFilter getMoney() {
        return money;
    }

    public void setMoney(FloatFilter money) {
        this.money = money;
    }

    public LocalDateFilter getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(LocalDateFilter paidTime) {
        this.paidTime = paidTime;
    }

    public PaymentStatusTypeFilter getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusTypeFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateFilter getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateFilter createAt) {
        this.createAt = createAt;
    }

    public LocalDateFilter getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateFilter updateAt) {
        this.updateAt = updateAt;
    }

    public LongFilter getHouseId() {
        return houseId;
    }

    public void setHouseId(LongFilter houseId) {
        this.houseId = houseId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCreateById() {
        return createById;
    }

    public void setCreateById(LongFilter createById) {
        this.createById = createById;
    }

    public LongFilter getUpdateById() {
        return updateById;
    }

    public void setUpdateById(LongFilter updateById) {
        this.updateById = updateById;
    }

    @Override
    public String toString() {
        return "PaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (money != null ? "money=" + money + ", " : "") +
                (paidTime != null ? "paidTime=" + paidTime + ", " : "") +
                (paymentStatus != null ? "paymentStatus=" + paymentStatus + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (updateAt != null ? "updateAt=" + updateAt + ", " : "") +
                (houseId != null ? "houseId=" + houseId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (createById != null ? "createById=" + createById + ", " : "") +
                (updateById != null ? "updateById=" + updateById + ", " : "") +
            "}";
    }

}

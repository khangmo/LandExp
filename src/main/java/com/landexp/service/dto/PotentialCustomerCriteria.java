package com.landexp.service.dto;

import java.io.Serializable;
import com.landexp.domain.enumeration.CustomerLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the PotentialCustomer entity. This class is used in PotentialCustomerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /potential-customers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PotentialCustomerCriteria implements Serializable {
    /**
     * Class for filtering CustomerLevel
     */
    public static class CustomerLevelFilter extends Filter<CustomerLevel> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private CustomerLevelFilter level;

    private StringFilter description;

    private LocalDateFilter createAt;

    private LocalDateFilter updateAt;

    private LongFilter customerId;

    private LongFilter createById;

    private LongFilter updateById;

    public PotentialCustomerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public CustomerLevelFilter getLevel() {
        return level;
    }

    public void setLevel(CustomerLevelFilter level) {
        this.level = level;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        return "PotentialCustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (updateAt != null ? "updateAt=" + updateAt + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (createById != null ? "createById=" + createById + ", " : "") +
                (updateById != null ? "updateById=" + updateById + ", " : "") +
            "}";
    }

}

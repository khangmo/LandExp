package com.landexp.service.dto;

import java.io.Serializable;
import com.landexp.domain.enumeration.UserActivityType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the HouseTracking entity. This class is used in HouseTrackingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /house-trackings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HouseTrackingCriteria implements Serializable {
    /**
     * Class for filtering UserActivityType
     */
    public static class UserActivityTypeFilter extends Filter<UserActivityType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private UserActivityTypeFilter activityType;

    private StringFilter sourceId;

    private StringFilter sourceLink;

    private StringFilter description;

    private LocalDateFilter createAt;

    private LongFilter houseId;

    private LongFilter userId;

    public HouseTrackingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UserActivityTypeFilter getActivityType() {
        return activityType;
    }

    public void setActivityType(UserActivityTypeFilter activityType) {
        this.activityType = activityType;
    }

    public StringFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(StringFilter sourceId) {
        this.sourceId = sourceId;
    }

    public StringFilter getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(StringFilter sourceLink) {
        this.sourceLink = sourceLink;
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

    public LongFilter getHouseId() {
        return houseId;
    }

    public void setHouseId(LongFilter houseId) {
        this.houseId = houseId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "HouseTrackingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (activityType != null ? "activityType=" + activityType + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (sourceLink != null ? "sourceLink=" + sourceLink + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (houseId != null ? "houseId=" + houseId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}

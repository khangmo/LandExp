package com.landexp.service.dto;

import java.io.Serializable;
import com.landexp.domain.enumeration.UserActivityType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.LandType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the SearchTracking entity. This class is used in SearchTrackingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /search-trackings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SearchTrackingCriteria implements Serializable {
    /**
     * Class for filtering UserActivityType
     */
    public static class UserActivityTypeFilter extends Filter<UserActivityType> {
    }

    /**
     * Class for filtering DirectionType
     */
    public static class DirectionTypeFilter extends Filter<DirectionType> {
    }

    /**
     * Class for filtering LandType
     */
    public static class LandTypeFilter extends Filter<LandType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private UserActivityTypeFilter actionType;

    private StringFilter keyword;

    private FloatFilter costFrom;

    private FloatFilter costTo;

    private FloatFilter acreageFrom;

    private FloatFilter acreageTo;

    private DirectionTypeFilter direction;

    private StringFilter floor;

    private IntegerFilter bathRoom;

    private IntegerFilter bedRoom;

    private BooleanFilter parking;

    private LandTypeFilter landType;

    private LocalDateFilter createAt;

    private LongFilter userId;

    private LongFilter cityId;

    private LongFilter streetId;

    public SearchTrackingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UserActivityTypeFilter getActionType() {
        return actionType;
    }

    public void setActionType(UserActivityTypeFilter actionType) {
        this.actionType = actionType;
    }

    public StringFilter getKeyword() {
        return keyword;
    }

    public void setKeyword(StringFilter keyword) {
        this.keyword = keyword;
    }

    public FloatFilter getCostFrom() {
        return costFrom;
    }

    public void setCostFrom(FloatFilter costFrom) {
        this.costFrom = costFrom;
    }

    public FloatFilter getCostTo() {
        return costTo;
    }

    public void setCostTo(FloatFilter costTo) {
        this.costTo = costTo;
    }

    public FloatFilter getAcreageFrom() {
        return acreageFrom;
    }

    public void setAcreageFrom(FloatFilter acreageFrom) {
        this.acreageFrom = acreageFrom;
    }

    public FloatFilter getAcreageTo() {
        return acreageTo;
    }

    public void setAcreageTo(FloatFilter acreageTo) {
        this.acreageTo = acreageTo;
    }

    public DirectionTypeFilter getDirection() {
        return direction;
    }

    public void setDirection(DirectionTypeFilter direction) {
        this.direction = direction;
    }

    public StringFilter getFloor() {
        return floor;
    }

    public void setFloor(StringFilter floor) {
        this.floor = floor;
    }

    public IntegerFilter getBathRoom() {
        return bathRoom;
    }

    public void setBathRoom(IntegerFilter bathRoom) {
        this.bathRoom = bathRoom;
    }

    public IntegerFilter getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(IntegerFilter bedRoom) {
        this.bedRoom = bedRoom;
    }

    public BooleanFilter getParking() {
        return parking;
    }

    public void setParking(BooleanFilter parking) {
        this.parking = parking;
    }

    public LandTypeFilter getLandType() {
        return landType;
    }

    public void setLandType(LandTypeFilter landType) {
        this.landType = landType;
    }

    public LocalDateFilter getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateFilter createAt) {
        this.createAt = createAt;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getStreetId() {
        return streetId;
    }

    public void setStreetId(LongFilter streetId) {
        this.streetId = streetId;
    }

    @Override
    public String toString() {
        return "SearchTrackingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (actionType != null ? "actionType=" + actionType + ", " : "") +
                (keyword != null ? "keyword=" + keyword + ", " : "") +
                (costFrom != null ? "costFrom=" + costFrom + ", " : "") +
                (costTo != null ? "costTo=" + costTo + ", " : "") +
                (acreageFrom != null ? "acreageFrom=" + acreageFrom + ", " : "") +
                (acreageTo != null ? "acreageTo=" + acreageTo + ", " : "") +
                (direction != null ? "direction=" + direction + ", " : "") +
                (floor != null ? "floor=" + floor + ", " : "") +
                (bathRoom != null ? "bathRoom=" + bathRoom + ", " : "") +
                (bedRoom != null ? "bedRoom=" + bedRoom + ", " : "") +
                (parking != null ? "parking=" + parking + ", " : "") +
                (landType != null ? "landType=" + landType + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (cityId != null ? "cityId=" + cityId + ", " : "") +
                (streetId != null ? "streetId=" + streetId + ", " : "") +
            "}";
    }

}

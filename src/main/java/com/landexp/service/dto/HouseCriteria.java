package com.landexp.service.dto;

import java.io.Serializable;
import com.landexp.domain.enumeration.UserActionType;
import com.landexp.domain.enumeration.MoneyType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.LandType;
import com.landexp.domain.enumeration.SaleType;
import com.landexp.domain.enumeration.PresentType;
import com.landexp.domain.enumeration.StatusType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the House entity. This class is used in HouseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /houses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HouseCriteria implements Serializable {
    /**
     * Class for filtering UserActionType
     */
    public static class UserActionTypeFilter extends Filter<UserActionType> {
    }

    /**
     * Class for filtering MoneyType
     */
    public static class MoneyTypeFilter extends Filter<MoneyType> {
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

    /**
     * Class for filtering SaleType
     */
    public static class SaleTypeFilter extends Filter<SaleType> {
    }

    /**
     * Class for filtering PresentType
     */
    public static class PresentTypeFilter extends Filter<PresentType> {
    }

    /**
     * Class for filtering StatusType
     */
    public static class StatusTypeFilter extends Filter<StatusType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter avatar;

    private UserActionTypeFilter actionType;

    private StringFilter address;

    private FloatFilter money;

    private MoneyTypeFilter moneyType;

    private FloatFilter acreage;

    private FloatFilter discount;

    private DirectionTypeFilter direction;

    private DirectionTypeFilter directionBalcony;

    private StringFilter floor;

    private FloatFilter numberOfFloor;

    private IntegerFilter bathRoom;

    private IntegerFilter bedRoom;

    private BooleanFilter parking;

    private BooleanFilter furniture;

    private LandTypeFilter landType;

    private SaleTypeFilter saleType;

    private FloatFilter fee;

    private FloatFilter feeMax;

    private PresentTypeFilter present;

    private IntegerFilter hits;

    private StatusTypeFilter statusType;

    private LocalDateFilter createAt;

    private LocalDateFilter updateAt;

    private LongFilter photosId;

    private LongFilter cityId;

    private LongFilter districtId;

    private LongFilter streetId;

    private LongFilter projectId;

    private LongFilter createById;

    private LongFilter updateById;

    public HouseCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAvatar() {
        return avatar;
    }

    public void setAvatar(StringFilter avatar) {
        this.avatar = avatar;
    }

    public UserActionTypeFilter getActionType() {
        return actionType;
    }

    public void setActionType(UserActionTypeFilter actionType) {
        this.actionType = actionType;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public FloatFilter getMoney() {
        return money;
    }

    public void setMoney(FloatFilter money) {
        this.money = money;
    }

    public MoneyTypeFilter getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyTypeFilter moneyType) {
        this.moneyType = moneyType;
    }

    public FloatFilter getAcreage() {
        return acreage;
    }

    public void setAcreage(FloatFilter acreage) {
        this.acreage = acreage;
    }

    public FloatFilter getDiscount() {
        return discount;
    }

    public void setDiscount(FloatFilter discount) {
        this.discount = discount;
    }

    public DirectionTypeFilter getDirection() {
        return direction;
    }

    public void setDirection(DirectionTypeFilter direction) {
        this.direction = direction;
    }

    public DirectionTypeFilter getDirectionBalcony() {
        return directionBalcony;
    }

    public void setDirectionBalcony(DirectionTypeFilter directionBalcony) {
        this.directionBalcony = directionBalcony;
    }

    public StringFilter getFloor() {
        return floor;
    }

    public void setFloor(StringFilter floor) {
        this.floor = floor;
    }

    public FloatFilter getNumberOfFloor() {
        return numberOfFloor;
    }

    public void setNumberOfFloor(FloatFilter numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
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

    public BooleanFilter getFurniture() {
        return furniture;
    }

    public void setFurniture(BooleanFilter furniture) {
        this.furniture = furniture;
    }

    public LandTypeFilter getLandType() {
        return landType;
    }

    public void setLandType(LandTypeFilter landType) {
        this.landType = landType;
    }

    public SaleTypeFilter getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleTypeFilter saleType) {
        this.saleType = saleType;
    }

    public FloatFilter getFee() {
        return fee;
    }

    public void setFee(FloatFilter fee) {
        this.fee = fee;
    }

    public FloatFilter getFeeMax() {
        return feeMax;
    }

    public void setFeeMax(FloatFilter feeMax) {
        this.feeMax = feeMax;
    }

    public PresentTypeFilter getPresent() {
        return present;
    }

    public void setPresent(PresentTypeFilter present) {
        this.present = present;
    }

    public IntegerFilter getHits() {
        return hits;
    }

    public void setHits(IntegerFilter hits) {
        this.hits = hits;
    }

    public StatusTypeFilter getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusTypeFilter statusType) {
        this.statusType = statusType;
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

    public LongFilter getPhotosId() {
        return photosId;
    }

    public void setPhotosId(LongFilter photosId) {
        this.photosId = photosId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getDistrictId() {
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
    }

    public LongFilter getStreetId() {
        return streetId;
    }

    public void setStreetId(LongFilter streetId) {
        this.streetId = streetId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
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
        return "HouseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (avatar != null ? "avatar=" + avatar + ", " : "") +
                (actionType != null ? "actionType=" + actionType + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (money != null ? "money=" + money + ", " : "") +
                (moneyType != null ? "moneyType=" + moneyType + ", " : "") +
                (acreage != null ? "acreage=" + acreage + ", " : "") +
                (discount != null ? "discount=" + discount + ", " : "") +
                (direction != null ? "direction=" + direction + ", " : "") +
                (directionBalcony != null ? "directionBalcony=" + directionBalcony + ", " : "") +
                (floor != null ? "floor=" + floor + ", " : "") +
                (numberOfFloor != null ? "numberOfFloor=" + numberOfFloor + ", " : "") +
                (bathRoom != null ? "bathRoom=" + bathRoom + ", " : "") +
                (bedRoom != null ? "bedRoom=" + bedRoom + ", " : "") +
                (parking != null ? "parking=" + parking + ", " : "") +
                (furniture != null ? "furniture=" + furniture + ", " : "") +
                (landType != null ? "landType=" + landType + ", " : "") +
                (saleType != null ? "saleType=" + saleType + ", " : "") +
                (fee != null ? "fee=" + fee + ", " : "") +
                (feeMax != null ? "feeMax=" + feeMax + ", " : "") +
                (present != null ? "present=" + present + ", " : "") +
                (hits != null ? "hits=" + hits + ", " : "") +
                (statusType != null ? "statusType=" + statusType + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (updateAt != null ? "updateAt=" + updateAt + ", " : "") +
                (photosId != null ? "photosId=" + photosId + ", " : "") +
                (cityId != null ? "cityId=" + cityId + ", " : "") +
                (districtId != null ? "districtId=" + districtId + ", " : "") +
                (streetId != null ? "streetId=" + streetId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (createById != null ? "createById=" + createById + ", " : "") +
                (updateById != null ? "updateById=" + updateById + ", " : "") +
            "}";
    }

}

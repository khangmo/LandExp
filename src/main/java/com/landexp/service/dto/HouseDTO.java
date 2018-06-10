package com.landexp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.landexp.domain.enumeration.UserActionType;
import com.landexp.domain.enumeration.MoneyType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.DirectionType;
import com.landexp.domain.enumeration.LandType;
import com.landexp.domain.enumeration.SaleType;
import com.landexp.domain.enumeration.PresentType;
import com.landexp.domain.enumeration.StatusType;

/**
 * A DTO for the House entity.
 */
public class HouseDTO implements Serializable {

    private Long id;

    private String avatar;

    private UserActionType actionType;

    private String address;

    private Float money;

    private MoneyType moneyType;

    private Float acreage;

    private Float discount;

    private DirectionType direction;

    private DirectionType directionBalcony;

    private String floor;

    private Float numberOfFloor;

    private Integer bathRoom;

    private Integer bedRoom;

    private Boolean parking;

    private Boolean furniture;

    private LandType landType;

    private SaleType saleType;

    private Float fee;

    private Float feeMax;

    private PresentType present;

    private Integer hits;

    private StatusType statusType;

    private LocalDate createAt;

    private LocalDate updateAt;

    private Long districtId;

    private Long cityId;

    private String cityName;

    private Long streetId;

    private String streetName;

    private Long projectId;

    private String projectName;

    private Long createById;

    private String createByLogin;

    private Long updateById;

    private String updateByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserActionType getActionType() {
        return actionType;
    }

    public void setActionType(UserActionType actionType) {
        this.actionType = actionType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    public Float getAcreage() {
        return acreage;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public DirectionType getDirectionBalcony() {
        return directionBalcony;
    }

    public void setDirectionBalcony(DirectionType directionBalcony) {
        this.directionBalcony = directionBalcony;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Float getNumberOfFloor() {
        return numberOfFloor;
    }

    public void setNumberOfFloor(Float numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
    }

    public Integer getBathRoom() {
        return bathRoom;
    }

    public void setBathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
    }

    public Integer getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
    }

    public Boolean isParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean isFurniture() {
        return furniture;
    }

    public void setFurniture(Boolean furniture) {
        this.furniture = furniture;
    }

    public LandType getLandType() {
        return landType;
    }

    public void setLandType(LandType landType) {
        this.landType = landType;
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

    public Float getFeeMax() {
        return feeMax;
    }

    public void setFeeMax(Float feeMax) {
        this.feeMax = feeMax;
    }

    public PresentType getPresent() {
        return present;
    }

    public void setPresent(PresentType present) {
        this.present = present;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long landProjectId) {
        this.projectId = landProjectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String landProjectName) {
        this.projectName = landProjectName;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long userId) {
        this.createById = userId;
    }

    public String getCreateByLogin() {
        return createByLogin;
    }

    public void setCreateByLogin(String userLogin) {
        this.createByLogin = userLogin;
    }

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long userId) {
        this.updateById = userId;
    }

    public String getUpdateByLogin() {
        return updateByLogin;
    }

    public void setUpdateByLogin(String userLogin) {
        this.updateByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HouseDTO houseDTO = (HouseDTO) o;
        if (houseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseDTO{" +
            "id=" + getId() +
            ", avatar='" + getAvatar() + "'" +
            ", actionType='" + getActionType() + "'" +
            ", address='" + getAddress() + "'" +
            ", money=" + getMoney() +
            ", moneyType='" + getMoneyType() + "'" +
            ", acreage=" + getAcreage() +
            ", discount=" + getDiscount() +
            ", direction='" + getDirection() + "'" +
            ", directionBalcony='" + getDirectionBalcony() + "'" +
            ", floor='" + getFloor() + "'" +
            ", numberOfFloor=" + getNumberOfFloor() +
            ", bathRoom=" + getBathRoom() +
            ", bedRoom=" + getBedRoom() +
            ", parking='" + isParking() + "'" +
            ", furniture='" + isFurniture() + "'" +
            ", landType='" + getLandType() + "'" +
            ", saleType='" + getSaleType() + "'" +
            ", fee=" + getFee() +
            ", feeMax=" + getFeeMax() +
            ", present='" + getPresent() + "'" +
            ", hits=" + getHits() +
            ", statusType='" + getStatusType() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", district=" + getDistrictId() +
            ", city=" + getCityId() +
            ", city='" + getCityName() + "'" +
            ", street=" + getStreetId() +
            ", street='" + getStreetName() + "'" +
            ", project=" + getProjectId() +
            ", project='" + getProjectName() + "'" +
            ", createBy=" + getCreateById() +
            ", createBy='" + getCreateByLogin() + "'" +
            ", updateBy=" + getUpdateById() +
            ", updateBy='" + getUpdateByLogin() + "'" +
            "}";
    }
}

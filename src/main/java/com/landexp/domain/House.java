package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.landexp.domain.enumeration.UserActionType;

import com.landexp.domain.enumeration.MoneyType;

import com.landexp.domain.enumeration.DirectionType;

import com.landexp.domain.enumeration.LandType;

import com.landexp.domain.enumeration.SaleType;

import com.landexp.domain.enumeration.PresentType;

import com.landexp.domain.enumeration.StatusType;

/**
 * A House.
 */
@Entity
@Table(name = "house")
@Document(indexName = "house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private UserActionType actionType;

    @Column(name = "address")
    private String address;

    @Column(name = "money")
    private Float money;

    @Enumerated(EnumType.STRING)
    @Column(name = "money_type")
    private MoneyType moneyType;

    @Column(name = "acreage")
    private Float acreage;

    @Column(name = "discount")
    private Float discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction")
    private DirectionType direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction_balcony")
    private DirectionType directionBalcony;

    @Column(name = "floor")
    private String floor;

    @Column(name = "number_of_floor")
    private Float numberOfFloor;

    @Column(name = "bath_room")
    private Integer bathRoom;

    @Column(name = "bed_room")
    private Integer bedRoom;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "furniture")
    private Boolean furniture;

    @Enumerated(EnumType.STRING)
    @Column(name = "land_type")
    private LandType landType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    private SaleType saleType;

    @Column(name = "fee")
    private Float fee;

    @Column(name = "fee_max")
    private Float feeMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "present")
    private PresentType present;

    @Column(name = "hits")
    private Integer hits;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private StatusType statusType;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "house")
    private Set<HousePhoto> photos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    private City city;

    @ManyToOne
    @JsonIgnoreProperties("")
    private District district;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Street street;

    @ManyToOne
    @JsonIgnoreProperties("")
    private LandProject project;

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

    public String getAvatar() {
        return avatar;
    }

    public House avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserActionType getActionType() {
        return actionType;
    }

    public House actionType(UserActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(UserActionType actionType) {
        this.actionType = actionType;
    }

    public String getAddress() {
        return address;
    }

    public House address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getMoney() {
        return money;
    }

    public House money(Float money) {
        this.money = money;
        return this;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public MoneyType getMoneyType() {
        return moneyType;
    }

    public House moneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
        return this;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
    }

    public Float getAcreage() {
        return acreage;
    }

    public House acreage(Float acreage) {
        this.acreage = acreage;
        return this;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }

    public Float getDiscount() {
        return discount;
    }

    public House discount(Float discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public House direction(DirectionType direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public DirectionType getDirectionBalcony() {
        return directionBalcony;
    }

    public House directionBalcony(DirectionType directionBalcony) {
        this.directionBalcony = directionBalcony;
        return this;
    }

    public void setDirectionBalcony(DirectionType directionBalcony) {
        this.directionBalcony = directionBalcony;
    }

    public String getFloor() {
        return floor;
    }

    public House floor(String floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Float getNumberOfFloor() {
        return numberOfFloor;
    }

    public House numberOfFloor(Float numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
        return this;
    }

    public void setNumberOfFloor(Float numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
    }

    public Integer getBathRoom() {
        return bathRoom;
    }

    public House bathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
        return this;
    }

    public void setBathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
    }

    public Integer getBedRoom() {
        return bedRoom;
    }

    public House bedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
        return this;
    }

    public void setBedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
    }

    public Boolean isParking() {
        return parking;
    }

    public House parking(Boolean parking) {
        this.parking = parking;
        return this;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean isFurniture() {
        return furniture;
    }

    public House furniture(Boolean furniture) {
        this.furniture = furniture;
        return this;
    }

    public void setFurniture(Boolean furniture) {
        this.furniture = furniture;
    }

    public LandType getLandType() {
        return landType;
    }

    public House landType(LandType landType) {
        this.landType = landType;
        return this;
    }

    public void setLandType(LandType landType) {
        this.landType = landType;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public House saleType(SaleType saleType) {
        this.saleType = saleType;
        return this;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public Float getFee() {
        return fee;
    }

    public House fee(Float fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public Float getFeeMax() {
        return feeMax;
    }

    public House feeMax(Float feeMax) {
        this.feeMax = feeMax;
        return this;
    }

    public void setFeeMax(Float feeMax) {
        this.feeMax = feeMax;
    }

    public PresentType getPresent() {
        return present;
    }

    public House present(PresentType present) {
        this.present = present;
        return this;
    }

    public void setPresent(PresentType present) {
        this.present = present;
    }

    public Integer getHits() {
        return hits;
    }

    public House hits(Integer hits) {
        this.hits = hits;
        return this;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public House statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public House createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public House updateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Set<HousePhoto> getPhotos() {
        return photos;
    }

    public House photos(Set<HousePhoto> housePhotos) {
        this.photos = housePhotos;
        return this;
    }

    public House addPhotos(HousePhoto housePhoto) {
        this.photos.add(housePhoto);
        housePhoto.setHouse(this);
        return this;
    }

    public House removePhotos(HousePhoto housePhoto) {
        this.photos.remove(housePhoto);
        housePhoto.setHouse(null);
        return this;
    }

    public void setPhotos(Set<HousePhoto> housePhotos) {
        this.photos = housePhotos;
    }

    public City getCity() {
        return city;
    }

    public House city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public House district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Street getStreet() {
        return street;
    }

    public House street(Street street) {
        this.street = street;
        return this;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public LandProject getProject() {
        return project;
    }

    public House project(LandProject landProject) {
        this.project = landProject;
        return this;
    }

    public void setProject(LandProject landProject) {
        this.project = landProject;
    }

    public User getCreateBy() {
        return createBy;
    }

    public House createBy(User user) {
        this.createBy = user;
        return this;
    }

    public void setCreateBy(User user) {
        this.createBy = user;
    }

    public User getUpdateBy() {
        return updateBy;
    }

    public House updateBy(User user) {
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
        House house = (House) o;
        if (house.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), house.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "House{" +
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
            "}";
    }
}

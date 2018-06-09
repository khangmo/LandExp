package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.landexp.domain.enumeration.UserActivityType;

import com.landexp.domain.enumeration.DirectionType;

import com.landexp.domain.enumeration.LandType;

/**
 * A UserSubscription.
 */
@Entity
@Table(name = "user_subscription")
@Document(indexName = "usersubscription")
public class UserSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private UserActivityType actionType;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "cost_from")
    private Float costFrom;

    @Column(name = "cost_to")
    private Float costTo;

    @Column(name = "acreage_from")
    private Float acreageFrom;

    @Column(name = "acreage_to")
    private Float acreageTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction")
    private DirectionType direction;

    @Column(name = "floor")
    private String floor;

    @Column(name = "bath_room")
    private Integer bathRoom;

    @Column(name = "bed_room")
    private Integer bedRoom;

    @Column(name = "parking")
    private Boolean parking;

    @Enumerated(EnumType.STRING)
    @Column(name = "land_type")
    private LandType landType;

    @Column(name = "enabled")
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDate createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private City city;

    @ManyToOne
    @JsonIgnoreProperties("")
    private District district;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Street street;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserActivityType getActionType() {
        return actionType;
    }

    public UserSubscription actionType(UserActivityType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(UserActivityType actionType) {
        this.actionType = actionType;
    }

    public String getKeyword() {
        return keyword;
    }

    public UserSubscription keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Float getCostFrom() {
        return costFrom;
    }

    public UserSubscription costFrom(Float costFrom) {
        this.costFrom = costFrom;
        return this;
    }

    public void setCostFrom(Float costFrom) {
        this.costFrom = costFrom;
    }

    public Float getCostTo() {
        return costTo;
    }

    public UserSubscription costTo(Float costTo) {
        this.costTo = costTo;
        return this;
    }

    public void setCostTo(Float costTo) {
        this.costTo = costTo;
    }

    public Float getAcreageFrom() {
        return acreageFrom;
    }

    public UserSubscription acreageFrom(Float acreageFrom) {
        this.acreageFrom = acreageFrom;
        return this;
    }

    public void setAcreageFrom(Float acreageFrom) {
        this.acreageFrom = acreageFrom;
    }

    public Float getAcreageTo() {
        return acreageTo;
    }

    public UserSubscription acreageTo(Float acreageTo) {
        this.acreageTo = acreageTo;
        return this;
    }

    public void setAcreageTo(Float acreageTo) {
        this.acreageTo = acreageTo;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public UserSubscription direction(DirectionType direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public String getFloor() {
        return floor;
    }

    public UserSubscription floor(String floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getBathRoom() {
        return bathRoom;
    }

    public UserSubscription bathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
        return this;
    }

    public void setBathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
    }

    public Integer getBedRoom() {
        return bedRoom;
    }

    public UserSubscription bedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
        return this;
    }

    public void setBedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
    }

    public Boolean isParking() {
        return parking;
    }

    public UserSubscription parking(Boolean parking) {
        this.parking = parking;
        return this;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public LandType getLandType() {
        return landType;
    }

    public UserSubscription landType(LandType landType) {
        this.landType = landType;
        return this;
    }

    public void setLandType(LandType landType) {
        this.landType = landType;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public UserSubscription enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public UserSubscription createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public UserSubscription updateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public User getUser() {
        return user;
    }

    public UserSubscription user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public UserSubscription city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public UserSubscription district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Street getStreet() {
        return street;
    }

    public UserSubscription street(Street street) {
        this.street = street;
        return this;
    }

    public void setStreet(Street street) {
        this.street = street;
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
        UserSubscription userSubscription = (UserSubscription) o;
        if (userSubscription.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userSubscription.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserSubscription{" +
            "id=" + getId() +
            ", actionType='" + getActionType() + "'" +
            ", keyword='" + getKeyword() + "'" +
            ", costFrom=" + getCostFrom() +
            ", costTo=" + getCostTo() +
            ", acreageFrom=" + getAcreageFrom() +
            ", acreageTo=" + getAcreageTo() +
            ", direction='" + getDirection() + "'" +
            ", floor='" + getFloor() + "'" +
            ", bathRoom=" + getBathRoom() +
            ", bedRoom=" + getBedRoom() +
            ", parking='" + isParking() + "'" +
            ", landType='" + getLandType() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}

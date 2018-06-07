package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.landexp.domain.enumeration.UserActivityType;

import com.landexp.domain.enumeration.DirectionType;

import com.landexp.domain.enumeration.LandType;

/**
 * A SearchTracking.
 */
@Entity
@Table(name = "search_tracking")
@Document(indexName = "searchtracking")
public class SearchTracking implements Serializable {

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

    @Column(name = "create_at")
    private LocalDate createAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private City city;

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

    public SearchTracking actionType(UserActivityType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(UserActivityType actionType) {
        this.actionType = actionType;
    }

    public String getKeyword() {
        return keyword;
    }

    public SearchTracking keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Float getCostFrom() {
        return costFrom;
    }

    public SearchTracking costFrom(Float costFrom) {
        this.costFrom = costFrom;
        return this;
    }

    public void setCostFrom(Float costFrom) {
        this.costFrom = costFrom;
    }

    public Float getCostTo() {
        return costTo;
    }

    public SearchTracking costTo(Float costTo) {
        this.costTo = costTo;
        return this;
    }

    public void setCostTo(Float costTo) {
        this.costTo = costTo;
    }

    public Float getAcreageFrom() {
        return acreageFrom;
    }

    public SearchTracking acreageFrom(Float acreageFrom) {
        this.acreageFrom = acreageFrom;
        return this;
    }

    public void setAcreageFrom(Float acreageFrom) {
        this.acreageFrom = acreageFrom;
    }

    public Float getAcreageTo() {
        return acreageTo;
    }

    public SearchTracking acreageTo(Float acreageTo) {
        this.acreageTo = acreageTo;
        return this;
    }

    public void setAcreageTo(Float acreageTo) {
        this.acreageTo = acreageTo;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public SearchTracking direction(DirectionType direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public String getFloor() {
        return floor;
    }

    public SearchTracking floor(String floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getBathRoom() {
        return bathRoom;
    }

    public SearchTracking bathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
        return this;
    }

    public void setBathRoom(Integer bathRoom) {
        this.bathRoom = bathRoom;
    }

    public Integer getBedRoom() {
        return bedRoom;
    }

    public SearchTracking bedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
        return this;
    }

    public void setBedRoom(Integer bedRoom) {
        this.bedRoom = bedRoom;
    }

    public Boolean isParking() {
        return parking;
    }

    public SearchTracking parking(Boolean parking) {
        this.parking = parking;
        return this;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public LandType getLandType() {
        return landType;
    }

    public SearchTracking landType(LandType landType) {
        this.landType = landType;
        return this;
    }

    public void setLandType(LandType landType) {
        this.landType = landType;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public SearchTracking createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public User getUser() {
        return user;
    }

    public SearchTracking user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public SearchTracking city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Street getStreet() {
        return street;
    }

    public SearchTracking street(Street street) {
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
        SearchTracking searchTracking = (SearchTracking) o;
        if (searchTracking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), searchTracking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SearchTracking{" +
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
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}

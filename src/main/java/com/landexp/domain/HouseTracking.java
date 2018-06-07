package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.landexp.domain.enumeration.UserActivityType;

/**
 * A HouseTracking.
 */
@Entity
@Table(name = "house_tracking")
@Document(indexName = "housetracking")
public class HouseTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private UserActivityType activityType;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "source_link")
    private String sourceLink;

    @Column(name = "description")
    private String description;

    @Column(name = "create_at")
    private LocalDate createAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private House house;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserActivityType getActivityType() {
        return activityType;
    }

    public HouseTracking activityType(UserActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public void setActivityType(UserActivityType activityType) {
        this.activityType = activityType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public HouseTracking sourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public HouseTracking sourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
        return this;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getDescription() {
        return description;
    }

    public HouseTracking description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public HouseTracking createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public House getHouse() {
        return house;
    }

    public HouseTracking house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getUser() {
        return user;
    }

    public HouseTracking user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        HouseTracking houseTracking = (HouseTracking) o;
        if (houseTracking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseTracking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseTracking{" +
            "id=" + getId() +
            ", activityType='" + getActivityType() + "'" +
            ", sourceId='" + getSourceId() + "'" +
            ", sourceLink='" + getSourceLink() + "'" +
            ", description='" + getDescription() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}

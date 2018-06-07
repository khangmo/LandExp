package com.landexp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.landexp.domain.enumeration.UserActivityType;

/**
 * A DTO for the UserTracking entity.
 */
public class UserTrackingDTO implements Serializable {

    private Long id;

    private UserActivityType activityType;

    private String sourceId;

    private String sourceLink;

    private String description;

    private LocalDate createAt;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(UserActivityType activityType) {
        this.activityType = activityType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserTrackingDTO userTrackingDTO = (UserTrackingDTO) o;
        if (userTrackingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userTrackingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserTrackingDTO{" +
            "id=" + getId() +
            ", activityType='" + getActivityType() + "'" +
            ", sourceId='" + getSourceId() + "'" +
            ", sourceLink='" + getSourceLink() + "'" +
            ", description='" + getDescription() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

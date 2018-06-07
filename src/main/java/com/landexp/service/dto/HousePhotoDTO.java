package com.landexp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the HousePhoto entity.
 */
public class HousePhotoDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HousePhotoDTO housePhotoDTO = (HousePhotoDTO) o;
        if (housePhotoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), housePhotoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HousePhotoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}

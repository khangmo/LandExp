package com.landexp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Banner entity.
 */
public class BannerDTO implements Serializable {

    private Long id;

    private String title;

    private String titleAlias;

    private Integer area;

    private Integer hits;

    private LocalDate publicUp;

    private LocalDate publicDown;

    private LocalDate createAt;

    private LocalDate updateAt;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAlias() {
        return titleAlias;
    }

    public void setTitleAlias(String titleAlias) {
        this.titleAlias = titleAlias;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public LocalDate getPublicUp() {
        return publicUp;
    }

    public void setPublicUp(LocalDate publicUp) {
        this.publicUp = publicUp;
    }

    public LocalDate getPublicDown() {
        return publicDown;
    }

    public void setPublicDown(LocalDate publicDown) {
        this.publicDown = publicDown;
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

        BannerDTO bannerDTO = (BannerDTO) o;
        if (bannerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bannerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BannerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", titleAlias='" + getTitleAlias() + "'" +
            ", area=" + getArea() +
            ", hits=" + getHits() +
            ", publicUp='" + getPublicUp() + "'" +
            ", publicDown='" + getPublicDown() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", createBy=" + getCreateById() +
            ", createBy='" + getCreateByLogin() + "'" +
            ", updateBy=" + getUpdateById() +
            ", updateBy='" + getUpdateByLogin() + "'" +
            "}";
    }
}

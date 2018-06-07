package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Banner.
 */
@Entity
@Table(name = "banner")
@Document(indexName = "banner")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "title_alias")
    private String titleAlias;

    @Column(name = "area")
    private Integer area;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "public_up")
    private LocalDate publicUp;

    @Column(name = "public_down")
    private LocalDate publicDown;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDate createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDate updateAt;

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

    public String getTitle() {
        return title;
    }

    public Banner title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAlias() {
        return titleAlias;
    }

    public Banner titleAlias(String titleAlias) {
        this.titleAlias = titleAlias;
        return this;
    }

    public void setTitleAlias(String titleAlias) {
        this.titleAlias = titleAlias;
    }

    public Integer getArea() {
        return area;
    }

    public Banner area(Integer area) {
        this.area = area;
        return this;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getHits() {
        return hits;
    }

    public Banner hits(Integer hits) {
        this.hits = hits;
        return this;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public LocalDate getPublicUp() {
        return publicUp;
    }

    public Banner publicUp(LocalDate publicUp) {
        this.publicUp = publicUp;
        return this;
    }

    public void setPublicUp(LocalDate publicUp) {
        this.publicUp = publicUp;
    }

    public LocalDate getPublicDown() {
        return publicDown;
    }

    public Banner publicDown(LocalDate publicDown) {
        this.publicDown = publicDown;
        return this;
    }

    public void setPublicDown(LocalDate publicDown) {
        this.publicDown = publicDown;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Banner createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public Banner updateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public User getCreateBy() {
        return createBy;
    }

    public Banner createBy(User user) {
        this.createBy = user;
        return this;
    }

    public void setCreateBy(User user) {
        this.createBy = user;
    }

    public User getUpdateBy() {
        return updateBy;
    }

    public Banner updateBy(User user) {
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
        Banner banner = (Banner) o;
        if (banner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), banner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Banner{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", titleAlias='" + getTitleAlias() + "'" +
            ", area=" + getArea() +
            ", hits=" + getHits() +
            ", publicUp='" + getPublicUp() + "'" +
            ", publicDown='" + getPublicDown() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}

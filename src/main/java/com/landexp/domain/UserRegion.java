package com.landexp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserRegion.
 */
@Entity
@Table(name = "user_region")
@Document(indexName = "userregion")
public class UserRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToMany
    @JoinTable(name = "user_region_region",
               joinColumns = @JoinColumn(name = "user_regions_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "regions_id", referencedColumnName = "id"))
    private Set<Region> regions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public UserRegion createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public UserRegion updateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public User getUser() {
        return user;
    }

    public UserRegion user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public UserRegion regions(Set<Region> regions) {
        this.regions = regions;
        return this;
    }

    public UserRegion addRegion(Region region) {
        this.regions.add(region);
        return this;
    }

    public UserRegion removeRegion(Region region) {
        this.regions.remove(region);
        return this;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
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
        UserRegion userRegion = (UserRegion) o;
        if (userRegion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userRegion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRegion{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}

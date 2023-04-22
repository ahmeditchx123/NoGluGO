package com.noglugo.mvp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "img_path", nullable = false)
    private String imgPath;

    @NotNull
    @Column(name = "is_dedicated_gluten_free", nullable = false)
    private Boolean isDedicatedGlutenFree;

    @Column(name = "website")
    private String website;

    @NotNull
    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "orderSA", "orderBA", "store", "restaurant", "location" }, allowSetters = true)
    @OneToOne(mappedBy = "restaurant")
    private Address restaurantAddress;

    @JsonIgnoreProperties(value = { "restaurant", "menuItems" }, allowSetters = true)
    @OneToOne(mappedBy = "restaurant")
    private Menu restaurantMenu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Restaurant id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Restaurant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Restaurant description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Restaurant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public Restaurant imgPath(String imgPath) {
        this.setImgPath(imgPath);
        return this;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Boolean getIsDedicatedGlutenFree() {
        return this.isDedicatedGlutenFree;
    }

    public Restaurant isDedicatedGlutenFree(Boolean isDedicatedGlutenFree) {
        this.setIsDedicatedGlutenFree(isDedicatedGlutenFree);
        return this;
    }

    public void setIsDedicatedGlutenFree(Boolean isDedicatedGlutenFree) {
        this.isDedicatedGlutenFree = isDedicatedGlutenFree;
    }

    public String getWebsite() {
        return this.website;
    }

    public Restaurant website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getTableNumber() {
        return this.tableNumber;
    }

    public Restaurant tableNumber(Integer tableNumber) {
        this.setTableNumber(tableNumber);
        return this;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Restaurant createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Restaurant createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Restaurant lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Restaurant lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Address getRestaurantAddress() {
        return this.restaurantAddress;
    }

    public void setRestaurantAddress(Address address) {
        if (this.restaurantAddress != null) {
            this.restaurantAddress.setRestaurant(null);
        }
        if (address != null) {
            address.setRestaurant(this);
        }
        this.restaurantAddress = address;
    }

    public Restaurant restaurantAddress(Address address) {
        this.setRestaurantAddress(address);
        return this;
    }

    public Menu getRestaurantMenu() {
        return this.restaurantMenu;
    }

    public void setRestaurantMenu(Menu menu) {
        if (this.restaurantMenu != null) {
            this.restaurantMenu.setRestaurant(null);
        }
        if (menu != null) {
            menu.setRestaurant(this);
        }
        this.restaurantMenu = menu;
    }

    public Restaurant restaurantMenu(Menu menu) {
        this.setRestaurantMenu(menu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", imgPath='" + getImgPath() + "'" +
            ", isDedicatedGlutenFree='" + getIsDedicatedGlutenFree() + "'" +
            ", website='" + getWebsite() + "'" +
            ", tableNumber=" + getTableNumber() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

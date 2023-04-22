package com.noglugo.mvp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Store.
 */
@Entity
@Table(name = "store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Store implements Serializable {

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
    @Column(name = "has_delivery_mode", nullable = false)
    private Boolean hasDeliveryMode;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "orderSA", "orderBA", "store", "restaurant", "location" }, allowSetters = true)
    @OneToOne(mappedBy = "store")
    private Address storeAddress;

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cartItem", "orderItem", "store", "information", "reviews" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @Column(name = "user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Store id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Store name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Store description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Store telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public Store imgPath(String imgPath) {
        this.setImgPath(imgPath);
        return this;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Boolean getIsDedicatedGlutenFree() {
        return this.isDedicatedGlutenFree;
    }

    public Store isDedicatedGlutenFree(Boolean isDedicatedGlutenFree) {
        this.setIsDedicatedGlutenFree(isDedicatedGlutenFree);
        return this;
    }

    public void setIsDedicatedGlutenFree(Boolean isDedicatedGlutenFree) {
        this.isDedicatedGlutenFree = isDedicatedGlutenFree;
    }

    public String getWebsite() {
        return this.website;
    }

    public Store website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getHasDeliveryMode() {
        return this.hasDeliveryMode;
    }

    public Store hasDeliveryMode(Boolean hasDeliveryMode) {
        this.setHasDeliveryMode(hasDeliveryMode);
        return this;
    }

    public void setHasDeliveryMode(Boolean hasDeliveryMode) {
        this.hasDeliveryMode = hasDeliveryMode;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Store createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Store createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Store lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Store lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Address getStoreAddress() {
        return this.storeAddress;
    }

    public void setStoreAddress(Address address) {
        if (this.storeAddress != null) {
            this.storeAddress.setStore(null);
        }
        if (address != null) {
            address.setStore(this);
        }
        this.storeAddress = address;
    }

    public Store storeAddress(Address address) {
        this.setStoreAddress(address);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setStore(null));
        }
        if (products != null) {
            products.forEach(i -> i.setStore(this));
        }
        this.products = products;
    }

    public Store products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Store addProducts(Product product) {
        this.products.add(product);
        product.setStore(this);
        return this;
    }

    public Store removeProducts(Product product) {
        this.products.remove(product);
        product.setStore(null);
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Store)) {
            return false;
        }
        return id != null && id.equals(((Store) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", imgPath='" + getImgPath() + "'" +
            ", isDedicatedGlutenFree='" + getIsDedicatedGlutenFree() + "'" +
            ", website='" + getWebsite() + "'" +
            ", hasDeliveryMode='" + getHasDeliveryMode() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

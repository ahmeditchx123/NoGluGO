package com.noglugo.mvp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noglugo.mvp.domain.enumeration.Governorate;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "street_1", nullable = false)
    private String street1;

    @Column(name = "street_2")
    private String street2;

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private Governorate city;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "shippingAddress", "billingAddress", "orderItems" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Order orderSA;

    @JsonIgnoreProperties(value = { "shippingAddress", "billingAddress", "orderItems" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Order orderBA;

    @JsonIgnoreProperties(value = { "storeAddress", "products" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Store store;

    @JsonIgnoreProperties(value = { "restaurantAddress", "restaurantMenu" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Restaurant restaurant;

    @JsonIgnoreProperties(value = { "address" }, allowSetters = true)
    @OneToOne(mappedBy = "address")
    private Location location;

    @Column(name = "user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Address id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet1() {
        return this.street1;
    }

    public Address street1(String street1) {
        this.setStreet1(street1);
        return this;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public Address street2(String street2) {
        this.setStreet2(street2);
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public Governorate getCity() {
        return this.city;
    }

    public Address city(Governorate city) {
        this.setCity(city);
        return this;
    }

    public void setCity(Governorate city) {
        this.city = city;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Address createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Address createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Address lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Address lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Order getOrderSA() {
        return this.orderSA;
    }

    public void setOrderSA(Order order) {
        this.orderSA = order;
    }

    public Address orderSA(Order order) {
        this.setOrderSA(order);
        return this;
    }

    public Order getOrderBA() {
        return this.orderBA;
    }

    public void setOrderBA(Order order) {
        this.orderBA = order;
    }

    public Address orderBA(Order order) {
        this.setOrderBA(order);
        return this;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Address store(Store store) {
        this.setStore(store);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Address restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.setAddress(null);
        }
        if (location != null) {
            location.setAddress(this);
        }
        this.location = location;
    }

    public Address location(Location location) {
        this.setLocation(location);
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
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", city='" + getCity() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

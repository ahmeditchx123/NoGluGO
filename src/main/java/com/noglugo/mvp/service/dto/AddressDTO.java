package com.noglugo.mvp.service.dto;

import com.noglugo.mvp.domain.enumeration.Governorate;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.noglugo.mvp.domain.Address} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AddressDTO implements Serializable {

    private UUID id;

    @NotNull
    private String street1;

    private String street2;

    private Governorate city;

    @NotNull
    private String postalCode;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private OrderDTO orderSA;

    private OrderDTO orderBA;

    private StoreDTO store;

    private LocationDTO locationDTO;

    private RestaurantDTO restaurant;

    private Long userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public Governorate getCity() {
        return city;
    }

    public void setCity(Governorate city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OrderDTO getOrderSA() {
        return orderSA;
    }

    public void setOrderSA(OrderDTO orderSA) {
        this.orderSA = orderSA;
    }

    public OrderDTO getOrderBA() {
        return orderBA;
    }

    public void setOrderBA(OrderDTO orderBA) {
        this.orderBA = orderBA;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "id='" + getId() + "'" +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", city='" + getCity() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", orderSA=" + getOrderSA() +
            ", orderBA=" + getOrderBA() +
            ", store=" + getStore() +
            ", restaurant=" + getRestaurant() +
            "}";
    }
}

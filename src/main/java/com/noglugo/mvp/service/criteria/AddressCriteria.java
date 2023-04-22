package com.noglugo.mvp.service.criteria;

import com.noglugo.mvp.domain.enumeration.Governorate;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.Address} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AddressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Governorate
     */
    public static class GovernorateFilter extends Filter<Governorate> {

        public GovernorateFilter() {}

        public GovernorateFilter(GovernorateFilter filter) {
            super(filter);
        }

        @Override
        public GovernorateFilter copy() {
            return new GovernorateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter street1;

    private StringFilter street2;

    private GovernorateFilter city;

    private StringFilter postalCode;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter orderSAId;

    private UUIDFilter orderBAId;

    private UUIDFilter storeId;

    private UUIDFilter restaurantId;

    private UUIDFilter locationId;

    private Boolean distinct;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.street1 = other.street1 == null ? null : other.street1.copy();
        this.street2 = other.street2 == null ? null : other.street2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.orderSAId = other.orderSAId == null ? null : other.orderSAId.copy();
        this.orderBAId = other.orderBAId == null ? null : other.orderBAId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.restaurantId = other.restaurantId == null ? null : other.restaurantId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getStreet1() {
        return street1;
    }

    public StringFilter street1() {
        if (street1 == null) {
            street1 = new StringFilter();
        }
        return street1;
    }

    public void setStreet1(StringFilter street1) {
        this.street1 = street1;
    }

    public StringFilter getStreet2() {
        return street2;
    }

    public StringFilter street2() {
        if (street2 == null) {
            street2 = new StringFilter();
        }
        return street2;
    }

    public void setStreet2(StringFilter street2) {
        this.street2 = street2;
    }

    public GovernorateFilter getCity() {
        return city;
    }

    public GovernorateFilter city() {
        if (city == null) {
            city = new GovernorateFilter();
        }
        return city;
    }

    public void setCity(GovernorateFilter city) {
        this.city = city;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public UUIDFilter getOrderSAId() {
        return orderSAId;
    }

    public UUIDFilter orderSAId() {
        if (orderSAId == null) {
            orderSAId = new UUIDFilter();
        }
        return orderSAId;
    }

    public void setOrderSAId(UUIDFilter orderSAId) {
        this.orderSAId = orderSAId;
    }

    public UUIDFilter getOrderBAId() {
        return orderBAId;
    }

    public UUIDFilter orderBAId() {
        if (orderBAId == null) {
            orderBAId = new UUIDFilter();
        }
        return orderBAId;
    }

    public void setOrderBAId(UUIDFilter orderBAId) {
        this.orderBAId = orderBAId;
    }

    public UUIDFilter getStoreId() {
        return storeId;
    }

    public UUIDFilter storeId() {
        if (storeId == null) {
            storeId = new UUIDFilter();
        }
        return storeId;
    }

    public void setStoreId(UUIDFilter storeId) {
        this.storeId = storeId;
    }

    public UUIDFilter getRestaurantId() {
        return restaurantId;
    }

    public UUIDFilter restaurantId() {
        if (restaurantId == null) {
            restaurantId = new UUIDFilter();
        }
        return restaurantId;
    }

    public void setRestaurantId(UUIDFilter restaurantId) {
        this.restaurantId = restaurantId;
    }

    public UUIDFilter getLocationId() {
        return locationId;
    }

    public UUIDFilter locationId() {
        if (locationId == null) {
            locationId = new UUIDFilter();
        }
        return locationId;
    }

    public void setLocationId(UUIDFilter locationId) {
        this.locationId = locationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(street1, that.street1) &&
            Objects.equals(street2, that.street2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(orderSAId, that.orderSAId) &&
            Objects.equals(orderBAId, that.orderBAId) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            street1,
            street2,
            city,
            postalCode,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            orderSAId,
            orderBAId,
            storeId,
            restaurantId,
            locationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (street1 != null ? "street1=" + street1 + ", " : "") +
            (street2 != null ? "street2=" + street2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (orderSAId != null ? "orderSAId=" + orderSAId + ", " : "") +
            (orderBAId != null ? "orderBAId=" + orderBAId + ", " : "") +
            (storeId != null ? "storeId=" + storeId + ", " : "") +
            (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

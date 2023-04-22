package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.Cart} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.CartResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private IntegerFilter totalItems;

    private DoubleFilter totalPrice;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private UUIDFilter cartItemsId;

    private Boolean distinct;

    public CartCriteria() {}

    public CartCriteria(CartCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.totalItems = other.totalItems == null ? null : other.totalItems.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.cartItemsId = other.cartItemsId == null ? null : other.cartItemsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CartCriteria copy() {
        return new CartCriteria(this);
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

    public IntegerFilter getTotalItems() {
        return totalItems;
    }

    public IntegerFilter totalItems() {
        if (totalItems == null) {
            totalItems = new IntegerFilter();
        }
        return totalItems;
    }

    public void setTotalItems(IntegerFilter totalItems) {
        this.totalItems = totalItems;
    }

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public UUIDFilter getCartItemsId() {
        return cartItemsId;
    }

    public UUIDFilter cartItemsId() {
        if (cartItemsId == null) {
            cartItemsId = new UUIDFilter();
        }
        return cartItemsId;
    }

    public void setCartItemsId(UUIDFilter cartItemsId) {
        this.cartItemsId = cartItemsId;
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
        final CartCriteria that = (CartCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(totalItems, that.totalItems) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(cartItemsId, that.cartItemsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            totalItems,
            totalPrice,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            cartItemsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (totalItems != null ? "totalItems=" + totalItems + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (cartItemsId != null ? "cartItemsId=" + cartItemsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

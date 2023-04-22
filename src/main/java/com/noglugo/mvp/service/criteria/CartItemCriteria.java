package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.CartItem} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.CartItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cart-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private IntegerFilter qty;

    private DoubleFilter totalPrice;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter cartId;

    private UUIDFilter productsId;

    private Boolean distinct;

    public CartItemCriteria() {}

    public CartItemCriteria(CartItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.cartId = other.cartId == null ? null : other.cartId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CartItemCriteria copy() {
        return new CartItemCriteria(this);
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

    public IntegerFilter getQty() {
        return qty;
    }

    public IntegerFilter qty() {
        if (qty == null) {
            qty = new IntegerFilter();
        }
        return qty;
    }

    public void setQty(IntegerFilter qty) {
        this.qty = qty;
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

    public UUIDFilter getCartId() {
        return cartId;
    }

    public UUIDFilter cartId() {
        if (cartId == null) {
            cartId = new UUIDFilter();
        }
        return cartId;
    }

    public void setCartId(UUIDFilter cartId) {
        this.cartId = cartId;
    }

    public UUIDFilter getProductsId() {
        return productsId;
    }

    public UUIDFilter productsId() {
        if (productsId == null) {
            productsId = new UUIDFilter();
        }
        return productsId;
    }

    public void setProductsId(UUIDFilter productsId) {
        this.productsId = productsId;
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
        final CartItemCriteria that = (CartItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(cartId, that.cartId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qty, totalPrice, createdBy, createdDate, lastModifiedBy, lastModifiedDate, cartId, productsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (cartId != null ? "cartId=" + cartId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

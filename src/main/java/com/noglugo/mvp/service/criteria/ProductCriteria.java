package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.Product} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter sku;

    private StringFilter description;

    private StringFilter imgPath;

    private DoubleFilter unitPrice;

    private BooleanFilter isAvailable;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter cartItemId;

    private UUIDFilter orderItemId;

    private UUIDFilter storeId;

    private UUIDFilter informationId;

    private UUIDFilter reviewsId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.imgPath = other.imgPath == null ? null : other.imgPath.copy();
        this.unitPrice = other.unitPrice == null ? null : other.unitPrice.copy();
        this.isAvailable = other.isAvailable == null ? null : other.isAvailable.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.cartItemId = other.cartItemId == null ? null : other.cartItemId.copy();
        this.orderItemId = other.orderItemId == null ? null : other.orderItemId.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
        this.informationId = other.informationId == null ? null : other.informationId.copy();
        this.reviewsId = other.reviewsId == null ? null : other.reviewsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSku() {
        return sku;
    }

    public StringFilter sku() {
        if (sku == null) {
            sku = new StringFilter();
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getImgPath() {
        return imgPath;
    }

    public StringFilter imgPath() {
        if (imgPath == null) {
            imgPath = new StringFilter();
        }
        return imgPath;
    }

    public void setImgPath(StringFilter imgPath) {
        this.imgPath = imgPath;
    }

    public DoubleFilter getUnitPrice() {
        return unitPrice;
    }

    public DoubleFilter unitPrice() {
        if (unitPrice == null) {
            unitPrice = new DoubleFilter();
        }
        return unitPrice;
    }

    public void setUnitPrice(DoubleFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BooleanFilter getIsAvailable() {
        return isAvailable;
    }

    public BooleanFilter isAvailable() {
        if (isAvailable == null) {
            isAvailable = new BooleanFilter();
        }
        return isAvailable;
    }

    public void setIsAvailable(BooleanFilter isAvailable) {
        this.isAvailable = isAvailable;
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

    public UUIDFilter getCartItemId() {
        return cartItemId;
    }

    public UUIDFilter cartItemId() {
        if (cartItemId == null) {
            cartItemId = new UUIDFilter();
        }
        return cartItemId;
    }

    public void setCartItemId(UUIDFilter cartItemId) {
        this.cartItemId = cartItemId;
    }

    public UUIDFilter getOrderItemId() {
        return orderItemId;
    }

    public UUIDFilter orderItemId() {
        if (orderItemId == null) {
            orderItemId = new UUIDFilter();
        }
        return orderItemId;
    }

    public void setOrderItemId(UUIDFilter orderItemId) {
        this.orderItemId = orderItemId;
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

    public UUIDFilter getInformationId() {
        return informationId;
    }

    public UUIDFilter informationId() {
        if (informationId == null) {
            informationId = new UUIDFilter();
        }
        return informationId;
    }

    public void setInformationId(UUIDFilter informationId) {
        this.informationId = informationId;
    }

    public UUIDFilter getReviewsId() {
        return reviewsId;
    }

    public UUIDFilter reviewsId() {
        if (reviewsId == null) {
            reviewsId = new UUIDFilter();
        }
        return reviewsId;
    }

    public void setReviewsId(UUIDFilter reviewsId) {
        this.reviewsId = reviewsId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(description, that.description) &&
            Objects.equals(imgPath, that.imgPath) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(isAvailable, that.isAvailable) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(cartItemId, that.cartItemId) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(storeId, that.storeId) &&
            Objects.equals(informationId, that.informationId) &&
            Objects.equals(reviewsId, that.reviewsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            sku,
            description,
            imgPath,
            unitPrice,
            isAvailable,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            cartItemId,
            orderItemId,
            storeId,
            informationId,
            reviewsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (imgPath != null ? "imgPath=" + imgPath + ", " : "") +
            (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
            (isAvailable != null ? "isAvailable=" + isAvailable + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (cartItemId != null ? "cartItemId=" + cartItemId + ", " : "") +
            (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
            (storeId != null ? "storeId=" + storeId + ", " : "") +
            (informationId != null ? "informationId=" + informationId + ", " : "") +
            (reviewsId != null ? "reviewsId=" + reviewsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

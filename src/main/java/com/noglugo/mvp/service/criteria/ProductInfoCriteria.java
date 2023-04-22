package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.ProductInfo} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.ProductInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private IntegerFilter qtyInStock;

    private BooleanFilter isGlutenFree;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter productId;

    private Boolean distinct;

    public ProductInfoCriteria() {}

    public ProductInfoCriteria(ProductInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qtyInStock = other.qtyInStock == null ? null : other.qtyInStock.copy();
        this.isGlutenFree = other.isGlutenFree == null ? null : other.isGlutenFree.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductInfoCriteria copy() {
        return new ProductInfoCriteria(this);
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

    public IntegerFilter getQtyInStock() {
        return qtyInStock;
    }

    public IntegerFilter qtyInStock() {
        if (qtyInStock == null) {
            qtyInStock = new IntegerFilter();
        }
        return qtyInStock;
    }

    public void setQtyInStock(IntegerFilter qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public BooleanFilter getIsGlutenFree() {
        return isGlutenFree;
    }

    public BooleanFilter isGlutenFree() {
        if (isGlutenFree == null) {
            isGlutenFree = new BooleanFilter();
        }
        return isGlutenFree;
    }

    public void setIsGlutenFree(BooleanFilter isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
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

    public UUIDFilter getProductId() {
        return productId;
    }

    public UUIDFilter productId() {
        if (productId == null) {
            productId = new UUIDFilter();
        }
        return productId;
    }

    public void setProductId(UUIDFilter productId) {
        this.productId = productId;
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
        final ProductInfoCriteria that = (ProductInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(qtyInStock, that.qtyInStock) &&
            Objects.equals(isGlutenFree, that.isGlutenFree) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qtyInStock, isGlutenFree, createdBy, createdDate, lastModifiedBy, lastModifiedDate, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInfoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qtyInStock != null ? "qtyInStock=" + qtyInStock + ", " : "") +
            (isGlutenFree != null ? "isGlutenFree=" + isGlutenFree + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

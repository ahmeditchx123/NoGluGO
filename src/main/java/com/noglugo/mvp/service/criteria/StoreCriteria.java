package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.Store} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.StoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter telephone;

    private StringFilter imgPath;

    private BooleanFilter isDedicatedGlutenFree;

    private StringFilter website;

    private BooleanFilter hasDeliveryMode;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter storeAddressId;

    private UUIDFilter productsId;

    private Boolean distinct;

    public StoreCriteria() {}

    public StoreCriteria(StoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.imgPath = other.imgPath == null ? null : other.imgPath.copy();
        this.isDedicatedGlutenFree = other.isDedicatedGlutenFree == null ? null : other.isDedicatedGlutenFree.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.hasDeliveryMode = other.hasDeliveryMode == null ? null : other.hasDeliveryMode.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.storeAddressId = other.storeAddressId == null ? null : other.storeAddressId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StoreCriteria copy() {
        return new StoreCriteria(this);
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

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
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

    public BooleanFilter getIsDedicatedGlutenFree() {
        return isDedicatedGlutenFree;
    }

    public BooleanFilter isDedicatedGlutenFree() {
        if (isDedicatedGlutenFree == null) {
            isDedicatedGlutenFree = new BooleanFilter();
        }
        return isDedicatedGlutenFree;
    }

    public void setIsDedicatedGlutenFree(BooleanFilter isDedicatedGlutenFree) {
        this.isDedicatedGlutenFree = isDedicatedGlutenFree;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public BooleanFilter getHasDeliveryMode() {
        return hasDeliveryMode;
    }

    public BooleanFilter hasDeliveryMode() {
        if (hasDeliveryMode == null) {
            hasDeliveryMode = new BooleanFilter();
        }
        return hasDeliveryMode;
    }

    public void setHasDeliveryMode(BooleanFilter hasDeliveryMode) {
        this.hasDeliveryMode = hasDeliveryMode;
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

    public UUIDFilter getStoreAddressId() {
        return storeAddressId;
    }

    public UUIDFilter storeAddressId() {
        if (storeAddressId == null) {
            storeAddressId = new UUIDFilter();
        }
        return storeAddressId;
    }

    public void setStoreAddressId(UUIDFilter storeAddressId) {
        this.storeAddressId = storeAddressId;
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
        final StoreCriteria that = (StoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(imgPath, that.imgPath) &&
            Objects.equals(isDedicatedGlutenFree, that.isDedicatedGlutenFree) &&
            Objects.equals(website, that.website) &&
            Objects.equals(hasDeliveryMode, that.hasDeliveryMode) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(storeAddressId, that.storeAddressId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            telephone,
            imgPath,
            isDedicatedGlutenFree,
            website,
            hasDeliveryMode,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            storeAddressId,
            productsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (imgPath != null ? "imgPath=" + imgPath + ", " : "") +
            (isDedicatedGlutenFree != null ? "isDedicatedGlutenFree=" + isDedicatedGlutenFree + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (hasDeliveryMode != null ? "hasDeliveryMode=" + hasDeliveryMode + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (storeAddressId != null ? "storeAddressId=" + storeAddressId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

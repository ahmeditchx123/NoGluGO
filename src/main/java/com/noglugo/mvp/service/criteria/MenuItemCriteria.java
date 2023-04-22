package com.noglugo.mvp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.MenuItem} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.MenuItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menu-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter content;

    private StringFilter imgPath;

    private DoubleFilter unitPrice;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private UUIDFilter menuId;

    private UUIDFilter reviewsId;

    private Boolean distinct;

    public MenuItemCriteria() {}

    public MenuItemCriteria(MenuItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.imgPath = other.imgPath == null ? null : other.imgPath.copy();
        this.unitPrice = other.unitPrice == null ? null : other.unitPrice.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.menuId = other.menuId == null ? null : other.menuId.copy();
        this.reviewsId = other.reviewsId == null ? null : other.reviewsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MenuItemCriteria copy() {
        return new MenuItemCriteria(this);
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

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
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

    public UUIDFilter getMenuId() {
        return menuId;
    }

    public UUIDFilter menuId() {
        if (menuId == null) {
            menuId = new UUIDFilter();
        }
        return menuId;
    }

    public void setMenuId(UUIDFilter menuId) {
        this.menuId = menuId;
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
        final MenuItemCriteria that = (MenuItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(content, that.content) &&
            Objects.equals(imgPath, that.imgPath) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(menuId, that.menuId) &&
            Objects.equals(reviewsId, that.reviewsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            content,
            imgPath,
            unitPrice,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            menuId,
            reviewsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (imgPath != null ? "imgPath=" + imgPath + ", " : "") +
            (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (menuId != null ? "menuId=" + menuId + ", " : "") +
            (reviewsId != null ? "reviewsId=" + reviewsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.noglugo.mvp.service.criteria;

import com.noglugo.mvp.domain.enumeration.Diseas;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.GlutenProfile} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.GlutenProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gluten-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GlutenProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Diseas
     */
    public static class DiseasFilter extends Filter<Diseas> {

        public DiseasFilter() {}

        public DiseasFilter(DiseasFilter filter) {
            super(filter);
        }

        @Override
        public DiseasFilter copy() {
            return new DiseasFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private DiseasFilter diseas;

    private StringFilter otherDiseas;

    private IntegerFilter strictnessLevel;

    private IntegerFilter diaryFreePreferenceLvl;

    private IntegerFilter veganPreferenceLvl;

    private IntegerFilter ketoPreferenceLvl;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private Boolean distinct;

    public GlutenProfileCriteria() {}

    public GlutenProfileCriteria(GlutenProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.diseas = other.diseas == null ? null : other.diseas.copy();
        this.otherDiseas = other.otherDiseas == null ? null : other.otherDiseas.copy();
        this.strictnessLevel = other.strictnessLevel == null ? null : other.strictnessLevel.copy();
        this.diaryFreePreferenceLvl = other.diaryFreePreferenceLvl == null ? null : other.diaryFreePreferenceLvl.copy();
        this.veganPreferenceLvl = other.veganPreferenceLvl == null ? null : other.veganPreferenceLvl.copy();
        this.ketoPreferenceLvl = other.ketoPreferenceLvl == null ? null : other.ketoPreferenceLvl.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GlutenProfileCriteria copy() {
        return new GlutenProfileCriteria(this);
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

    public DiseasFilter getDiseas() {
        return diseas;
    }

    public DiseasFilter diseas() {
        if (diseas == null) {
            diseas = new DiseasFilter();
        }
        return diseas;
    }

    public void setDiseas(DiseasFilter diseas) {
        this.diseas = diseas;
    }

    public StringFilter getOtherDiseas() {
        return otherDiseas;
    }

    public StringFilter otherDiseas() {
        if (otherDiseas == null) {
            otherDiseas = new StringFilter();
        }
        return otherDiseas;
    }

    public void setOtherDiseas(StringFilter otherDiseas) {
        this.otherDiseas = otherDiseas;
    }

    public IntegerFilter getStrictnessLevel() {
        return strictnessLevel;
    }

    public IntegerFilter strictnessLevel() {
        if (strictnessLevel == null) {
            strictnessLevel = new IntegerFilter();
        }
        return strictnessLevel;
    }

    public void setStrictnessLevel(IntegerFilter strictnessLevel) {
        this.strictnessLevel = strictnessLevel;
    }

    public IntegerFilter getDiaryFreePreferenceLvl() {
        return diaryFreePreferenceLvl;
    }

    public IntegerFilter diaryFreePreferenceLvl() {
        if (diaryFreePreferenceLvl == null) {
            diaryFreePreferenceLvl = new IntegerFilter();
        }
        return diaryFreePreferenceLvl;
    }

    public void setDiaryFreePreferenceLvl(IntegerFilter diaryFreePreferenceLvl) {
        this.diaryFreePreferenceLvl = diaryFreePreferenceLvl;
    }

    public IntegerFilter getVeganPreferenceLvl() {
        return veganPreferenceLvl;
    }

    public IntegerFilter veganPreferenceLvl() {
        if (veganPreferenceLvl == null) {
            veganPreferenceLvl = new IntegerFilter();
        }
        return veganPreferenceLvl;
    }

    public void setVeganPreferenceLvl(IntegerFilter veganPreferenceLvl) {
        this.veganPreferenceLvl = veganPreferenceLvl;
    }

    public IntegerFilter getKetoPreferenceLvl() {
        return ketoPreferenceLvl;
    }

    public IntegerFilter ketoPreferenceLvl() {
        if (ketoPreferenceLvl == null) {
            ketoPreferenceLvl = new IntegerFilter();
        }
        return ketoPreferenceLvl;
    }

    public void setKetoPreferenceLvl(IntegerFilter ketoPreferenceLvl) {
        this.ketoPreferenceLvl = ketoPreferenceLvl;
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
        final GlutenProfileCriteria that = (GlutenProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(diseas, that.diseas) &&
            Objects.equals(otherDiseas, that.otherDiseas) &&
            Objects.equals(strictnessLevel, that.strictnessLevel) &&
            Objects.equals(diaryFreePreferenceLvl, that.diaryFreePreferenceLvl) &&
            Objects.equals(veganPreferenceLvl, that.veganPreferenceLvl) &&
            Objects.equals(ketoPreferenceLvl, that.ketoPreferenceLvl) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            diseas,
            otherDiseas,
            strictnessLevel,
            diaryFreePreferenceLvl,
            veganPreferenceLvl,
            ketoPreferenceLvl,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlutenProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (diseas != null ? "diseas=" + diseas + ", " : "") +
            (otherDiseas != null ? "otherDiseas=" + otherDiseas + ", " : "") +
            (strictnessLevel != null ? "strictnessLevel=" + strictnessLevel + ", " : "") +
            (diaryFreePreferenceLvl != null ? "diaryFreePreferenceLvl=" + diaryFreePreferenceLvl + ", " : "") +
            (veganPreferenceLvl != null ? "veganPreferenceLvl=" + veganPreferenceLvl + ", " : "") +
            (ketoPreferenceLvl != null ? "ketoPreferenceLvl=" + ketoPreferenceLvl + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

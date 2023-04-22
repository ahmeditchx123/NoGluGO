package com.noglugo.mvp.domain;

import com.noglugo.mvp.domain.enumeration.Diseas;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GlutenProfile.
 */
@Entity
@Table(name = "gluten_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GlutenProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "diseas")
    private Diseas diseas;

    @Column(name = "other_diseas")
    private String otherDiseas;

    @Column(name = "strictness_level")
    private Integer strictnessLevel;

    @Column(name = "diary_free_preference_lvl")
    private Integer diaryFreePreferenceLvl;

    @Column(name = "vegan_preference_lvl")
    private Integer veganPreferenceLvl;

    @Column(name = "keto_preference_lvl")
    private Integer ketoPreferenceLvl;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public GlutenProfile id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Diseas getDiseas() {
        return this.diseas;
    }

    public GlutenProfile diseas(Diseas diseas) {
        this.setDiseas(diseas);
        return this;
    }

    public void setDiseas(Diseas diseas) {
        this.diseas = diseas;
    }

    public String getOtherDiseas() {
        return this.otherDiseas;
    }

    public GlutenProfile otherDiseas(String otherDiseas) {
        this.setOtherDiseas(otherDiseas);
        return this;
    }

    public void setOtherDiseas(String otherDiseas) {
        this.otherDiseas = otherDiseas;
    }

    public Integer getStrictnessLevel() {
        return this.strictnessLevel;
    }

    public GlutenProfile strictnessLevel(Integer strictnessLevel) {
        this.setStrictnessLevel(strictnessLevel);
        return this;
    }

    public void setStrictnessLevel(Integer strictnessLevel) {
        this.strictnessLevel = strictnessLevel;
    }

    public Integer getDiaryFreePreferenceLvl() {
        return this.diaryFreePreferenceLvl;
    }

    public GlutenProfile diaryFreePreferenceLvl(Integer diaryFreePreferenceLvl) {
        this.setDiaryFreePreferenceLvl(diaryFreePreferenceLvl);
        return this;
    }

    public void setDiaryFreePreferenceLvl(Integer diaryFreePreferenceLvl) {
        this.diaryFreePreferenceLvl = diaryFreePreferenceLvl;
    }

    public Integer getVeganPreferenceLvl() {
        return this.veganPreferenceLvl;
    }

    public GlutenProfile veganPreferenceLvl(Integer veganPreferenceLvl) {
        this.setVeganPreferenceLvl(veganPreferenceLvl);
        return this;
    }

    public void setVeganPreferenceLvl(Integer veganPreferenceLvl) {
        this.veganPreferenceLvl = veganPreferenceLvl;
    }

    public Integer getKetoPreferenceLvl() {
        return this.ketoPreferenceLvl;
    }

    public GlutenProfile ketoPreferenceLvl(Integer ketoPreferenceLvl) {
        this.setKetoPreferenceLvl(ketoPreferenceLvl);
        return this;
    }

    public void setKetoPreferenceLvl(Integer ketoPreferenceLvl) {
        this.ketoPreferenceLvl = ketoPreferenceLvl;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public GlutenProfile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public GlutenProfile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public GlutenProfile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public GlutenProfile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getUserId() {
        return this.userId;
    }

    public GlutenProfile userId(Long userId) {
        this.setUserId(userId);
        return this;
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
        if (!(o instanceof GlutenProfile)) {
            return false;
        }
        return id != null && id.equals(((GlutenProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlutenProfile{" +
            "id=" + getId() +
            ", diseas='" + getDiseas() + "'" +
            ", otherDiseas='" + getOtherDiseas() + "'" +
            ", strictnessLevel=" + getStrictnessLevel() +
            ", diaryFreePreferenceLvl=" + getDiaryFreePreferenceLvl() +
            ", veganPreferenceLvl=" + getVeganPreferenceLvl() +
            ", ketoPreferenceLvl=" + getKetoPreferenceLvl() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}

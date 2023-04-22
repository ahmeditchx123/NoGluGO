package com.noglugo.mvp.service.dto;

import com.noglugo.mvp.domain.enumeration.Diseas;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.noglugo.mvp.domain.GlutenProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GlutenProfileDTO implements Serializable {

    private UUID id;

    private Diseas diseas;

    private String otherDiseas;

    private Integer strictnessLevel;

    private Integer diaryFreePreferenceLvl;

    private Integer veganPreferenceLvl;

    private Integer ketoPreferenceLvl;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Long userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Diseas getDiseas() {
        return diseas;
    }

    public void setDiseas(Diseas diseas) {
        this.diseas = diseas;
    }

    public String getOtherDiseas() {
        return otherDiseas;
    }

    public void setOtherDiseas(String otherDiseas) {
        this.otherDiseas = otherDiseas;
    }

    public Integer getStrictnessLevel() {
        return strictnessLevel;
    }

    public void setStrictnessLevel(Integer strictnessLevel) {
        this.strictnessLevel = strictnessLevel;
    }

    public Integer getDiaryFreePreferenceLvl() {
        return diaryFreePreferenceLvl;
    }

    public void setDiaryFreePreferenceLvl(Integer diaryFreePreferenceLvl) {
        this.diaryFreePreferenceLvl = diaryFreePreferenceLvl;
    }

    public Integer getVeganPreferenceLvl() {
        return veganPreferenceLvl;
    }

    public void setVeganPreferenceLvl(Integer veganPreferenceLvl) {
        this.veganPreferenceLvl = veganPreferenceLvl;
    }

    public Integer getKetoPreferenceLvl() {
        return ketoPreferenceLvl;
    }

    public void setKetoPreferenceLvl(Integer ketoPreferenceLvl) {
        this.ketoPreferenceLvl = ketoPreferenceLvl;
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
        if (!(o instanceof GlutenProfileDTO)) {
            return false;
        }

        GlutenProfileDTO glutenProfileDTO = (GlutenProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, glutenProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlutenProfileDTO{" +
            "id='" + getId() + "'" +
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

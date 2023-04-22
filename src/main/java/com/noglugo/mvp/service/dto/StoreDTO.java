package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.noglugo.mvp.domain.Store} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String telephone;

    @NotNull
    private String imgPath;

    @NotNull
    private Boolean isDedicatedGlutenFree;

    private String website;

    @NotNull
    private Boolean hasDeliveryMode;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private AddressDTO addressDTO;

    private Long userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Boolean getIsDedicatedGlutenFree() {
        return isDedicatedGlutenFree;
    }

    public void setIsDedicatedGlutenFree(Boolean isDedicatedGlutenFree) {
        this.isDedicatedGlutenFree = isDedicatedGlutenFree;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getHasDeliveryMode() {
        return hasDeliveryMode;
    }

    public void setHasDeliveryMode(Boolean hasDeliveryMode) {
        this.hasDeliveryMode = hasDeliveryMode;
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

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
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
        if (!(o instanceof StoreDTO)) {
            return false;
        }

        StoreDTO storeDTO = (StoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", imgPath='" + getImgPath() + "'" +
            ", isDedicatedGlutenFree='" + getIsDedicatedGlutenFree() + "'" +
            ", website='" + getWebsite() + "'" +
            ", hasDeliveryMode='" + getHasDeliveryMode() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

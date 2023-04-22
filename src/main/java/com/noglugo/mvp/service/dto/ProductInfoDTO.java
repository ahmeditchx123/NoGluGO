package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.noglugo.mvp.domain.ProductInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInfoDTO implements Serializable {

    private UUID id;

    @NotNull
    private Integer qtyInStock;

    @NotNull
    private Boolean isGlutenFree;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ProductDTO product;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Integer qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public Boolean getIsGlutenFree() {
        return isGlutenFree;
    }

    public void setIsGlutenFree(Boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInfoDTO)) {
            return false;
        }

        ProductInfoDTO productInfoDTO = (ProductInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInfoDTO{" +
            "id='" + getId() + "'" +
            ", qtyInStock=" + getQtyInStock() +
            ", isGlutenFree='" + getIsGlutenFree() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}

package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.noglugo.mvp.domain.Cart} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartDTO implements Serializable {

    private UUID id;

    private Integer totalItems;

    private Double totalPrice;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<CartItemDTO> cartItemDTOS = new HashSet<>();

    private Long userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

    public Set<CartItemDTO> getCartItemDTOS() {
        return cartItemDTOS;
    }

    public void setCartItemDTOS(Set<CartItemDTO> cartItemDTOS) {
        this.cartItemDTOS = cartItemDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartDTO)) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartDTO{" +
            "id='" + getId() + "'" +
            ", totalItems=" + getTotalItems() +
            ", totalPrice=" + getTotalPrice() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}

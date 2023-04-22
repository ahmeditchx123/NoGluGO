package com.noglugo.mvp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "img_path", nullable = false)
    private String imgPath;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cart", "products" }, allowSetters = true)
    private CartItem cartItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "order", "products" }, allowSetters = true)
    private OrderItem orderItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "storeAddress", "products" }, allowSetters = true)
    private Store store;

    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    @OneToOne(mappedBy = "product")
    private ProductInfo information;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItem", "product" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Product id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public Product imgPath(String imgPath) {
        this.setImgPath(imgPath);
        return this;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public Product unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public Product isAvailable(Boolean isAvailable) {
        this.setIsAvailable(isAvailable);
        return this;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Product createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Product lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Product lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public CartItem getCartItem() {
        return this.cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public Product cartItem(CartItem cartItem) {
        this.setCartItem(cartItem);
        return this;
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Product orderItem(OrderItem orderItem) {
        this.setOrderItem(orderItem);
        return this;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product store(Store store) {
        this.setStore(store);
        return this;
    }

    public ProductInfo getInformation() {
        return this.information;
    }

    public void setInformation(ProductInfo productInfo) {
        if (this.information != null) {
            this.information.setProduct(null);
        }
        if (productInfo != null) {
            productInfo.setProduct(this);
        }
        this.information = productInfo;
    }

    public Product information(ProductInfo productInfo) {
        this.setInformation(productInfo);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setProduct(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setProduct(this));
        }
        this.reviews = reviews;
    }

    public Product reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Product addReviews(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
        return this;
    }

    public Product removeReviews(Review review) {
        this.reviews.remove(review);
        review.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sku='" + getSku() + "'" +
            ", description='" + getDescription() + "'" +
            ", imgPath='" + getImgPath() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", isAvailable='" + getIsAvailable() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

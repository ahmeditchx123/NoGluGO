package com.noglugo.mvp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.noglugo.mvp.domain.enumeration.DeliveryMethod;
import com.noglugo.mvp.domain.enumeration.OrderStatus;
import com.noglugo.mvp.domain.enumeration.PaymentMethod;
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
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @NotNull
    @Column(name = "total_items", nullable = false)
    private Integer totalItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

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

    @JsonIgnoreProperties(value = { "orderSA", "orderBA", "store", "restaurant", "location" }, allowSetters = true)
    @OneToOne(mappedBy = "orderSA")
    private Address shippingAddress;

    @JsonIgnoreProperties(value = { "orderSA", "orderBA", "store", "restaurant", "location" }, allowSetters = true)
    @OneToOne(mappedBy = "orderBA")
    private Address billingAddress;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order", "products" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Order id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Order totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalItems() {
        return this.totalItems;
    }

    public Order totalItems(Integer totalItems) {
        this.setTotalItems(totalItems);
        return this;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryMethod getDeliveryMethod() {
        return this.deliveryMethod;
    }

    public Order deliveryMethod(DeliveryMethod deliveryMethod) {
        this.setDeliveryMethod(deliveryMethod);
        return this;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public Order paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Order createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Order createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Order lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Order lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Order userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Address getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(Address address) {
        if (this.shippingAddress != null) {
            this.shippingAddress.setOrderSA(null);
        }
        if (address != null) {
            address.setOrderSA(this);
        }
        this.shippingAddress = address;
    }

    public Order shippingAddress(Address address) {
        this.setShippingAddress(address);
        return this;
    }

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address address) {
        if (this.billingAddress != null) {
            this.billingAddress.setOrderBA(null);
        }
        if (address != null) {
            address.setOrderBA(this);
        }
        this.billingAddress = address;
    }

    public Order billingAddress(Address address) {
        this.setBillingAddress(address);
        return this;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
    }

    public Order orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public Order addOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeOrderItems(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", totalPrice=" + getTotalPrice() +
            ", totalItems=" + getTotalItems() +
            ", status='" + getStatus() + "'" +
            ", deliveryMethod='" + getDeliveryMethod() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}

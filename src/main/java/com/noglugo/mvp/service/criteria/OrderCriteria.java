package com.noglugo.mvp.service.criteria;

import com.noglugo.mvp.domain.enumeration.DeliveryMethod;
import com.noglugo.mvp.domain.enumeration.OrderStatus;
import com.noglugo.mvp.domain.enumeration.PaymentMethod;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.noglugo.mvp.domain.Order} entity. This class is used
 * in {@link com.noglugo.mvp.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    /**
     * Class for filtering DeliveryMethod
     */
    public static class DeliveryMethodFilter extends Filter<DeliveryMethod> {

        public DeliveryMethodFilter() {}

        public DeliveryMethodFilter(DeliveryMethodFilter filter) {
            super(filter);
        }

        @Override
        public DeliveryMethodFilter copy() {
            return new DeliveryMethodFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {

        public PaymentMethodFilter() {}

        public PaymentMethodFilter(PaymentMethodFilter filter) {
            super(filter);
        }

        @Override
        public PaymentMethodFilter copy() {
            return new PaymentMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private DoubleFilter totalPrice;

    private IntegerFilter totalItems;

    private OrderStatusFilter status;

    private DeliveryMethodFilter deliveryMethod;

    private PaymentMethodFilter paymentMethod;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private UUIDFilter shippingAddressId;

    private UUIDFilter billingAddressId;

    private UUIDFilter orderItemsId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.totalItems = other.totalItems == null ? null : other.totalItems.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.deliveryMethod = other.deliveryMethod == null ? null : other.deliveryMethod.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.shippingAddressId = other.shippingAddressId == null ? null : other.shippingAddressId.copy();
        this.billingAddressId = other.billingAddressId == null ? null : other.billingAddressId.copy();
        this.orderItemsId = other.orderItemsId == null ? null : other.orderItemsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
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

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public IntegerFilter getTotalItems() {
        return totalItems;
    }

    public IntegerFilter totalItems() {
        if (totalItems == null) {
            totalItems = new IntegerFilter();
        }
        return totalItems;
    }

    public void setTotalItems(IntegerFilter totalItems) {
        this.totalItems = totalItems;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public OrderStatusFilter status() {
        if (status == null) {
            status = new OrderStatusFilter();
        }
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public DeliveryMethodFilter getDeliveryMethod() {
        return deliveryMethod;
    }

    public DeliveryMethodFilter deliveryMethod() {
        if (deliveryMethod == null) {
            deliveryMethod = new DeliveryMethodFilter();
        }
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethodFilter deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PaymentMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodFilter();
        }
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public UUIDFilter getShippingAddressId() {
        return shippingAddressId;
    }

    public UUIDFilter shippingAddressId() {
        if (shippingAddressId == null) {
            shippingAddressId = new UUIDFilter();
        }
        return shippingAddressId;
    }

    public void setShippingAddressId(UUIDFilter shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public UUIDFilter getBillingAddressId() {
        return billingAddressId;
    }

    public UUIDFilter billingAddressId() {
        if (billingAddressId == null) {
            billingAddressId = new UUIDFilter();
        }
        return billingAddressId;
    }

    public void setBillingAddressId(UUIDFilter billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public UUIDFilter getOrderItemsId() {
        return orderItemsId;
    }

    public UUIDFilter orderItemsId() {
        if (orderItemsId == null) {
            orderItemsId = new UUIDFilter();
        }
        return orderItemsId;
    }

    public void setOrderItemsId(UUIDFilter orderItemsId) {
        this.orderItemsId = orderItemsId;
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
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(totalItems, that.totalItems) &&
            Objects.equals(status, that.status) &&
            Objects.equals(deliveryMethod, that.deliveryMethod) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(shippingAddressId, that.shippingAddressId) &&
            Objects.equals(billingAddressId, that.billingAddressId) &&
            Objects.equals(orderItemsId, that.orderItemsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            totalPrice,
            totalItems,
            status,
            deliveryMethod,
            paymentMethod,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            shippingAddressId,
            billingAddressId,
            orderItemsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (totalItems != null ? "totalItems=" + totalItems + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (deliveryMethod != null ? "deliveryMethod=" + deliveryMethod + ", " : "") +
            (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (shippingAddressId != null ? "shippingAddressId=" + shippingAddressId + ", " : "") +
            (billingAddressId != null ? "billingAddressId=" + billingAddressId + ", " : "") +
            (orderItemsId != null ? "orderItemsId=" + orderItemsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

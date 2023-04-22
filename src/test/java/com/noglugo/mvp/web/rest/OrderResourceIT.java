package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.domain.enumeration.DeliveryMethod;
import com.noglugo.mvp.domain.enumeration.OrderStatus;
import com.noglugo.mvp.domain.enumeration.PaymentMethod;
import com.noglugo.mvp.repository.OrderRepository;
import com.noglugo.mvp.service.criteria.OrderCriteria;
import com.noglugo.mvp.service.dto.OrderDTO;
import com.noglugo.mvp.service.mapper.OrderMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final Integer DEFAULT_TOTAL_ITEMS = 1;
    private static final Integer UPDATED_TOTAL_ITEMS = 2;
    private static final Integer SMALLER_TOTAL_ITEMS = 1 - 1;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.NOT_VERIFIED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.VERIFIED;

    private static final DeliveryMethod DEFAULT_DELIVERY_METHOD = DeliveryMethod.RETRIEVAL;
    private static final DeliveryMethod UPDATED_DELIVERY_METHOD = DeliveryMethod.SHIPPING;

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.PAYMENT_IN_DELIVERY;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.PAYMENT_IN_RETRIEVAL;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .totalItems(DEFAULT_TOTAL_ITEMS)
            .status(DEFAULT_STATUS)
            .deliveryMethod(DEFAULT_DELIVERY_METHOD)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .userId(DEFAULT_USER_ID);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalItems(UPDATED_TOTAL_ITEMS)
            .status(UPDATED_STATUS)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrder.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrder.getDeliveryMethod()).isEqualTo(DEFAULT_DELIVERY_METHOD);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOrder.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        orderRepository.saveAndFlush(order);
        OrderDTO orderDTO = orderMapper.toDto(order);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setTotalPrice(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalItemsIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setTotalItems(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deliveryMethod").value(hasItem(DEFAULT_DELIVERY_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.totalItems").value(DEFAULT_TOTAL_ITEMS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.deliveryMethod").value(DEFAULT_DELIVERY_METHOD.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        UUID id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the orderList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is not null
        defaultOrderShouldBeFound("totalPrice.specified=true");

        // Get all the orderList where totalPrice is null
        defaultOrderShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultOrderShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultOrderShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems equals to DEFAULT_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.equals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the orderList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.equals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems in DEFAULT_TOTAL_ITEMS or UPDATED_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.in=" + DEFAULT_TOTAL_ITEMS + "," + UPDATED_TOTAL_ITEMS);

        // Get all the orderList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.in=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems is not null
        defaultOrderShouldBeFound("totalItems.specified=true");

        // Get all the orderList where totalItems is null
        defaultOrderShouldNotBeFound("totalItems.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems is greater than or equal to DEFAULT_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the orderList where totalItems is greater than or equal to UPDATED_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems is less than or equal to DEFAULT_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the orderList where totalItems is less than or equal to SMALLER_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems is less than DEFAULT_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.lessThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the orderList where totalItems is less than UPDATED_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.lessThan=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByTotalItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where totalItems is greater than DEFAULT_TOTAL_ITEMS
        defaultOrderShouldNotBeFound("totalItems.greaterThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the orderList where totalItems is greater than SMALLER_TOTAL_ITEMS
        defaultOrderShouldBeFound("totalItems.greaterThan=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status equals to DEFAULT_STATUS
        defaultOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the orderList where status equals to UPDATED_STATUS
        defaultOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the orderList where status equals to UPDATED_STATUS
        defaultOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where status is not null
        defaultOrderShouldBeFound("status.specified=true");

        // Get all the orderList where status is null
        defaultOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryMethod equals to DEFAULT_DELIVERY_METHOD
        defaultOrderShouldBeFound("deliveryMethod.equals=" + DEFAULT_DELIVERY_METHOD);

        // Get all the orderList where deliveryMethod equals to UPDATED_DELIVERY_METHOD
        defaultOrderShouldNotBeFound("deliveryMethod.equals=" + UPDATED_DELIVERY_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryMethodIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryMethod in DEFAULT_DELIVERY_METHOD or UPDATED_DELIVERY_METHOD
        defaultOrderShouldBeFound("deliveryMethod.in=" + DEFAULT_DELIVERY_METHOD + "," + UPDATED_DELIVERY_METHOD);

        // Get all the orderList where deliveryMethod equals to UPDATED_DELIVERY_METHOD
        defaultOrderShouldNotBeFound("deliveryMethod.in=" + UPDATED_DELIVERY_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryMethod is not null
        defaultOrderShouldBeFound("deliveryMethod.specified=true");

        // Get all the orderList where deliveryMethod is null
        defaultOrderShouldNotBeFound("deliveryMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod equals to DEFAULT_PAYMENT_METHOD
        defaultOrderShouldBeFound("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD);

        // Get all the orderList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultOrderShouldNotBeFound("paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod in DEFAULT_PAYMENT_METHOD or UPDATED_PAYMENT_METHOD
        defaultOrderShouldBeFound("paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD);

        // Get all the orderList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultOrderShouldNotBeFound("paymentMethod.in=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where paymentMethod is not null
        defaultOrderShouldBeFound("paymentMethod.specified=true");

        // Get all the orderList where paymentMethod is null
        defaultOrderShouldNotBeFound("paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy equals to DEFAULT_CREATED_BY
        defaultOrderShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultOrderShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the orderList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy is not null
        defaultOrderShouldBeFound("createdBy.specified=true");

        // Get all the orderList where createdBy is null
        defaultOrderShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy contains DEFAULT_CREATED_BY
        defaultOrderShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy contains UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy does not contain DEFAULT_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy does not contain UPDATED_CREATED_BY
        defaultOrderShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdDate equals to DEFAULT_CREATED_DATE
        defaultOrderShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the orderList where createdDate equals to UPDATED_CREATED_DATE
        defaultOrderShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultOrderShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the orderList where createdDate equals to UPDATED_CREATED_DATE
        defaultOrderShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdDate is not null
        defaultOrderShouldBeFound("createdDate.specified=true");

        // Get all the orderList where createdDate is null
        defaultOrderShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultOrderShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultOrderShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultOrderShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the orderList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultOrderShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedBy is not null
        defaultOrderShouldBeFound("lastModifiedBy.specified=true");

        // Get all the orderList where lastModifiedBy is null
        defaultOrderShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultOrderShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultOrderShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultOrderShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultOrderShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultOrderShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the orderList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultOrderShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultOrderShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the orderList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultOrderShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where lastModifiedDate is not null
        defaultOrderShouldBeFound("lastModifiedDate.specified=true");

        // Get all the orderList where lastModifiedDate is null
        defaultOrderShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId equals to DEFAULT_USER_ID
        defaultOrderShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the orderList where userId equals to UPDATED_USER_ID
        defaultOrderShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultOrderShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the orderList where userId equals to UPDATED_USER_ID
        defaultOrderShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId is not null
        defaultOrderShouldBeFound("userId.specified=true");

        // Get all the orderList where userId is null
        defaultOrderShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId is greater than or equal to DEFAULT_USER_ID
        defaultOrderShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the orderList where userId is greater than or equal to UPDATED_USER_ID
        defaultOrderShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId is less than or equal to DEFAULT_USER_ID
        defaultOrderShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the orderList where userId is less than or equal to SMALLER_USER_ID
        defaultOrderShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId is less than DEFAULT_USER_ID
        defaultOrderShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the orderList where userId is less than UPDATED_USER_ID
        defaultOrderShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where userId is greater than DEFAULT_USER_ID
        defaultOrderShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the orderList where userId is greater than SMALLER_USER_ID
        defaultOrderShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByShippingAddressIsEqualToSomething() throws Exception {
        Address shippingAddress;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            shippingAddress = AddressResourceIT.createEntity(em);
        } else {
            shippingAddress = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(shippingAddress);
        em.flush();
        order.setShippingAddress(shippingAddress);
        shippingAddress.setOrderSA(order);
        orderRepository.saveAndFlush(order);
        UUID shippingAddressId = shippingAddress.getId();

        // Get all the orderList where shippingAddress equals to shippingAddressId
        defaultOrderShouldBeFound("shippingAddressId.equals=" + shippingAddressId);

        // Get all the orderList where shippingAddress equals to UUID.randomUUID()
        defaultOrderShouldNotBeFound("shippingAddressId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllOrdersByBillingAddressIsEqualToSomething() throws Exception {
        Address billingAddress;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            billingAddress = AddressResourceIT.createEntity(em);
        } else {
            billingAddress = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(billingAddress);
        em.flush();
        order.setBillingAddress(billingAddress);
        billingAddress.setOrderBA(order);
        orderRepository.saveAndFlush(order);
        UUID billingAddressId = billingAddress.getId();

        // Get all the orderList where billingAddress equals to billingAddressId
        defaultOrderShouldBeFound("billingAddressId.equals=" + billingAddressId);

        // Get all the orderList where billingAddress equals to UUID.randomUUID()
        defaultOrderShouldNotBeFound("billingAddressId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllOrdersByOrderItemsIsEqualToSomething() throws Exception {
        OrderItem orderItems;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            orderRepository.saveAndFlush(order);
            orderItems = OrderItemResourceIT.createEntity(em);
        } else {
            orderItems = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        em.persist(orderItems);
        em.flush();
        order.addOrderItems(orderItems);
        orderRepository.saveAndFlush(order);
        UUID orderItemsId = orderItems.getId();

        // Get all the orderList where orderItems equals to orderItemsId
        defaultOrderShouldBeFound("orderItemsId.equals=" + orderItemsId);

        // Get all the orderList where orderItems equals to UUID.randomUUID()
        defaultOrderShouldNotBeFound("orderItemsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deliveryMethod").value(hasItem(DEFAULT_DELIVERY_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalItems(UPDATED_TOTAL_ITEMS)
            .status(UPDATED_STATUS)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalItems(UPDATED_TOTAL_ITEMS)
            .status(UPDATED_STATUS)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrder.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrder.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(UUID.randomUUID());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

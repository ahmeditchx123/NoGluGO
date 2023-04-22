package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.repository.OrderItemRepository;
import com.noglugo.mvp.service.criteria.OrderItemCriteria;
import com.noglugo.mvp.service.dto.OrderItemDTO;
import com.noglugo.mvp.service.mapper.OrderItemMapper;
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
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final Integer SMALLER_QTY = 1 - 1;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .qty(DEFAULT_QTY)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrderItem.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrderItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrderItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrderItem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrderItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItemRepository.saveAndFlush(orderItem);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setQty(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setTotalPrice(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        UUID id = orderItem.getId();

        defaultOrderItemShouldBeFound("id.equals=" + id);
        defaultOrderItemShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty equals to DEFAULT_QTY
        defaultOrderItemShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the orderItemList where qty equals to UPDATED_QTY
        defaultOrderItemShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultOrderItemShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the orderItemList where qty equals to UPDATED_QTY
        defaultOrderItemShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty is not null
        defaultOrderItemShouldBeFound("qty.specified=true");

        // Get all the orderItemList where qty is null
        defaultOrderItemShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty is greater than or equal to DEFAULT_QTY
        defaultOrderItemShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the orderItemList where qty is greater than or equal to UPDATED_QTY
        defaultOrderItemShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty is less than or equal to DEFAULT_QTY
        defaultOrderItemShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the orderItemList where qty is less than or equal to SMALLER_QTY
        defaultOrderItemShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty is less than DEFAULT_QTY
        defaultOrderItemShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the orderItemList where qty is less than UPDATED_QTY
        defaultOrderItemShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where qty is greater than DEFAULT_QTY
        defaultOrderItemShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the orderItemList where qty is greater than SMALLER_QTY
        defaultOrderItemShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice is not null
        defaultOrderItemShouldBeFound("totalPrice.specified=true");

        // Get all the orderItemList where totalPrice is null
        defaultOrderItemShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultOrderItemShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the orderItemList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultOrderItemShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdBy equals to DEFAULT_CREATED_BY
        defaultOrderItemShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the orderItemList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderItemShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultOrderItemShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the orderItemList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderItemShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdBy is not null
        defaultOrderItemShouldBeFound("createdBy.specified=true");

        // Get all the orderItemList where createdBy is null
        defaultOrderItemShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdBy contains DEFAULT_CREATED_BY
        defaultOrderItemShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the orderItemList where createdBy contains UPDATED_CREATED_BY
        defaultOrderItemShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdBy does not contain DEFAULT_CREATED_BY
        defaultOrderItemShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the orderItemList where createdBy does not contain UPDATED_CREATED_BY
        defaultOrderItemShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdDate equals to DEFAULT_CREATED_DATE
        defaultOrderItemShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the orderItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultOrderItemShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultOrderItemShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the orderItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultOrderItemShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdDate is not null
        defaultOrderItemShouldBeFound("createdDate.specified=true");

        // Get all the orderItemList where createdDate is null
        defaultOrderItemShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultOrderItemShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultOrderItemShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultOrderItemShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the orderItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultOrderItemShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedBy is not null
        defaultOrderItemShouldBeFound("lastModifiedBy.specified=true");

        // Get all the orderItemList where lastModifiedBy is null
        defaultOrderItemShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultOrderItemShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderItemList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultOrderItemShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultOrderItemShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the orderItemList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultOrderItemShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultOrderItemShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the orderItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultOrderItemShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultOrderItemShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the orderItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultOrderItemShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where lastModifiedDate is not null
        defaultOrderItemShouldBeFound("lastModifiedDate.specified=true");

        // Get all the orderItemList where lastModifiedDate is null
        defaultOrderItemShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        orderItem.setOrder(order);
        orderItemRepository.saveAndFlush(orderItem);
        UUID orderId = order.getId();

        // Get all the orderItemList where order equals to orderId
        defaultOrderItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderItemList where order equals to UUID.randomUUID()
        defaultOrderItemShouldNotBeFound("orderId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllOrderItemsByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            orderItemRepository.saveAndFlush(orderItem);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        orderItem.addProducts(products);
        orderItemRepository.saveAndFlush(orderItem);
        UUID productsId = products.getId();

        // Get all the orderItemList where products equals to productsId
        defaultOrderItemShouldBeFound("productsId.equals=" + productsId);

        // Get all the orderItemList where products equals to UUID.randomUUID()
        defaultOrderItemShouldNotBeFound("productsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemShouldBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemShouldNotBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(updatedOrderItem);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrderItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .qty(UPDATED_QTY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderItem.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrderItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrderItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrderItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrderItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

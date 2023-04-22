package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Cart;
import com.noglugo.mvp.domain.CartItem;
import com.noglugo.mvp.repository.CartRepository;
import com.noglugo.mvp.service.criteria.CartCriteria;
import com.noglugo.mvp.service.dto.CartDTO;
import com.noglugo.mvp.service.mapper.CartMapper;
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
 * Integration tests for the {@link CartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartResourceIT {

    private static final Integer DEFAULT_TOTAL_ITEMS = 1;
    private static final Integer UPDATED_TOTAL_ITEMS = 2;
    private static final Integer SMALLER_TOTAL_ITEMS = 1 - 1;

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

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartMockMvc;

    private Cart cart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart()
            .totalItems(DEFAULT_TOTAL_ITEMS)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .userId(DEFAULT_USER_ID);
        return cart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createUpdatedEntity(EntityManager em) {
        Cart cart = new Cart()
            .totalItems(UPDATED_TOTAL_ITEMS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        return cart;
    }

    @BeforeEach
    public void initTest() {
        cart = createEntity(em);
    }

    @Test
    @Transactional
    void createCart() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();
        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate + 1);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);
        assertThat(testCart.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCart.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCart.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCart.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCart.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCart.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createCartWithExistingId() throws Exception {
        // Create the Cart with an existing ID
        cartRepository.saveAndFlush(cart);
        CartDTO cartDTO = cartMapper.toDto(cart);

        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarts() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc
            .perform(get(ENTITY_API_URL_ID, cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().toString()))
            .andExpect(jsonPath("$.totalItems").value(DEFAULT_TOTAL_ITEMS))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getCartsByIdFiltering() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        UUID id = cart.getId();

        defaultCartShouldBeFound("id.equals=" + id);
        defaultCartShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems equals to DEFAULT_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.equals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the cartList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.equals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems in DEFAULT_TOTAL_ITEMS or UPDATED_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.in=" + DEFAULT_TOTAL_ITEMS + "," + UPDATED_TOTAL_ITEMS);

        // Get all the cartList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.in=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems is not null
        defaultCartShouldBeFound("totalItems.specified=true");

        // Get all the cartList where totalItems is null
        defaultCartShouldNotBeFound("totalItems.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems is greater than or equal to DEFAULT_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the cartList where totalItems is greater than or equal to UPDATED_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems is less than or equal to DEFAULT_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the cartList where totalItems is less than or equal to SMALLER_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems is less than DEFAULT_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.lessThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the cartList where totalItems is less than UPDATED_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.lessThan=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalItems is greater than DEFAULT_TOTAL_ITEMS
        defaultCartShouldNotBeFound("totalItems.greaterThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the cartList where totalItems is greater than SMALLER_TOTAL_ITEMS
        defaultCartShouldBeFound("totalItems.greaterThan=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is not null
        defaultCartShouldBeFound("totalPrice.specified=true");

        // Get all the cartList where totalPrice is null
        defaultCartShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdBy equals to DEFAULT_CREATED_BY
        defaultCartShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the cartList where createdBy equals to UPDATED_CREATED_BY
        defaultCartShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCartShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the cartList where createdBy equals to UPDATED_CREATED_BY
        defaultCartShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdBy is not null
        defaultCartShouldBeFound("createdBy.specified=true");

        // Get all the cartList where createdBy is null
        defaultCartShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdBy contains DEFAULT_CREATED_BY
        defaultCartShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the cartList where createdBy contains UPDATED_CREATED_BY
        defaultCartShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdBy does not contain DEFAULT_CREATED_BY
        defaultCartShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the cartList where createdBy does not contain UPDATED_CREATED_BY
        defaultCartShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCartShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the cartList where createdDate equals to UPDATED_CREATED_DATE
        defaultCartShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCartShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the cartList where createdDate equals to UPDATED_CREATED_DATE
        defaultCartShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate is not null
        defaultCartShouldBeFound("createdDate.specified=true");

        // Get all the cartList where createdDate is null
        defaultCartShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCartShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCartShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCartShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the cartList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCartShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedBy is not null
        defaultCartShouldBeFound("lastModifiedBy.specified=true");

        // Get all the cartList where lastModifiedBy is null
        defaultCartShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultCartShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultCartShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultCartShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultCartShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCartShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the cartList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCartShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCartShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the cartList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCartShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate is not null
        defaultCartShouldBeFound("lastModifiedDate.specified=true");

        // Get all the cartList where lastModifiedDate is null
        defaultCartShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId equals to DEFAULT_USER_ID
        defaultCartShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the cartList where userId equals to UPDATED_USER_ID
        defaultCartShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultCartShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the cartList where userId equals to UPDATED_USER_ID
        defaultCartShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId is not null
        defaultCartShouldBeFound("userId.specified=true");

        // Get all the cartList where userId is null
        defaultCartShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId is greater than or equal to DEFAULT_USER_ID
        defaultCartShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the cartList where userId is greater than or equal to UPDATED_USER_ID
        defaultCartShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId is less than or equal to DEFAULT_USER_ID
        defaultCartShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the cartList where userId is less than or equal to SMALLER_USER_ID
        defaultCartShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId is less than DEFAULT_USER_ID
        defaultCartShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the cartList where userId is less than UPDATED_USER_ID
        defaultCartShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where userId is greater than DEFAULT_USER_ID
        defaultCartShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the cartList where userId is greater than SMALLER_USER_ID
        defaultCartShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllCartsByCartItemsIsEqualToSomething() throws Exception {
        CartItem cartItems;
        if (TestUtil.findAll(em, CartItem.class).isEmpty()) {
            cartRepository.saveAndFlush(cart);
            cartItems = CartItemResourceIT.createEntity(em);
        } else {
            cartItems = TestUtil.findAll(em, CartItem.class).get(0);
        }
        em.persist(cartItems);
        em.flush();
        cart.addCartItems(cartItems);
        cartRepository.saveAndFlush(cart);
        UUID cartItemsId = cartItems.getId();

        // Get all the cartList where cartItems equals to cartItemsId
        defaultCartShouldBeFound("cartItemsId.equals=" + cartItemsId);

        // Get all the cartList where cartItems equals to UUID.randomUUID()
        defaultCartShouldNotBeFound("cartItemsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartShouldBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().toString())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartShouldNotBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        // Disconnect from session so that the updates on updatedCart are not directly saved in db
        em.detach(updatedCart);
        updatedCart
            .totalItems(UPDATED_TOTAL_ITEMS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        CartDTO cartDTO = cartMapper.toDto(updatedCart);

        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
        assertThat(testCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCart.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCart.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCart.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCart.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart
            .totalItems(UPDATED_TOTAL_ITEMS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
        assertThat(testCart.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCart.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCart.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCart.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCart.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCart.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart
            .totalItems(UPDATED_TOTAL_ITEMS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
        assertThat(testCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCart.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCart.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCart.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCart.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(UUID.randomUUID());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeDelete = cartRepository.findAll().size();

        // Delete the cart
        restCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, cart.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

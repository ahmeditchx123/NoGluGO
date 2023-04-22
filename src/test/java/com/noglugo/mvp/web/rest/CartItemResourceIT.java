package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Cart;
import com.noglugo.mvp.domain.CartItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.repository.CartItemRepository;
import com.noglugo.mvp.service.criteria.CartItemCriteria;
import com.noglugo.mvp.service.dto.CartItemDTO;
import com.noglugo.mvp.service.mapper.CartItemMapper;
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
 * Integration tests for the {@link CartItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartItemResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cart-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartItemMockMvc;

    private CartItem cartItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createEntity(EntityManager em) {
        CartItem cartItem = new CartItem()
            .qty(DEFAULT_QTY)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return cartItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createUpdatedEntity(EntityManager em) {
        CartItem cartItem = new CartItem()
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return cartItem;
    }

    @BeforeEach
    public void initTest() {
        cartItem = createEntity(em);
    }

    @Test
    @Transactional
    void createCartItem() throws Exception {
        int databaseSizeBeforeCreate = cartItemRepository.findAll().size();
        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);
        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartItemDTO)))
            .andExpect(status().isCreated());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeCreate + 1);
        CartItem testCartItem = cartItemList.get(cartItemList.size() - 1);
        assertThat(testCartItem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testCartItem.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCartItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCartItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCartItem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCartItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCartItemWithExistingId() throws Exception {
        // Create the CartItem with an existing ID
        cartItemRepository.saveAndFlush(cartItem);
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        int databaseSizeBeforeCreate = cartItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartItemRepository.findAll().size();
        // set the field null
        cartItem.setQty(null);

        // Create the CartItem, which fails.
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartItemRepository.findAll().size();
        // set the field null
        cartItem.setTotalPrice(null);

        // Create the CartItem, which fails.
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartItemDTO)))
            .andExpect(status().isBadRequest());

        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCartItems() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCartItem() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get the cartItem
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL_ID, cartItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartItem.getId().toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCartItemsByIdFiltering() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        UUID id = cartItem.getId();

        defaultCartItemShouldBeFound("id.equals=" + id);
        defaultCartItemShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty equals to DEFAULT_QTY
        defaultCartItemShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the cartItemList where qty equals to UPDATED_QTY
        defaultCartItemShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultCartItemShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the cartItemList where qty equals to UPDATED_QTY
        defaultCartItemShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty is not null
        defaultCartItemShouldBeFound("qty.specified=true");

        // Get all the cartItemList where qty is null
        defaultCartItemShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty is greater than or equal to DEFAULT_QTY
        defaultCartItemShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the cartItemList where qty is greater than or equal to UPDATED_QTY
        defaultCartItemShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty is less than or equal to DEFAULT_QTY
        defaultCartItemShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the cartItemList where qty is less than or equal to SMALLER_QTY
        defaultCartItemShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty is less than DEFAULT_QTY
        defaultCartItemShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the cartItemList where qty is less than UPDATED_QTY
        defaultCartItemShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where qty is greater than DEFAULT_QTY
        defaultCartItemShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the cartItemList where qty is greater than SMALLER_QTY
        defaultCartItemShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is not null
        defaultCartItemShouldBeFound("totalPrice.specified=true");

        // Get all the cartItemList where totalPrice is null
        defaultCartItemShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultCartItemShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartItemList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultCartItemShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdBy equals to DEFAULT_CREATED_BY
        defaultCartItemShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the cartItemList where createdBy equals to UPDATED_CREATED_BY
        defaultCartItemShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCartItemShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the cartItemList where createdBy equals to UPDATED_CREATED_BY
        defaultCartItemShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdBy is not null
        defaultCartItemShouldBeFound("createdBy.specified=true");

        // Get all the cartItemList where createdBy is null
        defaultCartItemShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdBy contains DEFAULT_CREATED_BY
        defaultCartItemShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the cartItemList where createdBy contains UPDATED_CREATED_BY
        defaultCartItemShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdBy does not contain DEFAULT_CREATED_BY
        defaultCartItemShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the cartItemList where createdBy does not contain UPDATED_CREATED_BY
        defaultCartItemShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCartItemShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the cartItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultCartItemShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCartItemShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the cartItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultCartItemShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCartItemsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where createdDate is not null
        defaultCartItemShouldBeFound("createdDate.specified=true");

        // Get all the cartItemList where createdDate is null
        defaultCartItemShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCartItemShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCartItemShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCartItemShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the cartItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCartItemShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedBy is not null
        defaultCartItemShouldBeFound("lastModifiedBy.specified=true");

        // Get all the cartItemList where lastModifiedBy is null
        defaultCartItemShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultCartItemShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartItemList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultCartItemShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultCartItemShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the cartItemList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultCartItemShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCartItemShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the cartItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCartItemShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCartItemShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the cartItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCartItemShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCartItemsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where lastModifiedDate is not null
        defaultCartItemShouldBeFound("lastModifiedDate.specified=true");

        // Get all the cartItemList where lastModifiedDate is null
        defaultCartItemShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByCartIsEqualToSomething() throws Exception {
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            cartItemRepository.saveAndFlush(cartItem);
            cart = CartResourceIT.createEntity(em);
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        em.persist(cart);
        em.flush();
        cartItem.setCart(cart);
        cartItemRepository.saveAndFlush(cartItem);
        UUID cartId = cart.getId();

        // Get all the cartItemList where cart equals to cartId
        defaultCartItemShouldBeFound("cartId.equals=" + cartId);

        // Get all the cartItemList where cart equals to UUID.randomUUID()
        defaultCartItemShouldNotBeFound("cartId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllCartItemsByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            cartItemRepository.saveAndFlush(cartItem);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        cartItem.addProducts(products);
        cartItemRepository.saveAndFlush(cartItem);
        UUID productsId = products.getId();

        // Get all the cartItemList where products equals to productsId
        defaultCartItemShouldBeFound("productsId.equals=" + productsId);

        // Get all the cartItemList where products equals to UUID.randomUUID()
        defaultCartItemShouldNotBeFound("productsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartItemShouldBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartItemShouldNotBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCartItem() throws Exception {
        // Get the cartItem
        restCartItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCartItem() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();

        // Update the cartItem
        CartItem updatedCartItem = cartItemRepository.findById(cartItem.getId()).get();
        // Disconnect from session so that the updates on updatedCartItem are not directly saved in db
        em.detach(updatedCartItem);
        updatedCartItem
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CartItemDTO cartItemDTO = cartItemMapper.toDto(updatedCartItem);

        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
        CartItem testCartItem = cartItemList.get(cartItemList.size() - 1);
        assertThat(testCartItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testCartItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCartItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCartItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCartItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem.totalPrice(UPDATED_TOTAL_PRICE).createdBy(UPDATED_CREATED_BY);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
        CartItem testCartItem = cartItemList.get(cartItemList.size() - 1);
        assertThat(testCartItem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testCartItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCartItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCartItem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCartItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem
            .qty(UPDATED_QTY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
        CartItem testCartItem = cartItemList.get(cartItemList.size() - 1);
        assertThat(testCartItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testCartItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCartItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCartItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCartItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartItem() throws Exception {
        int databaseSizeBeforeUpdate = cartItemRepository.findAll().size();
        cartItem.setId(UUID.randomUUID());

        // Create the CartItem
        CartItemDTO cartItemDTO = cartItemMapper.toDto(cartItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cartItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartItem() throws Exception {
        // Initialize the database
        cartItemRepository.saveAndFlush(cartItem);

        int databaseSizeBeforeDelete = cartItemRepository.findAll().size();

        // Delete the cartItem
        restCartItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartItem.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartItem> cartItemList = cartItemRepository.findAll();
        assertThat(cartItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

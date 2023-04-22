package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.CartItem;
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.ProductInfo;
import com.noglugo.mvp.domain.Review;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.repository.ProductRepository;
import com.noglugo.mvp.service.criteria.ProductCriteria;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMG_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;
    private static final Double SMALLER_UNIT_PRICE = 1D - 1D;

    private static final Boolean DEFAULT_IS_AVAILABLE = false;
    private static final Boolean UPDATED_IS_AVAILABLE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .sku(DEFAULT_SKU)
            .description(DEFAULT_DESCRIPTION)
            .imgPath(DEFAULT_IMG_PATH)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .isAvailable(DEFAULT_IS_AVAILABLE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testProduct.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testProduct.getIsAvailable()).isEqualTo(DEFAULT_IS_AVAILABLE);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProduct.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProduct.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        productRepository.saveAndFlush(product);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSku(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImgPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setImgPath(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setUnitPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setIsAvailable(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.isAvailable").value(DEFAULT_IS_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        UUID id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to DEFAULT_SKU
        defaultProductShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultProductShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductShouldBeFound("sku.specified=true");

        // Get all the productList where sku is null
        defaultProductShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku contains DEFAULT_SKU
        defaultProductShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the productList where sku contains UPDATED_SKU
        defaultProductShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain DEFAULT_SKU
        defaultProductShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the productList where sku does not contain UPDATED_SKU
        defaultProductShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByImgPathIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imgPath equals to DEFAULT_IMG_PATH
        defaultProductShouldBeFound("imgPath.equals=" + DEFAULT_IMG_PATH);

        // Get all the productList where imgPath equals to UPDATED_IMG_PATH
        defaultProductShouldNotBeFound("imgPath.equals=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllProductsByImgPathIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imgPath in DEFAULT_IMG_PATH or UPDATED_IMG_PATH
        defaultProductShouldBeFound("imgPath.in=" + DEFAULT_IMG_PATH + "," + UPDATED_IMG_PATH);

        // Get all the productList where imgPath equals to UPDATED_IMG_PATH
        defaultProductShouldNotBeFound("imgPath.in=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllProductsByImgPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imgPath is not null
        defaultProductShouldBeFound("imgPath.specified=true");

        // Get all the productList where imgPath is null
        defaultProductShouldNotBeFound("imgPath.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByImgPathContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imgPath contains DEFAULT_IMG_PATH
        defaultProductShouldBeFound("imgPath.contains=" + DEFAULT_IMG_PATH);

        // Get all the productList where imgPath contains UPDATED_IMG_PATH
        defaultProductShouldNotBeFound("imgPath.contains=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllProductsByImgPathNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imgPath does not contain DEFAULT_IMG_PATH
        defaultProductShouldNotBeFound("imgPath.doesNotContain=" + DEFAULT_IMG_PATH);

        // Get all the productList where imgPath does not contain UPDATED_IMG_PATH
        defaultProductShouldBeFound("imgPath.doesNotContain=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the productList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is not null
        defaultProductShouldBeFound("unitPrice.specified=true");

        // Get all the productList where unitPrice is null
        defaultProductShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is greater than or equal to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is greater than or equal to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is less than or equal to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is less than or equal to SMALLER_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is less than DEFAULT_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is less than UPDATED_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.lessThan=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is greater than DEFAULT_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is greater than SMALLER_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByIsAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isAvailable equals to DEFAULT_IS_AVAILABLE
        defaultProductShouldBeFound("isAvailable.equals=" + DEFAULT_IS_AVAILABLE);

        // Get all the productList where isAvailable equals to UPDATED_IS_AVAILABLE
        defaultProductShouldNotBeFound("isAvailable.equals=" + UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByIsAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isAvailable in DEFAULT_IS_AVAILABLE or UPDATED_IS_AVAILABLE
        defaultProductShouldBeFound("isAvailable.in=" + DEFAULT_IS_AVAILABLE + "," + UPDATED_IS_AVAILABLE);

        // Get all the productList where isAvailable equals to UPDATED_IS_AVAILABLE
        defaultProductShouldNotBeFound("isAvailable.in=" + UPDATED_IS_AVAILABLE);
    }

    @Test
    @Transactional
    void getAllProductsByIsAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isAvailable is not null
        defaultProductShouldBeFound("isAvailable.specified=true");

        // Get all the productList where isAvailable is null
        defaultProductShouldNotBeFound("isAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy equals to DEFAULT_CREATED_BY
        defaultProductShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy equals to UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProductShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the productList where createdBy equals to UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy is not null
        defaultProductShouldBeFound("createdBy.specified=true");

        // Get all the productList where createdBy is null
        defaultProductShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy contains DEFAULT_CREATED_BY
        defaultProductShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy contains UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy does not contain DEFAULT_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy does not contain UPDATED_CREATED_BY
        defaultProductShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdDate equals to DEFAULT_CREATED_DATE
        defaultProductShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the productList where createdDate equals to UPDATED_CREATED_DATE
        defaultProductShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultProductShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the productList where createdDate equals to UPDATED_CREATED_DATE
        defaultProductShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdDate is not null
        defaultProductShouldBeFound("createdDate.specified=true");

        // Get all the productList where createdDate is null
        defaultProductShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultProductShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultProductShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the productList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedBy is not null
        defaultProductShouldBeFound("lastModifiedBy.specified=true");

        // Get all the productList where lastModifiedBy is null
        defaultProductShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultProductShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultProductShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultProductShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultProductShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultProductShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the productList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProductShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultProductShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the productList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProductShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastModifiedDate is not null
        defaultProductShouldBeFound("lastModifiedDate.specified=true");

        // Get all the productList where lastModifiedDate is null
        defaultProductShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCartItemIsEqualToSomething() throws Exception {
        CartItem cartItem;
        if (TestUtil.findAll(em, CartItem.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            cartItem = CartItemResourceIT.createEntity(em);
        } else {
            cartItem = TestUtil.findAll(em, CartItem.class).get(0);
        }
        em.persist(cartItem);
        em.flush();
        product.setCartItem(cartItem);
        productRepository.saveAndFlush(product);
        UUID cartItemId = cartItem.getId();

        // Get all the productList where cartItem equals to cartItemId
        defaultProductShouldBeFound("cartItemId.equals=" + cartItemId);

        // Get all the productList where cartItem equals to UUID.randomUUID()
        defaultProductShouldNotBeFound("cartItemId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllProductsByOrderItemIsEqualToSomething() throws Exception {
        OrderItem orderItem;
        if (TestUtil.findAll(em, OrderItem.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            orderItem = OrderItemResourceIT.createEntity(em);
        } else {
            orderItem = TestUtil.findAll(em, OrderItem.class).get(0);
        }
        em.persist(orderItem);
        em.flush();
        product.setOrderItem(orderItem);
        productRepository.saveAndFlush(product);
        UUID orderItemId = orderItem.getId();

        // Get all the productList where orderItem equals to orderItemId
        defaultProductShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the productList where orderItem equals to UUID.randomUUID()
        defaultProductShouldNotBeFound("orderItemId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllProductsByStoreIsEqualToSomething() throws Exception {
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            store = StoreResourceIT.createEntity(em);
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(store);
        em.flush();
        product.setStore(store);
        productRepository.saveAndFlush(product);
        UUID storeId = store.getId();

        // Get all the productList where store equals to storeId
        defaultProductShouldBeFound("storeId.equals=" + storeId);

        // Get all the productList where store equals to UUID.randomUUID()
        defaultProductShouldNotBeFound("storeId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllProductsByInformationIsEqualToSomething() throws Exception {
        ProductInfo information;
        if (TestUtil.findAll(em, ProductInfo.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            information = ProductInfoResourceIT.createEntity(em);
        } else {
            information = TestUtil.findAll(em, ProductInfo.class).get(0);
        }
        em.persist(information);
        em.flush();
        product.setInformation(information);
        information.setProduct(product);
        productRepository.saveAndFlush(product);
        UUID informationId = information.getId();

        // Get all the productList where information equals to informationId
        defaultProductShouldBeFound("informationId.equals=" + informationId);

        // Get all the productList where information equals to UUID.randomUUID()
        defaultProductShouldNotBeFound("informationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllProductsByReviewsIsEqualToSomething() throws Exception {
        Review reviews;
        if (TestUtil.findAll(em, Review.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            reviews = ReviewResourceIT.createEntity(em);
        } else {
            reviews = TestUtil.findAll(em, Review.class).get(0);
        }
        em.persist(reviews);
        em.flush();
        product.addReviews(reviews);
        productRepository.saveAndFlush(product);
        UUID reviewsId = reviews.getId();

        // Get all the productList where reviews equals to reviewsId
        defaultProductShouldBeFound("reviewsId.equals=" + reviewsId);

        // Get all the productList where reviews equals to UUID.randomUUID()
        defaultProductShouldNotBeFound("reviewsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testProduct.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProduct.getIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProduct.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProduct.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.unitPrice(UPDATED_UNIT_PRICE).isAvailable(UPDATED_IS_AVAILABLE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testProduct.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProduct.getIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProduct.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProduct.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .description(UPDATED_DESCRIPTION)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testProduct.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProduct.getIsAvailable()).isEqualTo(UPDATED_IS_AVAILABLE);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProduct.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProduct.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

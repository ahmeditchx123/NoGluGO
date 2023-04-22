package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.ProductInfo;
import com.noglugo.mvp.repository.ProductInfoRepository;
import com.noglugo.mvp.service.criteria.ProductInfoCriteria;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
import com.noglugo.mvp.service.mapper.ProductInfoMapper;
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
 * Integration tests for the {@link ProductInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductInfoResourceIT {

    private static final Integer DEFAULT_QTY_IN_STOCK = 1;
    private static final Integer UPDATED_QTY_IN_STOCK = 2;
    private static final Integer SMALLER_QTY_IN_STOCK = 1 - 1;

    private static final Boolean DEFAULT_IS_GLUTEN_FREE = false;
    private static final Boolean UPDATED_IS_GLUTEN_FREE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/product-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductInfoMockMvc;

    private ProductInfo productInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInfo createEntity(EntityManager em) {
        ProductInfo productInfo = new ProductInfo()
            .qtyInStock(DEFAULT_QTY_IN_STOCK)
            .isGlutenFree(DEFAULT_IS_GLUTEN_FREE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return productInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInfo createUpdatedEntity(EntityManager em) {
        ProductInfo productInfo = new ProductInfo()
            .qtyInStock(UPDATED_QTY_IN_STOCK)
            .isGlutenFree(UPDATED_IS_GLUTEN_FREE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return productInfo;
    }

    @BeforeEach
    public void initTest() {
        productInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createProductInfo() throws Exception {
        int databaseSizeBeforeCreate = productInfoRepository.findAll().size();
        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);
        restProductInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getQtyInStock()).isEqualTo(DEFAULT_QTY_IN_STOCK);
        assertThat(testProductInfo.getIsGlutenFree()).isEqualTo(DEFAULT_IS_GLUTEN_FREE);
        assertThat(testProductInfo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProductInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProductInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testProductInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createProductInfoWithExistingId() throws Exception {
        // Create the ProductInfo with an existing ID
        productInfoRepository.saveAndFlush(productInfo);
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        int databaseSizeBeforeCreate = productInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQtyInStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = productInfoRepository.findAll().size();
        // set the field null
        productInfo.setQtyInStock(null);

        // Create the ProductInfo, which fails.
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        restProductInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsGlutenFreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productInfoRepository.findAll().size();
        // set the field null
        productInfo.setIsGlutenFree(null);

        // Create the ProductInfo, which fails.
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        restProductInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductInfos() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].qtyInStock").value(hasItem(DEFAULT_QTY_IN_STOCK)))
            .andExpect(jsonPath("$.[*].isGlutenFree").value(hasItem(DEFAULT_IS_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get the productInfo
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, productInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productInfo.getId().toString()))
            .andExpect(jsonPath("$.qtyInStock").value(DEFAULT_QTY_IN_STOCK))
            .andExpect(jsonPath("$.isGlutenFree").value(DEFAULT_IS_GLUTEN_FREE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductInfosByIdFiltering() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        UUID id = productInfo.getId();

        defaultProductInfoShouldBeFound("id.equals=" + id);
        defaultProductInfoShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock equals to DEFAULT_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.equals=" + DEFAULT_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock equals to UPDATED_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.equals=" + UPDATED_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock in DEFAULT_QTY_IN_STOCK or UPDATED_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.in=" + DEFAULT_QTY_IN_STOCK + "," + UPDATED_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock equals to UPDATED_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.in=" + UPDATED_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock is not null
        defaultProductInfoShouldBeFound("qtyInStock.specified=true");

        // Get all the productInfoList where qtyInStock is null
        defaultProductInfoShouldNotBeFound("qtyInStock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock is greater than or equal to DEFAULT_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.greaterThanOrEqual=" + DEFAULT_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock is greater than or equal to UPDATED_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.greaterThanOrEqual=" + UPDATED_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock is less than or equal to DEFAULT_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.lessThanOrEqual=" + DEFAULT_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock is less than or equal to SMALLER_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.lessThanOrEqual=" + SMALLER_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock is less than DEFAULT_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.lessThan=" + DEFAULT_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock is less than UPDATED_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.lessThan=" + UPDATED_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByQtyInStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where qtyInStock is greater than DEFAULT_QTY_IN_STOCK
        defaultProductInfoShouldNotBeFound("qtyInStock.greaterThan=" + DEFAULT_QTY_IN_STOCK);

        // Get all the productInfoList where qtyInStock is greater than SMALLER_QTY_IN_STOCK
        defaultProductInfoShouldBeFound("qtyInStock.greaterThan=" + SMALLER_QTY_IN_STOCK);
    }

    @Test
    @Transactional
    void getAllProductInfosByIsGlutenFreeIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where isGlutenFree equals to DEFAULT_IS_GLUTEN_FREE
        defaultProductInfoShouldBeFound("isGlutenFree.equals=" + DEFAULT_IS_GLUTEN_FREE);

        // Get all the productInfoList where isGlutenFree equals to UPDATED_IS_GLUTEN_FREE
        defaultProductInfoShouldNotBeFound("isGlutenFree.equals=" + UPDATED_IS_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllProductInfosByIsGlutenFreeIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where isGlutenFree in DEFAULT_IS_GLUTEN_FREE or UPDATED_IS_GLUTEN_FREE
        defaultProductInfoShouldBeFound("isGlutenFree.in=" + DEFAULT_IS_GLUTEN_FREE + "," + UPDATED_IS_GLUTEN_FREE);

        // Get all the productInfoList where isGlutenFree equals to UPDATED_IS_GLUTEN_FREE
        defaultProductInfoShouldNotBeFound("isGlutenFree.in=" + UPDATED_IS_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllProductInfosByIsGlutenFreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where isGlutenFree is not null
        defaultProductInfoShouldBeFound("isGlutenFree.specified=true");

        // Get all the productInfoList where isGlutenFree is null
        defaultProductInfoShouldNotBeFound("isGlutenFree.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdBy equals to DEFAULT_CREATED_BY
        defaultProductInfoShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the productInfoList where createdBy equals to UPDATED_CREATED_BY
        defaultProductInfoShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProductInfoShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the productInfoList where createdBy equals to UPDATED_CREATED_BY
        defaultProductInfoShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdBy is not null
        defaultProductInfoShouldBeFound("createdBy.specified=true");

        // Get all the productInfoList where createdBy is null
        defaultProductInfoShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdBy contains DEFAULT_CREATED_BY
        defaultProductInfoShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the productInfoList where createdBy contains UPDATED_CREATED_BY
        defaultProductInfoShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdBy does not contain DEFAULT_CREATED_BY
        defaultProductInfoShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the productInfoList where createdBy does not contain UPDATED_CREATED_BY
        defaultProductInfoShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdDate equals to DEFAULT_CREATED_DATE
        defaultProductInfoShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the productInfoList where createdDate equals to UPDATED_CREATED_DATE
        defaultProductInfoShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultProductInfoShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the productInfoList where createdDate equals to UPDATED_CREATED_DATE
        defaultProductInfoShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProductInfosByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where createdDate is not null
        defaultProductInfoShouldBeFound("createdDate.specified=true");

        // Get all the productInfoList where createdDate is null
        defaultProductInfoShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultProductInfoShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productInfoList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductInfoShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultProductInfoShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the productInfoList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultProductInfoShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedBy is not null
        defaultProductInfoShouldBeFound("lastModifiedBy.specified=true");

        // Get all the productInfoList where lastModifiedBy is null
        defaultProductInfoShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultProductInfoShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productInfoList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultProductInfoShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultProductInfoShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the productInfoList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultProductInfoShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultProductInfoShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the productInfoList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProductInfoShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultProductInfoShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the productInfoList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultProductInfoShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllProductInfosByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList where lastModifiedDate is not null
        defaultProductInfoShouldBeFound("lastModifiedDate.specified=true");

        // Get all the productInfoList where lastModifiedDate is null
        defaultProductInfoShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInfosByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productInfoRepository.saveAndFlush(productInfo);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productInfo.setProduct(product);
        productInfoRepository.saveAndFlush(productInfo);
        UUID productId = product.getId();

        // Get all the productInfoList where product equals to productId
        defaultProductInfoShouldBeFound("productId.equals=" + productId);

        // Get all the productInfoList where product equals to UUID.randomUUID()
        defaultProductInfoShouldNotBeFound("productId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductInfoShouldBeFound(String filter) throws Exception {
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].qtyInStock").value(hasItem(DEFAULT_QTY_IN_STOCK)))
            .andExpect(jsonPath("$.[*].isGlutenFree").value(hasItem(DEFAULT_IS_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductInfoShouldNotBeFound(String filter) throws Exception {
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductInfo() throws Exception {
        // Get the productInfo
        restProductInfoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();

        // Update the productInfo
        ProductInfo updatedProductInfo = productInfoRepository.findById(productInfo.getId()).get();
        // Disconnect from session so that the updates on updatedProductInfo are not directly saved in db
        em.detach(updatedProductInfo);
        updatedProductInfo
            .qtyInStock(UPDATED_QTY_IN_STOCK)
            .isGlutenFree(UPDATED_IS_GLUTEN_FREE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(updatedProductInfo);

        restProductInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getQtyInStock()).isEqualTo(UPDATED_QTY_IN_STOCK);
        assertThat(testProductInfo.getIsGlutenFree()).isEqualTo(UPDATED_IS_GLUTEN_FREE);
        assertThat(testProductInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProductInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProductInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductInfoWithPatch() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();

        // Update the productInfo using partial update
        ProductInfo partialUpdatedProductInfo = new ProductInfo();
        partialUpdatedProductInfo.setId(productInfo.getId());

        partialUpdatedProductInfo.qtyInStock(UPDATED_QTY_IN_STOCK).createdBy(UPDATED_CREATED_BY).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restProductInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInfo))
            )
            .andExpect(status().isOk());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getQtyInStock()).isEqualTo(UPDATED_QTY_IN_STOCK);
        assertThat(testProductInfo.getIsGlutenFree()).isEqualTo(DEFAULT_IS_GLUTEN_FREE);
        assertThat(testProductInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProductInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProductInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateProductInfoWithPatch() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();

        // Update the productInfo using partial update
        ProductInfo partialUpdatedProductInfo = new ProductInfo();
        partialUpdatedProductInfo.setId(productInfo.getId());

        partialUpdatedProductInfo
            .qtyInStock(UPDATED_QTY_IN_STOCK)
            .isGlutenFree(UPDATED_IS_GLUTEN_FREE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProductInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInfo))
            )
            .andExpect(status().isOk());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getQtyInStock()).isEqualTo(UPDATED_QTY_IN_STOCK);
        assertThat(testProductInfo.getIsGlutenFree()).isEqualTo(UPDATED_IS_GLUTEN_FREE);
        assertThat(testProductInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProductInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testProductInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();
        productInfo.setId(UUID.randomUUID());

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeDelete = productInfoRepository.findAll().size();

        // Delete the productInfo
        restProductInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, productInfo.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

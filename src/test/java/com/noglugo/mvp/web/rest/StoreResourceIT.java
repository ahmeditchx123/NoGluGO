package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.repository.StoreRepository;
import com.noglugo.mvp.service.criteria.StoreCriteria;
import com.noglugo.mvp.service.dto.StoreDTO;
import com.noglugo.mvp.service.mapper.StoreMapper;
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
 * Integration tests for the {@link StoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMG_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEDICATED_GLUTEN_FREE = false;
    private static final Boolean UPDATED_IS_DEDICATED_GLUTEN_FREE = true;

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_DELIVERY_MODE = false;
    private static final Boolean UPDATED_HAS_DELIVERY_MODE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/stores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreMockMvc;

    private Store store;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createEntity(EntityManager em) {
        Store store = new Store()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .telephone(DEFAULT_TELEPHONE)
            .imgPath(DEFAULT_IMG_PATH)
            .isDedicatedGlutenFree(DEFAULT_IS_DEDICATED_GLUTEN_FREE)
            .website(DEFAULT_WEBSITE)
            .hasDeliveryMode(DEFAULT_HAS_DELIVERY_MODE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return store;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createUpdatedEntity(EntityManager em) {
        Store store = new Store()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .hasDeliveryMode(UPDATED_HAS_DELIVERY_MODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return store;
    }

    @BeforeEach
    public void initTest() {
        store = createEntity(em);
    }

    @Test
    @Transactional
    void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();
        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);
        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStore.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStore.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testStore.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testStore.getIsDedicatedGlutenFree()).isEqualTo(DEFAULT_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testStore.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testStore.getHasDeliveryMode()).isEqualTo(DEFAULT_HAS_DELIVERY_MODE);
        assertThat(testStore.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStore.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStore.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStore.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStoreWithExistingId() throws Exception {
        // Create the Store with an existing ID
        storeRepository.saveAndFlush(store);
        StoreDTO storeDTO = storeMapper.toDto(store);

        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setName(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setTelephone(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImgPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setImgPath(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDedicatedGlutenFreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setIsDedicatedGlutenFree(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasDeliveryModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setHasDeliveryMode(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList
        restStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].isDedicatedGlutenFree").value(hasItem(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].hasDeliveryMode").value(hasItem(DEFAULT_HAS_DELIVERY_MODE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get the store
        restStoreMockMvc
            .perform(get(ENTITY_API_URL_ID, store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH))
            .andExpect(jsonPath("$.isDedicatedGlutenFree").value(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.hasDeliveryMode").value(DEFAULT_HAS_DELIVERY_MODE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getStoresByIdFiltering() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        UUID id = store.getId();

        defaultStoreShouldBeFound("id.equals=" + id);
        defaultStoreShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllStoresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name equals to DEFAULT_NAME
        defaultStoreShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the storeList where name equals to UPDATED_NAME
        defaultStoreShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStoreShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the storeList where name equals to UPDATED_NAME
        defaultStoreShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name is not null
        defaultStoreShouldBeFound("name.specified=true");

        // Get all the storeList where name is null
        defaultStoreShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByNameContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name contains DEFAULT_NAME
        defaultStoreShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the storeList where name contains UPDATED_NAME
        defaultStoreShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name does not contain DEFAULT_NAME
        defaultStoreShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the storeList where name does not contain UPDATED_NAME
        defaultStoreShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStoresByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where description equals to DEFAULT_DESCRIPTION
        defaultStoreShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the storeList where description equals to UPDATED_DESCRIPTION
        defaultStoreShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStoresByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStoreShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the storeList where description equals to UPDATED_DESCRIPTION
        defaultStoreShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStoresByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where description is not null
        defaultStoreShouldBeFound("description.specified=true");

        // Get all the storeList where description is null
        defaultStoreShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where description contains DEFAULT_DESCRIPTION
        defaultStoreShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the storeList where description contains UPDATED_DESCRIPTION
        defaultStoreShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStoresByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where description does not contain DEFAULT_DESCRIPTION
        defaultStoreShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the storeList where description does not contain UPDATED_DESCRIPTION
        defaultStoreShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStoresByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where telephone equals to DEFAULT_TELEPHONE
        defaultStoreShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the storeList where telephone equals to UPDATED_TELEPHONE
        defaultStoreShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllStoresByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultStoreShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the storeList where telephone equals to UPDATED_TELEPHONE
        defaultStoreShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllStoresByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where telephone is not null
        defaultStoreShouldBeFound("telephone.specified=true");

        // Get all the storeList where telephone is null
        defaultStoreShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where telephone contains DEFAULT_TELEPHONE
        defaultStoreShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the storeList where telephone contains UPDATED_TELEPHONE
        defaultStoreShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllStoresByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where telephone does not contain DEFAULT_TELEPHONE
        defaultStoreShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the storeList where telephone does not contain UPDATED_TELEPHONE
        defaultStoreShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllStoresByImgPathIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where imgPath equals to DEFAULT_IMG_PATH
        defaultStoreShouldBeFound("imgPath.equals=" + DEFAULT_IMG_PATH);

        // Get all the storeList where imgPath equals to UPDATED_IMG_PATH
        defaultStoreShouldNotBeFound("imgPath.equals=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllStoresByImgPathIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where imgPath in DEFAULT_IMG_PATH or UPDATED_IMG_PATH
        defaultStoreShouldBeFound("imgPath.in=" + DEFAULT_IMG_PATH + "," + UPDATED_IMG_PATH);

        // Get all the storeList where imgPath equals to UPDATED_IMG_PATH
        defaultStoreShouldNotBeFound("imgPath.in=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllStoresByImgPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where imgPath is not null
        defaultStoreShouldBeFound("imgPath.specified=true");

        // Get all the storeList where imgPath is null
        defaultStoreShouldNotBeFound("imgPath.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByImgPathContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where imgPath contains DEFAULT_IMG_PATH
        defaultStoreShouldBeFound("imgPath.contains=" + DEFAULT_IMG_PATH);

        // Get all the storeList where imgPath contains UPDATED_IMG_PATH
        defaultStoreShouldNotBeFound("imgPath.contains=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllStoresByImgPathNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where imgPath does not contain DEFAULT_IMG_PATH
        defaultStoreShouldNotBeFound("imgPath.doesNotContain=" + DEFAULT_IMG_PATH);

        // Get all the storeList where imgPath does not contain UPDATED_IMG_PATH
        defaultStoreShouldBeFound("imgPath.doesNotContain=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllStoresByIsDedicatedGlutenFreeIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where isDedicatedGlutenFree equals to DEFAULT_IS_DEDICATED_GLUTEN_FREE
        defaultStoreShouldBeFound("isDedicatedGlutenFree.equals=" + DEFAULT_IS_DEDICATED_GLUTEN_FREE);

        // Get all the storeList where isDedicatedGlutenFree equals to UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultStoreShouldNotBeFound("isDedicatedGlutenFree.equals=" + UPDATED_IS_DEDICATED_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllStoresByIsDedicatedGlutenFreeIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where isDedicatedGlutenFree in DEFAULT_IS_DEDICATED_GLUTEN_FREE or UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultStoreShouldBeFound("isDedicatedGlutenFree.in=" + DEFAULT_IS_DEDICATED_GLUTEN_FREE + "," + UPDATED_IS_DEDICATED_GLUTEN_FREE);

        // Get all the storeList where isDedicatedGlutenFree equals to UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultStoreShouldNotBeFound("isDedicatedGlutenFree.in=" + UPDATED_IS_DEDICATED_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllStoresByIsDedicatedGlutenFreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where isDedicatedGlutenFree is not null
        defaultStoreShouldBeFound("isDedicatedGlutenFree.specified=true");

        // Get all the storeList where isDedicatedGlutenFree is null
        defaultStoreShouldNotBeFound("isDedicatedGlutenFree.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where website equals to DEFAULT_WEBSITE
        defaultStoreShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the storeList where website equals to UPDATED_WEBSITE
        defaultStoreShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStoresByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultStoreShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the storeList where website equals to UPDATED_WEBSITE
        defaultStoreShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStoresByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where website is not null
        defaultStoreShouldBeFound("website.specified=true");

        // Get all the storeList where website is null
        defaultStoreShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where website contains DEFAULT_WEBSITE
        defaultStoreShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the storeList where website contains UPDATED_WEBSITE
        defaultStoreShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStoresByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where website does not contain DEFAULT_WEBSITE
        defaultStoreShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the storeList where website does not contain UPDATED_WEBSITE
        defaultStoreShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllStoresByHasDeliveryModeIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where hasDeliveryMode equals to DEFAULT_HAS_DELIVERY_MODE
        defaultStoreShouldBeFound("hasDeliveryMode.equals=" + DEFAULT_HAS_DELIVERY_MODE);

        // Get all the storeList where hasDeliveryMode equals to UPDATED_HAS_DELIVERY_MODE
        defaultStoreShouldNotBeFound("hasDeliveryMode.equals=" + UPDATED_HAS_DELIVERY_MODE);
    }

    @Test
    @Transactional
    void getAllStoresByHasDeliveryModeIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where hasDeliveryMode in DEFAULT_HAS_DELIVERY_MODE or UPDATED_HAS_DELIVERY_MODE
        defaultStoreShouldBeFound("hasDeliveryMode.in=" + DEFAULT_HAS_DELIVERY_MODE + "," + UPDATED_HAS_DELIVERY_MODE);

        // Get all the storeList where hasDeliveryMode equals to UPDATED_HAS_DELIVERY_MODE
        defaultStoreShouldNotBeFound("hasDeliveryMode.in=" + UPDATED_HAS_DELIVERY_MODE);
    }

    @Test
    @Transactional
    void getAllStoresByHasDeliveryModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where hasDeliveryMode is not null
        defaultStoreShouldBeFound("hasDeliveryMode.specified=true");

        // Get all the storeList where hasDeliveryMode is null
        defaultStoreShouldNotBeFound("hasDeliveryMode.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdBy equals to DEFAULT_CREATED_BY
        defaultStoreShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the storeList where createdBy equals to UPDATED_CREATED_BY
        defaultStoreShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultStoreShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the storeList where createdBy equals to UPDATED_CREATED_BY
        defaultStoreShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdBy is not null
        defaultStoreShouldBeFound("createdBy.specified=true");

        // Get all the storeList where createdBy is null
        defaultStoreShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdBy contains DEFAULT_CREATED_BY
        defaultStoreShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the storeList where createdBy contains UPDATED_CREATED_BY
        defaultStoreShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdBy does not contain DEFAULT_CREATED_BY
        defaultStoreShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the storeList where createdBy does not contain UPDATED_CREATED_BY
        defaultStoreShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdDate equals to DEFAULT_CREATED_DATE
        defaultStoreShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the storeList where createdDate equals to UPDATED_CREATED_DATE
        defaultStoreShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultStoreShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the storeList where createdDate equals to UPDATED_CREATED_DATE
        defaultStoreShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllStoresByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where createdDate is not null
        defaultStoreShouldBeFound("createdDate.specified=true");

        // Get all the storeList where createdDate is null
        defaultStoreShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultStoreShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the storeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultStoreShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultStoreShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the storeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultStoreShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedBy is not null
        defaultStoreShouldBeFound("lastModifiedBy.specified=true");

        // Get all the storeList where lastModifiedBy is null
        defaultStoreShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultStoreShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the storeList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultStoreShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultStoreShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the storeList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultStoreShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultStoreShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the storeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultStoreShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultStoreShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the storeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultStoreShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllStoresByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where lastModifiedDate is not null
        defaultStoreShouldBeFound("lastModifiedDate.specified=true");

        // Get all the storeList where lastModifiedDate is null
        defaultStoreShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStoresByStoreAddressIsEqualToSomething() throws Exception {
        Address storeAddress;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            storeRepository.saveAndFlush(store);
            storeAddress = AddressResourceIT.createEntity(em);
        } else {
            storeAddress = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(storeAddress);
        em.flush();
        store.setStoreAddress(storeAddress);
        storeAddress.setStore(store);
        storeRepository.saveAndFlush(store);
        UUID storeAddressId = storeAddress.getId();

        // Get all the storeList where storeAddress equals to storeAddressId
        defaultStoreShouldBeFound("storeAddressId.equals=" + storeAddressId);

        // Get all the storeList where storeAddress equals to UUID.randomUUID()
        defaultStoreShouldNotBeFound("storeAddressId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllStoresByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            storeRepository.saveAndFlush(store);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        store.addProducts(products);
        storeRepository.saveAndFlush(store);
        UUID productsId = products.getId();

        // Get all the storeList where products equals to productsId
        defaultStoreShouldBeFound("productsId.equals=" + productsId);

        // Get all the storeList where products equals to UUID.randomUUID()
        defaultStoreShouldNotBeFound("productsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStoreShouldBeFound(String filter) throws Exception {
        restStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].isDedicatedGlutenFree").value(hasItem(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].hasDeliveryMode").value(hasItem(DEFAULT_HAS_DELIVERY_MODE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStoreShouldNotBeFound(String filter) throws Exception {
        restStoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findById(store.getId()).get();
        // Disconnect from session so that the updates on updatedStore are not directly saved in db
        em.detach(updatedStore);
        updatedStore
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .hasDeliveryMode(UPDATED_HAS_DELIVERY_MODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        StoreDTO storeDTO = storeMapper.toDto(updatedStore);

        restStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStore.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testStore.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testStore.getIsDedicatedGlutenFree()).isEqualTo(UPDATED_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testStore.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testStore.getHasDeliveryMode()).isEqualTo(UPDATED_HAS_DELIVERY_MODE);
        assertThat(testStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStore.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStore.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStore.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStoreWithPatch() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store using partial update
        Store partialUpdatedStore = new Store();
        partialUpdatedStore.setId(store.getId());

        partialUpdatedStore
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .hasDeliveryMode(UPDATED_HAS_DELIVERY_MODE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStore))
            )
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStore.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testStore.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testStore.getIsDedicatedGlutenFree()).isEqualTo(DEFAULT_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testStore.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testStore.getHasDeliveryMode()).isEqualTo(UPDATED_HAS_DELIVERY_MODE);
        assertThat(testStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStore.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testStore.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testStore.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStoreWithPatch() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store using partial update
        Store partialUpdatedStore = new Store();
        partialUpdatedStore.setId(store.getId());

        partialUpdatedStore
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .hasDeliveryMode(UPDATED_HAS_DELIVERY_MODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStore))
            )
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStore.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStore.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testStore.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testStore.getIsDedicatedGlutenFree()).isEqualTo(UPDATED_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testStore.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testStore.getHasDeliveryMode()).isEqualTo(UPDATED_HAS_DELIVERY_MODE);
        assertThat(testStore.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStore.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testStore.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testStore.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();
        store.setId(UUID.randomUUID());

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Delete the store
        restStoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, store.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

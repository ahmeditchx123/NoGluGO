package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.repository.RestaurantRepository;
import com.noglugo.mvp.service.criteria.RestaurantCriteria;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import com.noglugo.mvp.service.mapper.RestaurantMapper;
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
 * Integration tests for the {@link RestaurantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantResourceIT {

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

    private static final Integer DEFAULT_TABLE_NUMBER = 1;
    private static final Integer UPDATED_TABLE_NUMBER = 2;
    private static final Integer SMALLER_TABLE_NUMBER = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .telephone(DEFAULT_TELEPHONE)
            .imgPath(DEFAULT_IMG_PATH)
            .isDedicatedGlutenFree(DEFAULT_IS_DEDICATED_GLUTEN_FREE)
            .website(DEFAULT_WEBSITE)
            .tableNumber(DEFAULT_TABLE_NUMBER)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return restaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createUpdatedEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .tableNumber(UPDATED_TABLE_NUMBER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return restaurant;
    }

    @BeforeEach
    public void initTest() {
        restaurant = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurant() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();
        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);
        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isCreated());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRestaurant.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testRestaurant.getIsDedicatedGlutenFree()).isEqualTo(DEFAULT_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testRestaurant.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testRestaurant.getTableNumber()).isEqualTo(DEFAULT_TABLE_NUMBER);
        assertThat(testRestaurant.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRestaurant.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testRestaurant.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testRestaurant.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createRestaurantWithExistingId() throws Exception {
        // Create the Restaurant with an existing ID
        restaurantRepository.saveAndFlush(restaurant);
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setName(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setTelephone(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImgPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setImgPath(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDedicatedGlutenFreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setIsDedicatedGlutenFree(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().size();
        // set the field null
        restaurant.setTableNumber(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isBadRequest());

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].isDedicatedGlutenFree").value(hasItem(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurant.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH))
            .andExpect(jsonPath("$.isDedicatedGlutenFree").value(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.tableNumber").value(DEFAULT_TABLE_NUMBER))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getRestaurantsByIdFiltering() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        UUID id = restaurant.getId();

        defaultRestaurantShouldBeFound("id.equals=" + id);
        defaultRestaurantShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name equals to DEFAULT_NAME
        defaultRestaurantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the restaurantList where name equals to UPDATED_NAME
        defaultRestaurantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRestaurantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the restaurantList where name equals to UPDATED_NAME
        defaultRestaurantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name is not null
        defaultRestaurantShouldBeFound("name.specified=true");

        // Get all the restaurantList where name is null
        defaultRestaurantShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name contains DEFAULT_NAME
        defaultRestaurantShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the restaurantList where name contains UPDATED_NAME
        defaultRestaurantShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name does not contain DEFAULT_NAME
        defaultRestaurantShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the restaurantList where name does not contain UPDATED_NAME
        defaultRestaurantShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where description equals to DEFAULT_DESCRIPTION
        defaultRestaurantShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantList where description equals to UPDATED_DESCRIPTION
        defaultRestaurantShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRestaurantShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the restaurantList where description equals to UPDATED_DESCRIPTION
        defaultRestaurantShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where description is not null
        defaultRestaurantShouldBeFound("description.specified=true");

        // Get all the restaurantList where description is null
        defaultRestaurantShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where description contains DEFAULT_DESCRIPTION
        defaultRestaurantShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantList where description contains UPDATED_DESCRIPTION
        defaultRestaurantShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where description does not contain DEFAULT_DESCRIPTION
        defaultRestaurantShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the restaurantList where description does not contain UPDATED_DESCRIPTION
        defaultRestaurantShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where telephone equals to DEFAULT_TELEPHONE
        defaultRestaurantShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the restaurantList where telephone equals to UPDATED_TELEPHONE
        defaultRestaurantShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultRestaurantShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the restaurantList where telephone equals to UPDATED_TELEPHONE
        defaultRestaurantShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where telephone is not null
        defaultRestaurantShouldBeFound("telephone.specified=true");

        // Get all the restaurantList where telephone is null
        defaultRestaurantShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where telephone contains DEFAULT_TELEPHONE
        defaultRestaurantShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the restaurantList where telephone contains UPDATED_TELEPHONE
        defaultRestaurantShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where telephone does not contain DEFAULT_TELEPHONE
        defaultRestaurantShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the restaurantList where telephone does not contain UPDATED_TELEPHONE
        defaultRestaurantShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByImgPathIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where imgPath equals to DEFAULT_IMG_PATH
        defaultRestaurantShouldBeFound("imgPath.equals=" + DEFAULT_IMG_PATH);

        // Get all the restaurantList where imgPath equals to UPDATED_IMG_PATH
        defaultRestaurantShouldNotBeFound("imgPath.equals=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllRestaurantsByImgPathIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where imgPath in DEFAULT_IMG_PATH or UPDATED_IMG_PATH
        defaultRestaurantShouldBeFound("imgPath.in=" + DEFAULT_IMG_PATH + "," + UPDATED_IMG_PATH);

        // Get all the restaurantList where imgPath equals to UPDATED_IMG_PATH
        defaultRestaurantShouldNotBeFound("imgPath.in=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllRestaurantsByImgPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where imgPath is not null
        defaultRestaurantShouldBeFound("imgPath.specified=true");

        // Get all the restaurantList where imgPath is null
        defaultRestaurantShouldNotBeFound("imgPath.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByImgPathContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where imgPath contains DEFAULT_IMG_PATH
        defaultRestaurantShouldBeFound("imgPath.contains=" + DEFAULT_IMG_PATH);

        // Get all the restaurantList where imgPath contains UPDATED_IMG_PATH
        defaultRestaurantShouldNotBeFound("imgPath.contains=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllRestaurantsByImgPathNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where imgPath does not contain DEFAULT_IMG_PATH
        defaultRestaurantShouldNotBeFound("imgPath.doesNotContain=" + DEFAULT_IMG_PATH);

        // Get all the restaurantList where imgPath does not contain UPDATED_IMG_PATH
        defaultRestaurantShouldBeFound("imgPath.doesNotContain=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllRestaurantsByIsDedicatedGlutenFreeIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where isDedicatedGlutenFree equals to DEFAULT_IS_DEDICATED_GLUTEN_FREE
        defaultRestaurantShouldBeFound("isDedicatedGlutenFree.equals=" + DEFAULT_IS_DEDICATED_GLUTEN_FREE);

        // Get all the restaurantList where isDedicatedGlutenFree equals to UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultRestaurantShouldNotBeFound("isDedicatedGlutenFree.equals=" + UPDATED_IS_DEDICATED_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByIsDedicatedGlutenFreeIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where isDedicatedGlutenFree in DEFAULT_IS_DEDICATED_GLUTEN_FREE or UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultRestaurantShouldBeFound(
            "isDedicatedGlutenFree.in=" + DEFAULT_IS_DEDICATED_GLUTEN_FREE + "," + UPDATED_IS_DEDICATED_GLUTEN_FREE
        );

        // Get all the restaurantList where isDedicatedGlutenFree equals to UPDATED_IS_DEDICATED_GLUTEN_FREE
        defaultRestaurantShouldNotBeFound("isDedicatedGlutenFree.in=" + UPDATED_IS_DEDICATED_GLUTEN_FREE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByIsDedicatedGlutenFreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where isDedicatedGlutenFree is not null
        defaultRestaurantShouldBeFound("isDedicatedGlutenFree.specified=true");

        // Get all the restaurantList where isDedicatedGlutenFree is null
        defaultRestaurantShouldNotBeFound("isDedicatedGlutenFree.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where website equals to DEFAULT_WEBSITE
        defaultRestaurantShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the restaurantList where website equals to UPDATED_WEBSITE
        defaultRestaurantShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultRestaurantShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the restaurantList where website equals to UPDATED_WEBSITE
        defaultRestaurantShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where website is not null
        defaultRestaurantShouldBeFound("website.specified=true");

        // Get all the restaurantList where website is null
        defaultRestaurantShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where website contains DEFAULT_WEBSITE
        defaultRestaurantShouldBeFound("website.contains=" + DEFAULT_WEBSITE);

        // Get all the restaurantList where website contains UPDATED_WEBSITE
        defaultRestaurantShouldNotBeFound("website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where website does not contain DEFAULT_WEBSITE
        defaultRestaurantShouldNotBeFound("website.doesNotContain=" + DEFAULT_WEBSITE);

        // Get all the restaurantList where website does not contain UPDATED_WEBSITE
        defaultRestaurantShouldBeFound("website.doesNotContain=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber equals to DEFAULT_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.equals=" + DEFAULT_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber equals to UPDATED_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.equals=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber in DEFAULT_TABLE_NUMBER or UPDATED_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.in=" + DEFAULT_TABLE_NUMBER + "," + UPDATED_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber equals to UPDATED_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.in=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber is not null
        defaultRestaurantShouldBeFound("tableNumber.specified=true");

        // Get all the restaurantList where tableNumber is null
        defaultRestaurantShouldNotBeFound("tableNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber is greater than or equal to DEFAULT_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.greaterThanOrEqual=" + DEFAULT_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber is greater than or equal to UPDATED_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.greaterThanOrEqual=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber is less than or equal to DEFAULT_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.lessThanOrEqual=" + DEFAULT_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber is less than or equal to SMALLER_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.lessThanOrEqual=" + SMALLER_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber is less than DEFAULT_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.lessThan=" + DEFAULT_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber is less than UPDATED_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.lessThan=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByTableNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where tableNumber is greater than DEFAULT_TABLE_NUMBER
        defaultRestaurantShouldNotBeFound("tableNumber.greaterThan=" + DEFAULT_TABLE_NUMBER);

        // Get all the restaurantList where tableNumber is greater than SMALLER_TABLE_NUMBER
        defaultRestaurantShouldBeFound("tableNumber.greaterThan=" + SMALLER_TABLE_NUMBER);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdBy equals to DEFAULT_CREATED_BY
        defaultRestaurantShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the restaurantList where createdBy equals to UPDATED_CREATED_BY
        defaultRestaurantShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultRestaurantShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the restaurantList where createdBy equals to UPDATED_CREATED_BY
        defaultRestaurantShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdBy is not null
        defaultRestaurantShouldBeFound("createdBy.specified=true");

        // Get all the restaurantList where createdBy is null
        defaultRestaurantShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdBy contains DEFAULT_CREATED_BY
        defaultRestaurantShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the restaurantList where createdBy contains UPDATED_CREATED_BY
        defaultRestaurantShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdBy does not contain DEFAULT_CREATED_BY
        defaultRestaurantShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the restaurantList where createdBy does not contain UPDATED_CREATED_BY
        defaultRestaurantShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdDate equals to DEFAULT_CREATED_DATE
        defaultRestaurantShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the restaurantList where createdDate equals to UPDATED_CREATED_DATE
        defaultRestaurantShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultRestaurantShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the restaurantList where createdDate equals to UPDATED_CREATED_DATE
        defaultRestaurantShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where createdDate is not null
        defaultRestaurantShouldBeFound("createdDate.specified=true");

        // Get all the restaurantList where createdDate is null
        defaultRestaurantShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultRestaurantShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the restaurantList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultRestaurantShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultRestaurantShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the restaurantList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultRestaurantShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedBy is not null
        defaultRestaurantShouldBeFound("lastModifiedBy.specified=true");

        // Get all the restaurantList where lastModifiedBy is null
        defaultRestaurantShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultRestaurantShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the restaurantList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultRestaurantShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultRestaurantShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the restaurantList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultRestaurantShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultRestaurantShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the restaurantList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultRestaurantShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultRestaurantShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the restaurantList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultRestaurantShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRestaurantsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where lastModifiedDate is not null
        defaultRestaurantShouldBeFound("lastModifiedDate.specified=true");

        // Get all the restaurantList where lastModifiedDate is null
        defaultRestaurantShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurantsByRestaurantAddressIsEqualToSomething() throws Exception {
        Address restaurantAddress;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            restaurantRepository.saveAndFlush(restaurant);
            restaurantAddress = AddressResourceIT.createEntity(em);
        } else {
            restaurantAddress = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(restaurantAddress);
        em.flush();
        restaurant.setRestaurantAddress(restaurantAddress);
        restaurantAddress.setRestaurant(restaurant);
        restaurantRepository.saveAndFlush(restaurant);
        UUID restaurantAddressId = restaurantAddress.getId();

        // Get all the restaurantList where restaurantAddress equals to restaurantAddressId
        defaultRestaurantShouldBeFound("restaurantAddressId.equals=" + restaurantAddressId);

        // Get all the restaurantList where restaurantAddress equals to UUID.randomUUID()
        defaultRestaurantShouldNotBeFound("restaurantAddressId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllRestaurantsByRestaurantMenuIsEqualToSomething() throws Exception {
        Menu restaurantMenu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            restaurantRepository.saveAndFlush(restaurant);
            restaurantMenu = MenuResourceIT.createEntity(em);
        } else {
            restaurantMenu = TestUtil.findAll(em, Menu.class).get(0);
        }
        em.persist(restaurantMenu);
        em.flush();
        restaurant.setRestaurantMenu(restaurantMenu);
        restaurantMenu.setRestaurant(restaurant);
        restaurantRepository.saveAndFlush(restaurant);
        UUID restaurantMenuId = restaurantMenu.getId();

        // Get all the restaurantList where restaurantMenu equals to restaurantMenuId
        defaultRestaurantShouldBeFound("restaurantMenuId.equals=" + restaurantMenuId);

        // Get all the restaurantList where restaurantMenu equals to UUID.randomUUID()
        defaultRestaurantShouldNotBeFound("restaurantMenuId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRestaurantShouldBeFound(String filter) throws Exception {
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].isDedicatedGlutenFree").value(hasItem(DEFAULT_IS_DEDICATED_GLUTEN_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRestaurantShouldNotBeFound(String filter) throws Exception {
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurant are not directly saved in db
        em.detach(updatedRestaurant);
        updatedRestaurant
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .tableNumber(UPDATED_TABLE_NUMBER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(updatedRestaurant);

        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRestaurant.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testRestaurant.getIsDedicatedGlutenFree()).isEqualTo(UPDATED_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testRestaurant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testRestaurant.getTableNumber()).isEqualTo(UPDATED_TABLE_NUMBER);
        assertThat(testRestaurant.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRestaurant.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testRestaurant.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testRestaurant.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .website(UPDATED_WEBSITE)
            .tableNumber(UPDATED_TABLE_NUMBER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRestaurant.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testRestaurant.getIsDedicatedGlutenFree()).isEqualTo(DEFAULT_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testRestaurant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testRestaurant.getTableNumber()).isEqualTo(UPDATED_TABLE_NUMBER);
        assertThat(testRestaurant.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRestaurant.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testRestaurant.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testRestaurant.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .telephone(UPDATED_TELEPHONE)
            .imgPath(UPDATED_IMG_PATH)
            .isDedicatedGlutenFree(UPDATED_IS_DEDICATED_GLUTEN_FREE)
            .website(UPDATED_WEBSITE)
            .tableNumber(UPDATED_TABLE_NUMBER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRestaurant.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testRestaurant.getIsDedicatedGlutenFree()).isEqualTo(UPDATED_IS_DEDICATED_GLUTEN_FREE);
        assertThat(testRestaurant.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testRestaurant.getTableNumber()).isEqualTo(UPDATED_TABLE_NUMBER);
        assertThat(testRestaurant.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRestaurant.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testRestaurant.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testRestaurant.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();
        restaurant.setId(UUID.randomUUID());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        int databaseSizeBeforeDelete = restaurantRepository.findAll().size();

        // Delete the restaurant
        restRestaurantMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurant.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

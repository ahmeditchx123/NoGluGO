package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.domain.Review;
import com.noglugo.mvp.repository.MenuItemRepository;
import com.noglugo.mvp.service.criteria.MenuItemCriteria;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.service.mapper.MenuItemMapper;
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
 * Integration tests for the {@link MenuItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMG_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;
    private static final Double SMALLER_UNIT_PRICE = 1D - 1D;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/menu-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .imgPath(DEFAULT_IMG_PATH)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return menuItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createUpdatedEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return menuItem;
    }

    @BeforeEach
    public void initTest() {
        menuItem = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();
        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuItem.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMenuItem.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testMenuItem.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testMenuItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMenuItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMenuItem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMenuItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createMenuItemWithExistingId() throws Exception {
        // Create the MenuItem with an existing ID
        menuItemRepository.saveAndFlush(menuItem);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setContent(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImgPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setImgPath(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItem.setUnitPrice(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL_ID, menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMenuItemsByIdFiltering() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        UUID id = menuItem.getId();

        defaultMenuItemShouldBeFound("id.equals=" + id);
        defaultMenuItemShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllMenuItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name equals to DEFAULT_NAME
        defaultMenuItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuItemList where name equals to UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name is not null
        defaultMenuItemShouldBeFound("name.specified=true");

        // Get all the menuItemList where name is null
        defaultMenuItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByNameContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name contains DEFAULT_NAME
        defaultMenuItemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the menuItemList where name contains UPDATED_NAME
        defaultMenuItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuItemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where name does not contain DEFAULT_NAME
        defaultMenuItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the menuItemList where name does not contain UPDATED_NAME
        defaultMenuItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuItemsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where content equals to DEFAULT_CONTENT
        defaultMenuItemShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the menuItemList where content equals to UPDATED_CONTENT
        defaultMenuItemShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllMenuItemsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultMenuItemShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the menuItemList where content equals to UPDATED_CONTENT
        defaultMenuItemShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllMenuItemsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where content is not null
        defaultMenuItemShouldBeFound("content.specified=true");

        // Get all the menuItemList where content is null
        defaultMenuItemShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByContentContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where content contains DEFAULT_CONTENT
        defaultMenuItemShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the menuItemList where content contains UPDATED_CONTENT
        defaultMenuItemShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllMenuItemsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where content does not contain DEFAULT_CONTENT
        defaultMenuItemShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the menuItemList where content does not contain UPDATED_CONTENT
        defaultMenuItemShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllMenuItemsByImgPathIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imgPath equals to DEFAULT_IMG_PATH
        defaultMenuItemShouldBeFound("imgPath.equals=" + DEFAULT_IMG_PATH);

        // Get all the menuItemList where imgPath equals to UPDATED_IMG_PATH
        defaultMenuItemShouldNotBeFound("imgPath.equals=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllMenuItemsByImgPathIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imgPath in DEFAULT_IMG_PATH or UPDATED_IMG_PATH
        defaultMenuItemShouldBeFound("imgPath.in=" + DEFAULT_IMG_PATH + "," + UPDATED_IMG_PATH);

        // Get all the menuItemList where imgPath equals to UPDATED_IMG_PATH
        defaultMenuItemShouldNotBeFound("imgPath.in=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllMenuItemsByImgPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imgPath is not null
        defaultMenuItemShouldBeFound("imgPath.specified=true");

        // Get all the menuItemList where imgPath is null
        defaultMenuItemShouldNotBeFound("imgPath.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByImgPathContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imgPath contains DEFAULT_IMG_PATH
        defaultMenuItemShouldBeFound("imgPath.contains=" + DEFAULT_IMG_PATH);

        // Get all the menuItemList where imgPath contains UPDATED_IMG_PATH
        defaultMenuItemShouldNotBeFound("imgPath.contains=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllMenuItemsByImgPathNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where imgPath does not contain DEFAULT_IMG_PATH
        defaultMenuItemShouldNotBeFound("imgPath.doesNotContain=" + DEFAULT_IMG_PATH);

        // Get all the menuItemList where imgPath does not contain UPDATED_IMG_PATH
        defaultMenuItemShouldBeFound("imgPath.doesNotContain=" + UPDATED_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the menuItemList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the menuItemList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice is not null
        defaultMenuItemShouldBeFound("unitPrice.specified=true");

        // Get all the menuItemList where unitPrice is null
        defaultMenuItemShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice is greater than or equal to DEFAULT_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the menuItemList where unitPrice is greater than or equal to UPDATED_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice is less than or equal to DEFAULT_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the menuItemList where unitPrice is less than or equal to SMALLER_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice is less than DEFAULT_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);

        // Get all the menuItemList where unitPrice is less than UPDATED_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.lessThan=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where unitPrice is greater than DEFAULT_UNIT_PRICE
        defaultMenuItemShouldNotBeFound("unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);

        // Get all the menuItemList where unitPrice is greater than SMALLER_UNIT_PRICE
        defaultMenuItemShouldBeFound("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdBy equals to DEFAULT_CREATED_BY
        defaultMenuItemShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the menuItemList where createdBy equals to UPDATED_CREATED_BY
        defaultMenuItemShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultMenuItemShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the menuItemList where createdBy equals to UPDATED_CREATED_BY
        defaultMenuItemShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdBy is not null
        defaultMenuItemShouldBeFound("createdBy.specified=true");

        // Get all the menuItemList where createdBy is null
        defaultMenuItemShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdBy contains DEFAULT_CREATED_BY
        defaultMenuItemShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the menuItemList where createdBy contains UPDATED_CREATED_BY
        defaultMenuItemShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdBy does not contain DEFAULT_CREATED_BY
        defaultMenuItemShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the menuItemList where createdBy does not contain UPDATED_CREATED_BY
        defaultMenuItemShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMenuItemShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the menuItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuItemShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMenuItemShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the menuItemList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuItemShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where createdDate is not null
        defaultMenuItemShouldBeFound("createdDate.specified=true");

        // Get all the menuItemList where createdDate is null
        defaultMenuItemShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultMenuItemShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMenuItemShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultMenuItemShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the menuItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMenuItemShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedBy is not null
        defaultMenuItemShouldBeFound("lastModifiedBy.specified=true");

        // Get all the menuItemList where lastModifiedBy is null
        defaultMenuItemShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultMenuItemShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuItemList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultMenuItemShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultMenuItemShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuItemList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultMenuItemShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultMenuItemShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the menuItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultMenuItemShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultMenuItemShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the menuItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultMenuItemShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList where lastModifiedDate is not null
        defaultMenuItemShouldBeFound("lastModifiedDate.specified=true");

        // Get all the menuItemList where lastModifiedDate is null
        defaultMenuItemShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByMenuIsEqualToSomething() throws Exception {
        Menu menu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            menuItemRepository.saveAndFlush(menuItem);
            menu = MenuResourceIT.createEntity(em);
        } else {
            menu = TestUtil.findAll(em, Menu.class).get(0);
        }
        em.persist(menu);
        em.flush();
        menuItem.setMenu(menu);
        menuItemRepository.saveAndFlush(menuItem);
        UUID menuId = menu.getId();

        // Get all the menuItemList where menu equals to menuId
        defaultMenuItemShouldBeFound("menuId.equals=" + menuId);

        // Get all the menuItemList where menu equals to UUID.randomUUID()
        defaultMenuItemShouldNotBeFound("menuId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllMenuItemsByReviewsIsEqualToSomething() throws Exception {
        Review reviews;
        if (TestUtil.findAll(em, Review.class).isEmpty()) {
            menuItemRepository.saveAndFlush(menuItem);
            reviews = ReviewResourceIT.createEntity(em);
        } else {
            reviews = TestUtil.findAll(em, Review.class).get(0);
        }
        em.persist(reviews);
        em.flush();
        menuItem.addReviews(reviews);
        menuItemRepository.saveAndFlush(menuItem);
        UUID reviewsId = reviews.getId();

        // Get all the menuItemList where reviews equals to reviewsId
        defaultMenuItemShouldBeFound("reviewsId.equals=" + reviewsId);

        // Get all the menuItemList where reviews equals to UUID.randomUUID()
        defaultMenuItemShouldNotBeFound("reviewsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuItemShouldBeFound(String filter) throws Exception {
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuItemShouldNotBeFound(String filter) throws Exception {
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(updatedMenuItem);

        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMenuItem.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testMenuItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testMenuItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMenuItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMenuItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenuItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem.name(UPDATED_NAME).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMenuItem.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testMenuItem.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testMenuItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMenuItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMenuItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenuItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .imgPath(UPDATED_IMG_PATH)
            .unitPrice(UPDATED_UNIT_PRICE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItem testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMenuItem.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testMenuItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testMenuItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMenuItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMenuItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenuItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItem.setId(UUID.randomUUID());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItem);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Delete the menuItem
        restMenuItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuItem.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

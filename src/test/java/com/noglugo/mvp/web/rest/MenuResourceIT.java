package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.repository.MenuRepository;
import com.noglugo.mvp.service.criteria.MenuCriteria;
import com.noglugo.mvp.service.dto.MenuDTO;
import com.noglugo.mvp.service.mapper.MenuMapper;
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
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuMockMvc;

    private Menu menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity(EntityManager em) {
        Menu menu = new Menu()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return menu;
    }

    @BeforeEach
    public void initTest() {
        menu = createEntity(em);
    }

    @Test
    @Transactional
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();
        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenu.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMenu.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menuRepository.saveAndFlush(menu);
        MenuDTO menuDTO = menuMapper.toDto(menu);

        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setName(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMenusByIdFiltering() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        UUID id = menu.getId();

        defaultMenuShouldBeFound("id.equals=" + id);
        defaultMenuShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where name equals to DEFAULT_NAME
        defaultMenuShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuList where name equals to UPDATED_NAME
        defaultMenuShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuList where name equals to UPDATED_NAME
        defaultMenuShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where name is not null
        defaultMenuShouldBeFound("name.specified=true");

        // Get all the menuList where name is null
        defaultMenuShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByNameContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where name contains DEFAULT_NAME
        defaultMenuShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the menuList where name contains UPDATED_NAME
        defaultMenuShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameNotContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where name does not contain DEFAULT_NAME
        defaultMenuShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the menuList where name does not contain UPDATED_NAME
        defaultMenuShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where description equals to DEFAULT_DESCRIPTION
        defaultMenuShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the menuList where description equals to UPDATED_DESCRIPTION
        defaultMenuShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenusByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMenuShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the menuList where description equals to UPDATED_DESCRIPTION
        defaultMenuShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenusByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where description is not null
        defaultMenuShouldBeFound("description.specified=true");

        // Get all the menuList where description is null
        defaultMenuShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where description contains DEFAULT_DESCRIPTION
        defaultMenuShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the menuList where description contains UPDATED_DESCRIPTION
        defaultMenuShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenusByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where description does not contain DEFAULT_DESCRIPTION
        defaultMenuShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the menuList where description does not contain UPDATED_DESCRIPTION
        defaultMenuShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdBy equals to DEFAULT_CREATED_BY
        defaultMenuShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the menuList where createdBy equals to UPDATED_CREATED_BY
        defaultMenuShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultMenuShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the menuList where createdBy equals to UPDATED_CREATED_BY
        defaultMenuShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdBy is not null
        defaultMenuShouldBeFound("createdBy.specified=true");

        // Get all the menuList where createdBy is null
        defaultMenuShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdBy contains DEFAULT_CREATED_BY
        defaultMenuShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the menuList where createdBy contains UPDATED_CREATED_BY
        defaultMenuShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdBy does not contain DEFAULT_CREATED_BY
        defaultMenuShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the menuList where createdBy does not contain UPDATED_CREATED_BY
        defaultMenuShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMenuShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the menuList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMenuShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the menuList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where createdDate is not null
        defaultMenuShouldBeFound("createdDate.specified=true");

        // Get all the menuList where createdDate is null
        defaultMenuShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultMenuShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMenuShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultMenuShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the menuList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultMenuShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedBy is not null
        defaultMenuShouldBeFound("lastModifiedBy.specified=true");

        // Get all the menuList where lastModifiedBy is null
        defaultMenuShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultMenuShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultMenuShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultMenuShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the menuList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultMenuShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultMenuShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the menuList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultMenuShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultMenuShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the menuList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultMenuShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList where lastModifiedDate is not null
        defaultMenuShouldBeFound("lastModifiedDate.specified=true");

        // Get all the menuList where lastModifiedDate is null
        defaultMenuShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByRestaurantIsEqualToSomething() throws Exception {
        Restaurant restaurant;
        if (TestUtil.findAll(em, Restaurant.class).isEmpty()) {
            menuRepository.saveAndFlush(menu);
            restaurant = RestaurantResourceIT.createEntity(em);
        } else {
            restaurant = TestUtil.findAll(em, Restaurant.class).get(0);
        }
        em.persist(restaurant);
        em.flush();
        menu.setRestaurant(restaurant);
        menuRepository.saveAndFlush(menu);
        UUID restaurantId = restaurant.getId();

        // Get all the menuList where restaurant equals to restaurantId
        defaultMenuShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the menuList where restaurant equals to UUID.randomUUID()
        defaultMenuShouldNotBeFound("restaurantId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllMenusByMenuItemsIsEqualToSomething() throws Exception {
        MenuItem menuItems;
        if (TestUtil.findAll(em, MenuItem.class).isEmpty()) {
            menuRepository.saveAndFlush(menu);
            menuItems = MenuItemResourceIT.createEntity(em);
        } else {
            menuItems = TestUtil.findAll(em, MenuItem.class).get(0);
        }
        em.persist(menuItems);
        em.flush();
        menu.addMenuItems(menuItems);
        menuRepository.saveAndFlush(menu);
        UUID menuItemsId = menuItems.getId();

        // Get all the menuList where menuItems equals to menuItemsId
        defaultMenuShouldBeFound("menuItemsId.equals=" + menuItemsId);

        // Get all the menuList where menuItems equals to UUID.randomUUID()
        defaultMenuShouldNotBeFound("menuItemsId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuShouldBeFound(String filter) throws Exception {
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuShouldNotBeFound(String filter) throws Exception {
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).get();
        // Disconnect from session so that the updates on updatedMenu are not directly saved in db
        em.detach(updatedMenu);
        updatedMenu
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        MenuDTO menuDTO = menuMapper.toDto(updatedMenu);

        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuDTO))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenu.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMenu.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu.createdDate(UPDATED_CREATED_DATE);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenu.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMenu.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenu.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMenu.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(UUID.randomUUID());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Delete the menu
        restMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, menu.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

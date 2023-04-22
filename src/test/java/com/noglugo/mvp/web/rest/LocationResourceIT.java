package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Location;
import com.noglugo.mvp.repository.LocationRepository;
import com.noglugo.mvp.service.criteria.LocationCriteria;
import com.noglugo.mvp.service.dto.LocationDTO;
import com.noglugo.mvp.service.mapper.LocationMapper;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final Double DEFAULT_LATTITUDE = 1D;
    private static final Double UPDATED_LATTITUDE = 2D;
    private static final Double SMALLER_LATTITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGTITUDE = 1D;
    private static final Double UPDATED_LONGTITUDE = 2D;
    private static final Double SMALLER_LONGTITUDE = 1D - 1D;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .lattitude(DEFAULT_LATTITUDE)
            .longtitude(DEFAULT_LONGTITUDE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .lattitude(UPDATED_LATTITUDE)
            .longtitude(UPDATED_LONGTITUDE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return location;
    }

    @BeforeEach
    public void initTest() {
        location = createEntity(em);
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLattitude()).isEqualTo(DEFAULT_LATTITUDE);
        assertThat(testLocation.getLongtitude()).isEqualTo(DEFAULT_LONGTITUDE);
        assertThat(testLocation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLocation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLocation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testLocation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        locationRepository.saveAndFlush(location);
        LocationDTO locationDTO = locationMapper.toDto(location);

        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLattitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLattitude(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongtitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setLongtitude(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().toString())))
            .andExpect(jsonPath("$.[*].lattitude").value(hasItem(DEFAULT_LATTITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtitude").value(hasItem(DEFAULT_LONGTITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().toString()))
            .andExpect(jsonPath("$.lattitude").value(DEFAULT_LATTITUDE.doubleValue()))
            .andExpect(jsonPath("$.longtitude").value(DEFAULT_LONGTITUDE.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        UUID id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude equals to DEFAULT_LATTITUDE
        defaultLocationShouldBeFound("lattitude.equals=" + DEFAULT_LATTITUDE);

        // Get all the locationList where lattitude equals to UPDATED_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.equals=" + UPDATED_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude in DEFAULT_LATTITUDE or UPDATED_LATTITUDE
        defaultLocationShouldBeFound("lattitude.in=" + DEFAULT_LATTITUDE + "," + UPDATED_LATTITUDE);

        // Get all the locationList where lattitude equals to UPDATED_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.in=" + UPDATED_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude is not null
        defaultLocationShouldBeFound("lattitude.specified=true");

        // Get all the locationList where lattitude is null
        defaultLocationShouldNotBeFound("lattitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude is greater than or equal to DEFAULT_LATTITUDE
        defaultLocationShouldBeFound("lattitude.greaterThanOrEqual=" + DEFAULT_LATTITUDE);

        // Get all the locationList where lattitude is greater than or equal to UPDATED_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.greaterThanOrEqual=" + UPDATED_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude is less than or equal to DEFAULT_LATTITUDE
        defaultLocationShouldBeFound("lattitude.lessThanOrEqual=" + DEFAULT_LATTITUDE);

        // Get all the locationList where lattitude is less than or equal to SMALLER_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.lessThanOrEqual=" + SMALLER_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude is less than DEFAULT_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.lessThan=" + DEFAULT_LATTITUDE);

        // Get all the locationList where lattitude is less than UPDATED_LATTITUDE
        defaultLocationShouldBeFound("lattitude.lessThan=" + UPDATED_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLattitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lattitude is greater than DEFAULT_LATTITUDE
        defaultLocationShouldNotBeFound("lattitude.greaterThan=" + DEFAULT_LATTITUDE);

        // Get all the locationList where lattitude is greater than SMALLER_LATTITUDE
        defaultLocationShouldBeFound("lattitude.greaterThan=" + SMALLER_LATTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude equals to DEFAULT_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.equals=" + DEFAULT_LONGTITUDE);

        // Get all the locationList where longtitude equals to UPDATED_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.equals=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude in DEFAULT_LONGTITUDE or UPDATED_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.in=" + DEFAULT_LONGTITUDE + "," + UPDATED_LONGTITUDE);

        // Get all the locationList where longtitude equals to UPDATED_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.in=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude is not null
        defaultLocationShouldBeFound("longtitude.specified=true");

        // Get all the locationList where longtitude is null
        defaultLocationShouldNotBeFound("longtitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude is greater than or equal to DEFAULT_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.greaterThanOrEqual=" + DEFAULT_LONGTITUDE);

        // Get all the locationList where longtitude is greater than or equal to UPDATED_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.greaterThanOrEqual=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude is less than or equal to DEFAULT_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.lessThanOrEqual=" + DEFAULT_LONGTITUDE);

        // Get all the locationList where longtitude is less than or equal to SMALLER_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.lessThanOrEqual=" + SMALLER_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude is less than DEFAULT_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.lessThan=" + DEFAULT_LONGTITUDE);

        // Get all the locationList where longtitude is less than UPDATED_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.lessThan=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongtitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where longtitude is greater than DEFAULT_LONGTITUDE
        defaultLocationShouldNotBeFound("longtitude.greaterThan=" + DEFAULT_LONGTITUDE);

        // Get all the locationList where longtitude is greater than SMALLER_LONGTITUDE
        defaultLocationShouldBeFound("longtitude.greaterThan=" + SMALLER_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdBy equals to DEFAULT_CREATED_BY
        defaultLocationShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the locationList where createdBy equals to UPDATED_CREATED_BY
        defaultLocationShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultLocationShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the locationList where createdBy equals to UPDATED_CREATED_BY
        defaultLocationShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdBy is not null
        defaultLocationShouldBeFound("createdBy.specified=true");

        // Get all the locationList where createdBy is null
        defaultLocationShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdBy contains DEFAULT_CREATED_BY
        defaultLocationShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the locationList where createdBy contains UPDATED_CREATED_BY
        defaultLocationShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdBy does not contain DEFAULT_CREATED_BY
        defaultLocationShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the locationList where createdBy does not contain UPDATED_CREATED_BY
        defaultLocationShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultLocationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the locationList where createdDate equals to UPDATED_CREATED_DATE
        defaultLocationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultLocationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the locationList where createdDate equals to UPDATED_CREATED_DATE
        defaultLocationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLocationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where createdDate is not null
        defaultLocationShouldBeFound("createdDate.specified=true");

        // Get all the locationList where createdDate is null
        defaultLocationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultLocationShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the locationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultLocationShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultLocationShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the locationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultLocationShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedBy is not null
        defaultLocationShouldBeFound("lastModifiedBy.specified=true");

        // Get all the locationList where lastModifiedBy is null
        defaultLocationShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultLocationShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the locationList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultLocationShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultLocationShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the locationList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultLocationShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultLocationShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the locationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultLocationShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultLocationShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the locationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultLocationShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllLocationsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList where lastModifiedDate is not null
        defaultLocationShouldBeFound("lastModifiedDate.specified=true");

        // Get all the locationList where lastModifiedDate is null
        defaultLocationShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByAddressIsEqualToSomething() throws Exception {
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            locationRepository.saveAndFlush(location);
            address = AddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        em.persist(address);
        em.flush();
        location.setAddress(address);
        locationRepository.saveAndFlush(location);
        UUID addressId = address.getId();

        // Get all the locationList where address equals to addressId
        defaultLocationShouldBeFound("addressId.equals=" + addressId);

        // Get all the locationList where address equals to UUID.randomUUID()
        defaultLocationShouldNotBeFound("addressId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().toString())))
            .andExpect(jsonPath("$.[*].lattitude").value(hasItem(DEFAULT_LATTITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longtitude").value(hasItem(DEFAULT_LONGTITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).get();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .lattitude(UPDATED_LATTITUDE)
            .longtitude(UPDATED_LONGTITUDE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLattitude()).isEqualTo(UPDATED_LATTITUDE);
        assertThat(testLocation.getLongtitude()).isEqualTo(UPDATED_LONGTITUDE);
        assertThat(testLocation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLocation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .lattitude(UPDATED_LATTITUDE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLattitude()).isEqualTo(UPDATED_LATTITUDE);
        assertThat(testLocation.getLongtitude()).isEqualTo(DEFAULT_LONGTITUDE);
        assertThat(testLocation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLocation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .lattitude(UPDATED_LATTITUDE)
            .longtitude(UPDATED_LONGTITUDE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getLattitude()).isEqualTo(UPDATED_LATTITUDE);
        assertThat(testLocation.getLongtitude()).isEqualTo(UPDATED_LONGTITUDE);
        assertThat(testLocation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLocation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLocation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
        location.setId(UUID.randomUUID());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

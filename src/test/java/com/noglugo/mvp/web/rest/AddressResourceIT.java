package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Location;
import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.domain.enumeration.Governorate;
import com.noglugo.mvp.repository.AddressRepository;
import com.noglugo.mvp.service.criteria.AddressCriteria;
import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.mapper.AddressMapper;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_STREET_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final Governorate DEFAULT_CITY = Governorate.TUNIS;
    private static final Governorate UPDATED_CITY = Governorate.ARIANA;

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .street1(DEFAULT_STREET_1)
            .street2(DEFAULT_STREET_2)
            .city(DEFAULT_CITY)
            .postalCode(DEFAULT_POSTAL_CODE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAddress.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAddress.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAddress.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        addressRepository.saveAndFlush(address);
        AddressDTO addressDTO = addressMapper.toDto(address);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStreet1IsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreet1(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPostalCode(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().toString())))
            .andExpect(jsonPath("$.[*].street1").value(hasItem(DEFAULT_STREET_1)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().toString()))
            .andExpect(jsonPath("$.street1").value(DEFAULT_STREET_1))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        UUID id = address.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street1 equals to DEFAULT_STREET_1
        defaultAddressShouldBeFound("street1.equals=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 equals to UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.equals=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street1 in DEFAULT_STREET_1 or UPDATED_STREET_1
        defaultAddressShouldBeFound("street1.in=" + DEFAULT_STREET_1 + "," + UPDATED_STREET_1);

        // Get all the addressList where street1 equals to UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.in=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street1 is not null
        defaultAddressShouldBeFound("street1.specified=true");

        // Get all the addressList where street1 is null
        defaultAddressShouldNotBeFound("street1.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street1 contains DEFAULT_STREET_1
        defaultAddressShouldBeFound("street1.contains=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 contains UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.contains=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street1 does not contain DEFAULT_STREET_1
        defaultAddressShouldNotBeFound("street1.doesNotContain=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 does not contain UPDATED_STREET_1
        defaultAddressShouldBeFound("street1.doesNotContain=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street2 equals to DEFAULT_STREET_2
        defaultAddressShouldBeFound("street2.equals=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 equals to UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.equals=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street2 in DEFAULT_STREET_2 or UPDATED_STREET_2
        defaultAddressShouldBeFound("street2.in=" + DEFAULT_STREET_2 + "," + UPDATED_STREET_2);

        // Get all the addressList where street2 equals to UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.in=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street2 is not null
        defaultAddressShouldBeFound("street2.specified=true");

        // Get all the addressList where street2 is null
        defaultAddressShouldNotBeFound("street2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street2 contains DEFAULT_STREET_2
        defaultAddressShouldBeFound("street2.contains=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 contains UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.contains=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street2 does not contain DEFAULT_STREET_2
        defaultAddressShouldNotBeFound("street2.doesNotContain=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 does not contain UPDATED_STREET_2
        defaultAddressShouldBeFound("street2.doesNotContain=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city equals to DEFAULT_CITY
        defaultAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city is not null
        defaultAddressShouldBeFound("city.specified=true");

        // Get all the addressList where city is null
        defaultAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode is not null
        defaultAddressShouldBeFound("postalCode.specified=true");

        // Get all the addressList where postalCode is null
        defaultAddressShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode contains DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode contains UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdBy equals to DEFAULT_CREATED_BY
        defaultAddressShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the addressList where createdBy equals to UPDATED_CREATED_BY
        defaultAddressShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAddressShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the addressList where createdBy equals to UPDATED_CREATED_BY
        defaultAddressShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdBy is not null
        defaultAddressShouldBeFound("createdBy.specified=true");

        // Get all the addressList where createdBy is null
        defaultAddressShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdBy contains DEFAULT_CREATED_BY
        defaultAddressShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the addressList where createdBy contains UPDATED_CREATED_BY
        defaultAddressShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdBy does not contain DEFAULT_CREATED_BY
        defaultAddressShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the addressList where createdBy does not contain UPDATED_CREATED_BY
        defaultAddressShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdDate equals to DEFAULT_CREATED_DATE
        defaultAddressShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the addressList where createdDate equals to UPDATED_CREATED_DATE
        defaultAddressShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultAddressShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the addressList where createdDate equals to UPDATED_CREATED_DATE
        defaultAddressShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdDate is not null
        defaultAddressShouldBeFound("createdDate.specified=true");

        // Get all the addressList where createdDate is null
        defaultAddressShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy is not null
        defaultAddressShouldBeFound("lastModifiedBy.specified=true");

        // Get all the addressList where lastModifiedBy is null
        defaultAddressShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAddressShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the addressList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAddressShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAddressShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the addressList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAddressShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultAddressShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the addressList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAddressShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAddressesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where lastModifiedDate is not null
        defaultAddressShouldBeFound("lastModifiedDate.specified=true");

        // Get all the addressList where lastModifiedDate is null
        defaultAddressShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByOrderSAIsEqualToSomething() throws Exception {
        Order orderSA;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            orderSA = OrderResourceIT.createEntity(em);
        } else {
            orderSA = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(orderSA);
        em.flush();
        address.setOrderSA(orderSA);
        addressRepository.saveAndFlush(address);
        UUID orderSAId = orderSA.getId();

        // Get all the addressList where orderSA equals to orderSAId
        defaultAddressShouldBeFound("orderSAId.equals=" + orderSAId);

        // Get all the addressList where orderSA equals to UUID.randomUUID()
        defaultAddressShouldNotBeFound("orderSAId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAddressesByOrderBAIsEqualToSomething() throws Exception {
        Order orderBA;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            orderBA = OrderResourceIT.createEntity(em);
        } else {
            orderBA = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(orderBA);
        em.flush();
        address.setOrderBA(orderBA);
        addressRepository.saveAndFlush(address);
        UUID orderBAId = orderBA.getId();

        // Get all the addressList where orderBA equals to orderBAId
        defaultAddressShouldBeFound("orderBAId.equals=" + orderBAId);

        // Get all the addressList where orderBA equals to UUID.randomUUID()
        defaultAddressShouldNotBeFound("orderBAId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAddressesByStoreIsEqualToSomething() throws Exception {
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            store = StoreResourceIT.createEntity(em);
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        em.persist(store);
        em.flush();
        address.setStore(store);
        addressRepository.saveAndFlush(address);
        UUID storeId = store.getId();

        // Get all the addressList where store equals to storeId
        defaultAddressShouldBeFound("storeId.equals=" + storeId);

        // Get all the addressList where store equals to UUID.randomUUID()
        defaultAddressShouldNotBeFound("storeId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAddressesByRestaurantIsEqualToSomething() throws Exception {
        Restaurant restaurant;
        if (TestUtil.findAll(em, Restaurant.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            restaurant = RestaurantResourceIT.createEntity(em);
        } else {
            restaurant = TestUtil.findAll(em, Restaurant.class).get(0);
        }
        em.persist(restaurant);
        em.flush();
        address.setRestaurant(restaurant);
        addressRepository.saveAndFlush(address);
        UUID restaurantId = restaurant.getId();

        // Get all the addressList where restaurant equals to restaurantId
        defaultAddressShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the addressList where restaurant equals to UUID.randomUUID()
        defaultAddressShouldNotBeFound("restaurantId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAddressesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        address.setLocation(location);
        location.setAddress(address);
        addressRepository.saveAndFlush(address);
        UUID locationId = location.getId();

        // Get all the addressList where location equals to locationId
        defaultAddressShouldBeFound("locationId.equals=" + locationId);

        // Get all the addressList where location equals to UUID.randomUUID()
        defaultAddressShouldNotBeFound("locationId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().toString())))
            .andExpect(jsonPath("$.[*].street1").value(hasItem(DEFAULT_STREET_1)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddress.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddress.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddress.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddress.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddress.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAddress.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAddress.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(UUID.randomUUID());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.GlutenProfile;
import com.noglugo.mvp.domain.enumeration.Diseas;
import com.noglugo.mvp.repository.GlutenProfileRepository;
import com.noglugo.mvp.service.criteria.GlutenProfileCriteria;
import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import com.noglugo.mvp.service.mapper.GlutenProfileMapper;
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
 * Integration tests for the {@link GlutenProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GlutenProfileResourceIT {

    private static final Diseas DEFAULT_DISEAS = Diseas.CELIAC;
    private static final Diseas UPDATED_DISEAS = Diseas.GLUTEN_SENSITIVE;

    private static final String DEFAULT_OTHER_DISEAS = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_DISEAS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STRICTNESS_LEVEL = 1;
    private static final Integer UPDATED_STRICTNESS_LEVEL = 2;
    private static final Integer SMALLER_STRICTNESS_LEVEL = 1 - 1;

    private static final Integer DEFAULT_DIARY_FREE_PREFERENCE_LVL = 1;
    private static final Integer UPDATED_DIARY_FREE_PREFERENCE_LVL = 2;
    private static final Integer SMALLER_DIARY_FREE_PREFERENCE_LVL = 1 - 1;

    private static final Integer DEFAULT_VEGAN_PREFERENCE_LVL = 1;
    private static final Integer UPDATED_VEGAN_PREFERENCE_LVL = 2;
    private static final Integer SMALLER_VEGAN_PREFERENCE_LVL = 1 - 1;

    private static final Integer DEFAULT_KETO_PREFERENCE_LVL = 1;
    private static final Integer UPDATED_KETO_PREFERENCE_LVL = 2;
    private static final Integer SMALLER_KETO_PREFERENCE_LVL = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/gluten-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GlutenProfileRepository glutenProfileRepository;

    @Autowired
    private GlutenProfileMapper glutenProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlutenProfileMockMvc;

    private GlutenProfile glutenProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlutenProfile createEntity(EntityManager em) {
        GlutenProfile glutenProfile = new GlutenProfile()
            .diseas(DEFAULT_DISEAS)
            .otherDiseas(DEFAULT_OTHER_DISEAS)
            .strictnessLevel(DEFAULT_STRICTNESS_LEVEL)
            .diaryFreePreferenceLvl(DEFAULT_DIARY_FREE_PREFERENCE_LVL)
            .veganPreferenceLvl(DEFAULT_VEGAN_PREFERENCE_LVL)
            .ketoPreferenceLvl(DEFAULT_KETO_PREFERENCE_LVL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .userId(DEFAULT_USER_ID);
        return glutenProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlutenProfile createUpdatedEntity(EntityManager em) {
        GlutenProfile glutenProfile = new GlutenProfile()
            .diseas(UPDATED_DISEAS)
            .otherDiseas(UPDATED_OTHER_DISEAS)
            .strictnessLevel(UPDATED_STRICTNESS_LEVEL)
            .diaryFreePreferenceLvl(UPDATED_DIARY_FREE_PREFERENCE_LVL)
            .veganPreferenceLvl(UPDATED_VEGAN_PREFERENCE_LVL)
            .ketoPreferenceLvl(UPDATED_KETO_PREFERENCE_LVL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        return glutenProfile;
    }

    @BeforeEach
    public void initTest() {
        glutenProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createGlutenProfile() throws Exception {
        int databaseSizeBeforeCreate = glutenProfileRepository.findAll().size();
        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);
        restGlutenProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeCreate + 1);
        GlutenProfile testGlutenProfile = glutenProfileList.get(glutenProfileList.size() - 1);
        assertThat(testGlutenProfile.getDiseas()).isEqualTo(DEFAULT_DISEAS);
        assertThat(testGlutenProfile.getOtherDiseas()).isEqualTo(DEFAULT_OTHER_DISEAS);
        assertThat(testGlutenProfile.getStrictnessLevel()).isEqualTo(DEFAULT_STRICTNESS_LEVEL);
        assertThat(testGlutenProfile.getDiaryFreePreferenceLvl()).isEqualTo(DEFAULT_DIARY_FREE_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getVeganPreferenceLvl()).isEqualTo(DEFAULT_VEGAN_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getKetoPreferenceLvl()).isEqualTo(DEFAULT_KETO_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testGlutenProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGlutenProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testGlutenProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGlutenProfile.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createGlutenProfileWithExistingId() throws Exception {
        // Create the GlutenProfile with an existing ID
        glutenProfileRepository.saveAndFlush(glutenProfile);
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        int databaseSizeBeforeCreate = glutenProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlutenProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGlutenProfiles() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(glutenProfile.getId().toString())))
            .andExpect(jsonPath("$.[*].diseas").value(hasItem(DEFAULT_DISEAS.toString())))
            .andExpect(jsonPath("$.[*].otherDiseas").value(hasItem(DEFAULT_OTHER_DISEAS)))
            .andExpect(jsonPath("$.[*].strictnessLevel").value(hasItem(DEFAULT_STRICTNESS_LEVEL)))
            .andExpect(jsonPath("$.[*].diaryFreePreferenceLvl").value(hasItem(DEFAULT_DIARY_FREE_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].veganPreferenceLvl").value(hasItem(DEFAULT_VEGAN_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].ketoPreferenceLvl").value(hasItem(DEFAULT_KETO_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getGlutenProfile() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get the glutenProfile
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, glutenProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(glutenProfile.getId().toString()))
            .andExpect(jsonPath("$.diseas").value(DEFAULT_DISEAS.toString()))
            .andExpect(jsonPath("$.otherDiseas").value(DEFAULT_OTHER_DISEAS))
            .andExpect(jsonPath("$.strictnessLevel").value(DEFAULT_STRICTNESS_LEVEL))
            .andExpect(jsonPath("$.diaryFreePreferenceLvl").value(DEFAULT_DIARY_FREE_PREFERENCE_LVL))
            .andExpect(jsonPath("$.veganPreferenceLvl").value(DEFAULT_VEGAN_PREFERENCE_LVL))
            .andExpect(jsonPath("$.ketoPreferenceLvl").value(DEFAULT_KETO_PREFERENCE_LVL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getGlutenProfilesByIdFiltering() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        UUID id = glutenProfile.getId();

        defaultGlutenProfileShouldBeFound("id.equals=" + id);
        defaultGlutenProfileShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiseasIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diseas equals to DEFAULT_DISEAS
        defaultGlutenProfileShouldBeFound("diseas.equals=" + DEFAULT_DISEAS);

        // Get all the glutenProfileList where diseas equals to UPDATED_DISEAS
        defaultGlutenProfileShouldNotBeFound("diseas.equals=" + UPDATED_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiseasIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diseas in DEFAULT_DISEAS or UPDATED_DISEAS
        defaultGlutenProfileShouldBeFound("diseas.in=" + DEFAULT_DISEAS + "," + UPDATED_DISEAS);

        // Get all the glutenProfileList where diseas equals to UPDATED_DISEAS
        defaultGlutenProfileShouldNotBeFound("diseas.in=" + UPDATED_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiseasIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diseas is not null
        defaultGlutenProfileShouldBeFound("diseas.specified=true");

        // Get all the glutenProfileList where diseas is null
        defaultGlutenProfileShouldNotBeFound("diseas.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByOtherDiseasIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where otherDiseas equals to DEFAULT_OTHER_DISEAS
        defaultGlutenProfileShouldBeFound("otherDiseas.equals=" + DEFAULT_OTHER_DISEAS);

        // Get all the glutenProfileList where otherDiseas equals to UPDATED_OTHER_DISEAS
        defaultGlutenProfileShouldNotBeFound("otherDiseas.equals=" + UPDATED_OTHER_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByOtherDiseasIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where otherDiseas in DEFAULT_OTHER_DISEAS or UPDATED_OTHER_DISEAS
        defaultGlutenProfileShouldBeFound("otherDiseas.in=" + DEFAULT_OTHER_DISEAS + "," + UPDATED_OTHER_DISEAS);

        // Get all the glutenProfileList where otherDiseas equals to UPDATED_OTHER_DISEAS
        defaultGlutenProfileShouldNotBeFound("otherDiseas.in=" + UPDATED_OTHER_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByOtherDiseasIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where otherDiseas is not null
        defaultGlutenProfileShouldBeFound("otherDiseas.specified=true");

        // Get all the glutenProfileList where otherDiseas is null
        defaultGlutenProfileShouldNotBeFound("otherDiseas.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByOtherDiseasContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where otherDiseas contains DEFAULT_OTHER_DISEAS
        defaultGlutenProfileShouldBeFound("otherDiseas.contains=" + DEFAULT_OTHER_DISEAS);

        // Get all the glutenProfileList where otherDiseas contains UPDATED_OTHER_DISEAS
        defaultGlutenProfileShouldNotBeFound("otherDiseas.contains=" + UPDATED_OTHER_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByOtherDiseasNotContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where otherDiseas does not contain DEFAULT_OTHER_DISEAS
        defaultGlutenProfileShouldNotBeFound("otherDiseas.doesNotContain=" + DEFAULT_OTHER_DISEAS);

        // Get all the glutenProfileList where otherDiseas does not contain UPDATED_OTHER_DISEAS
        defaultGlutenProfileShouldBeFound("otherDiseas.doesNotContain=" + UPDATED_OTHER_DISEAS);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel equals to DEFAULT_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.equals=" + DEFAULT_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel equals to UPDATED_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.equals=" + UPDATED_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel in DEFAULT_STRICTNESS_LEVEL or UPDATED_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.in=" + DEFAULT_STRICTNESS_LEVEL + "," + UPDATED_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel equals to UPDATED_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.in=" + UPDATED_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel is not null
        defaultGlutenProfileShouldBeFound("strictnessLevel.specified=true");

        // Get all the glutenProfileList where strictnessLevel is null
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel is greater than or equal to DEFAULT_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.greaterThanOrEqual=" + DEFAULT_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel is greater than or equal to UPDATED_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.greaterThanOrEqual=" + UPDATED_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel is less than or equal to DEFAULT_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.lessThanOrEqual=" + DEFAULT_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel is less than or equal to SMALLER_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.lessThanOrEqual=" + SMALLER_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel is less than DEFAULT_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.lessThan=" + DEFAULT_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel is less than UPDATED_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.lessThan=" + UPDATED_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByStrictnessLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where strictnessLevel is greater than DEFAULT_STRICTNESS_LEVEL
        defaultGlutenProfileShouldNotBeFound("strictnessLevel.greaterThan=" + DEFAULT_STRICTNESS_LEVEL);

        // Get all the glutenProfileList where strictnessLevel is greater than SMALLER_STRICTNESS_LEVEL
        defaultGlutenProfileShouldBeFound("strictnessLevel.greaterThan=" + SMALLER_STRICTNESS_LEVEL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl equals to DEFAULT_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.equals=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL);

        // Get all the glutenProfileList where diaryFreePreferenceLvl equals to UPDATED_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.equals=" + UPDATED_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl in DEFAULT_DIARY_FREE_PREFERENCE_LVL or UPDATED_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound(
            "diaryFreePreferenceLvl.in=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL + "," + UPDATED_DIARY_FREE_PREFERENCE_LVL
        );

        // Get all the glutenProfileList where diaryFreePreferenceLvl equals to UPDATED_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.in=" + UPDATED_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is not null
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.specified=true");

        // Get all the glutenProfileList where diaryFreePreferenceLvl is null
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is greater than or equal to DEFAULT_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.greaterThanOrEqual=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is greater than or equal to UPDATED_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.greaterThanOrEqual=" + UPDATED_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is less than or equal to DEFAULT_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.lessThanOrEqual=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is less than or equal to SMALLER_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.lessThanOrEqual=" + SMALLER_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsLessThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is less than DEFAULT_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.lessThan=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is less than UPDATED_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.lessThan=" + UPDATED_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByDiaryFreePreferenceLvlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is greater than DEFAULT_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("diaryFreePreferenceLvl.greaterThan=" + DEFAULT_DIARY_FREE_PREFERENCE_LVL);

        // Get all the glutenProfileList where diaryFreePreferenceLvl is greater than SMALLER_DIARY_FREE_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("diaryFreePreferenceLvl.greaterThan=" + SMALLER_DIARY_FREE_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl equals to DEFAULT_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.equals=" + DEFAULT_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl equals to UPDATED_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.equals=" + UPDATED_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl in DEFAULT_VEGAN_PREFERENCE_LVL or UPDATED_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.in=" + DEFAULT_VEGAN_PREFERENCE_LVL + "," + UPDATED_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl equals to UPDATED_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.in=" + UPDATED_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl is not null
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.specified=true");

        // Get all the glutenProfileList where veganPreferenceLvl is null
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl is greater than or equal to DEFAULT_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.greaterThanOrEqual=" + DEFAULT_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl is greater than or equal to UPDATED_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.greaterThanOrEqual=" + UPDATED_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl is less than or equal to DEFAULT_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.lessThanOrEqual=" + DEFAULT_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl is less than or equal to SMALLER_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.lessThanOrEqual=" + SMALLER_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsLessThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl is less than DEFAULT_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.lessThan=" + DEFAULT_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl is less than UPDATED_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.lessThan=" + UPDATED_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByVeganPreferenceLvlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where veganPreferenceLvl is greater than DEFAULT_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("veganPreferenceLvl.greaterThan=" + DEFAULT_VEGAN_PREFERENCE_LVL);

        // Get all the glutenProfileList where veganPreferenceLvl is greater than SMALLER_VEGAN_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("veganPreferenceLvl.greaterThan=" + SMALLER_VEGAN_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl equals to DEFAULT_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.equals=" + DEFAULT_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl equals to UPDATED_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.equals=" + UPDATED_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl in DEFAULT_KETO_PREFERENCE_LVL or UPDATED_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.in=" + DEFAULT_KETO_PREFERENCE_LVL + "," + UPDATED_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl equals to UPDATED_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.in=" + UPDATED_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl is not null
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.specified=true");

        // Get all the glutenProfileList where ketoPreferenceLvl is null
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl is greater than or equal to DEFAULT_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.greaterThanOrEqual=" + DEFAULT_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl is greater than or equal to UPDATED_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.greaterThanOrEqual=" + UPDATED_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl is less than or equal to DEFAULT_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.lessThanOrEqual=" + DEFAULT_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl is less than or equal to SMALLER_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.lessThanOrEqual=" + SMALLER_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsLessThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl is less than DEFAULT_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.lessThan=" + DEFAULT_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl is less than UPDATED_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.lessThan=" + UPDATED_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByKetoPreferenceLvlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where ketoPreferenceLvl is greater than DEFAULT_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldNotBeFound("ketoPreferenceLvl.greaterThan=" + DEFAULT_KETO_PREFERENCE_LVL);

        // Get all the glutenProfileList where ketoPreferenceLvl is greater than SMALLER_KETO_PREFERENCE_LVL
        defaultGlutenProfileShouldBeFound("ketoPreferenceLvl.greaterThan=" + SMALLER_KETO_PREFERENCE_LVL);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdBy equals to DEFAULT_CREATED_BY
        defaultGlutenProfileShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the glutenProfileList where createdBy equals to UPDATED_CREATED_BY
        defaultGlutenProfileShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultGlutenProfileShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the glutenProfileList where createdBy equals to UPDATED_CREATED_BY
        defaultGlutenProfileShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdBy is not null
        defaultGlutenProfileShouldBeFound("createdBy.specified=true");

        // Get all the glutenProfileList where createdBy is null
        defaultGlutenProfileShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdBy contains DEFAULT_CREATED_BY
        defaultGlutenProfileShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the glutenProfileList where createdBy contains UPDATED_CREATED_BY
        defaultGlutenProfileShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdBy does not contain DEFAULT_CREATED_BY
        defaultGlutenProfileShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the glutenProfileList where createdBy does not contain UPDATED_CREATED_BY
        defaultGlutenProfileShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdDate equals to DEFAULT_CREATED_DATE
        defaultGlutenProfileShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the glutenProfileList where createdDate equals to UPDATED_CREATED_DATE
        defaultGlutenProfileShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultGlutenProfileShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the glutenProfileList where createdDate equals to UPDATED_CREATED_DATE
        defaultGlutenProfileShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where createdDate is not null
        defaultGlutenProfileShouldBeFound("createdDate.specified=true");

        // Get all the glutenProfileList where createdDate is null
        defaultGlutenProfileShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultGlutenProfileShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the glutenProfileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultGlutenProfileShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultGlutenProfileShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the glutenProfileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultGlutenProfileShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedBy is not null
        defaultGlutenProfileShouldBeFound("lastModifiedBy.specified=true");

        // Get all the glutenProfileList where lastModifiedBy is null
        defaultGlutenProfileShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultGlutenProfileShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the glutenProfileList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultGlutenProfileShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultGlutenProfileShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the glutenProfileList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultGlutenProfileShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultGlutenProfileShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the glutenProfileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultGlutenProfileShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultGlutenProfileShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the glutenProfileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultGlutenProfileShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where lastModifiedDate is not null
        defaultGlutenProfileShouldBeFound("lastModifiedDate.specified=true");

        // Get all the glutenProfileList where lastModifiedDate is null
        defaultGlutenProfileShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId equals to DEFAULT_USER_ID
        defaultGlutenProfileShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the glutenProfileList where userId equals to UPDATED_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultGlutenProfileShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the glutenProfileList where userId equals to UPDATED_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId is not null
        defaultGlutenProfileShouldBeFound("userId.specified=true");

        // Get all the glutenProfileList where userId is null
        defaultGlutenProfileShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId is greater than or equal to DEFAULT_USER_ID
        defaultGlutenProfileShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the glutenProfileList where userId is greater than or equal to UPDATED_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId is less than or equal to DEFAULT_USER_ID
        defaultGlutenProfileShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the glutenProfileList where userId is less than or equal to SMALLER_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId is less than DEFAULT_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the glutenProfileList where userId is less than UPDATED_USER_ID
        defaultGlutenProfileShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllGlutenProfilesByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        // Get all the glutenProfileList where userId is greater than DEFAULT_USER_ID
        defaultGlutenProfileShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the glutenProfileList where userId is greater than SMALLER_USER_ID
        defaultGlutenProfileShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGlutenProfileShouldBeFound(String filter) throws Exception {
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(glutenProfile.getId().toString())))
            .andExpect(jsonPath("$.[*].diseas").value(hasItem(DEFAULT_DISEAS.toString())))
            .andExpect(jsonPath("$.[*].otherDiseas").value(hasItem(DEFAULT_OTHER_DISEAS)))
            .andExpect(jsonPath("$.[*].strictnessLevel").value(hasItem(DEFAULT_STRICTNESS_LEVEL)))
            .andExpect(jsonPath("$.[*].diaryFreePreferenceLvl").value(hasItem(DEFAULT_DIARY_FREE_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].veganPreferenceLvl").value(hasItem(DEFAULT_VEGAN_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].ketoPreferenceLvl").value(hasItem(DEFAULT_KETO_PREFERENCE_LVL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGlutenProfileShouldNotBeFound(String filter) throws Exception {
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGlutenProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGlutenProfile() throws Exception {
        // Get the glutenProfile
        restGlutenProfileMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGlutenProfile() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();

        // Update the glutenProfile
        GlutenProfile updatedGlutenProfile = glutenProfileRepository.findById(glutenProfile.getId()).get();
        // Disconnect from session so that the updates on updatedGlutenProfile are not directly saved in db
        em.detach(updatedGlutenProfile);
        updatedGlutenProfile
            .diseas(UPDATED_DISEAS)
            .otherDiseas(UPDATED_OTHER_DISEAS)
            .strictnessLevel(UPDATED_STRICTNESS_LEVEL)
            .diaryFreePreferenceLvl(UPDATED_DIARY_FREE_PREFERENCE_LVL)
            .veganPreferenceLvl(UPDATED_VEGAN_PREFERENCE_LVL)
            .ketoPreferenceLvl(UPDATED_KETO_PREFERENCE_LVL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(updatedGlutenProfile);

        restGlutenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, glutenProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
        GlutenProfile testGlutenProfile = glutenProfileList.get(glutenProfileList.size() - 1);
        assertThat(testGlutenProfile.getDiseas()).isEqualTo(UPDATED_DISEAS);
        assertThat(testGlutenProfile.getOtherDiseas()).isEqualTo(UPDATED_OTHER_DISEAS);
        assertThat(testGlutenProfile.getStrictnessLevel()).isEqualTo(UPDATED_STRICTNESS_LEVEL);
        assertThat(testGlutenProfile.getDiaryFreePreferenceLvl()).isEqualTo(UPDATED_DIARY_FREE_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getVeganPreferenceLvl()).isEqualTo(UPDATED_VEGAN_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getKetoPreferenceLvl()).isEqualTo(UPDATED_KETO_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlutenProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGlutenProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlutenProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGlutenProfile.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, glutenProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGlutenProfileWithPatch() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();

        // Update the glutenProfile using partial update
        GlutenProfile partialUpdatedGlutenProfile = new GlutenProfile();
        partialUpdatedGlutenProfile.setId(glutenProfile.getId());

        partialUpdatedGlutenProfile
            .diseas(UPDATED_DISEAS)
            .strictnessLevel(UPDATED_STRICTNESS_LEVEL)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restGlutenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlutenProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlutenProfile))
            )
            .andExpect(status().isOk());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
        GlutenProfile testGlutenProfile = glutenProfileList.get(glutenProfileList.size() - 1);
        assertThat(testGlutenProfile.getDiseas()).isEqualTo(UPDATED_DISEAS);
        assertThat(testGlutenProfile.getOtherDiseas()).isEqualTo(DEFAULT_OTHER_DISEAS);
        assertThat(testGlutenProfile.getStrictnessLevel()).isEqualTo(UPDATED_STRICTNESS_LEVEL);
        assertThat(testGlutenProfile.getDiaryFreePreferenceLvl()).isEqualTo(DEFAULT_DIARY_FREE_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getVeganPreferenceLvl()).isEqualTo(DEFAULT_VEGAN_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getKetoPreferenceLvl()).isEqualTo(DEFAULT_KETO_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlutenProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGlutenProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlutenProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGlutenProfile.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateGlutenProfileWithPatch() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();

        // Update the glutenProfile using partial update
        GlutenProfile partialUpdatedGlutenProfile = new GlutenProfile();
        partialUpdatedGlutenProfile.setId(glutenProfile.getId());

        partialUpdatedGlutenProfile
            .diseas(UPDATED_DISEAS)
            .otherDiseas(UPDATED_OTHER_DISEAS)
            .strictnessLevel(UPDATED_STRICTNESS_LEVEL)
            .diaryFreePreferenceLvl(UPDATED_DIARY_FREE_PREFERENCE_LVL)
            .veganPreferenceLvl(UPDATED_VEGAN_PREFERENCE_LVL)
            .ketoPreferenceLvl(UPDATED_KETO_PREFERENCE_LVL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);

        restGlutenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlutenProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlutenProfile))
            )
            .andExpect(status().isOk());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
        GlutenProfile testGlutenProfile = glutenProfileList.get(glutenProfileList.size() - 1);
        assertThat(testGlutenProfile.getDiseas()).isEqualTo(UPDATED_DISEAS);
        assertThat(testGlutenProfile.getOtherDiseas()).isEqualTo(UPDATED_OTHER_DISEAS);
        assertThat(testGlutenProfile.getStrictnessLevel()).isEqualTo(UPDATED_STRICTNESS_LEVEL);
        assertThat(testGlutenProfile.getDiaryFreePreferenceLvl()).isEqualTo(UPDATED_DIARY_FREE_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getVeganPreferenceLvl()).isEqualTo(UPDATED_VEGAN_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getKetoPreferenceLvl()).isEqualTo(UPDATED_KETO_PREFERENCE_LVL);
        assertThat(testGlutenProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlutenProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGlutenProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlutenProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGlutenProfile.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, glutenProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlutenProfile() throws Exception {
        int databaseSizeBeforeUpdate = glutenProfileRepository.findAll().size();
        glutenProfile.setId(UUID.randomUUID());

        // Create the GlutenProfile
        GlutenProfileDTO glutenProfileDTO = glutenProfileMapper.toDto(glutenProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlutenProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(glutenProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlutenProfile in the database
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGlutenProfile() throws Exception {
        // Initialize the database
        glutenProfileRepository.saveAndFlush(glutenProfile);

        int databaseSizeBeforeDelete = glutenProfileRepository.findAll().size();

        // Delete the glutenProfile
        restGlutenProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, glutenProfile.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlutenProfile> glutenProfileList = glutenProfileRepository.findAll();
        assertThat(glutenProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

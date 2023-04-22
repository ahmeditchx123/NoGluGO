package com.noglugo.mvp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.noglugo.mvp.IntegrationTest;
import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.Review;
import com.noglugo.mvp.repository.ReviewRepository;
import com.noglugo.mvp.service.criteria.ReviewCriteria;
import com.noglugo.mvp.service.dto.ReviewDTO;
import com.noglugo.mvp.service.mapper.ReviewMapper;
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
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReviewResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .rating(DEFAULT_RATING)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .userId(DEFAULT_USER_ID);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity(EntityManager em) {
        Review review = new Review()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .rating(UPDATED_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        return review;
    }

    @BeforeEach
    public void initTest() {
        review = createEntity(em);
    }

    @Test
    @Transactional
    void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();
        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testReview.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testReview.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testReview.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testReview.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        reviewRepository.saveAndFlush(review);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setTitle(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setContent(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setRating(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getReviewsByIdFiltering() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        UUID id = review.getId();

        defaultReviewShouldBeFound("id.equals=" + id);
        defaultReviewShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title equals to DEFAULT_TITLE
        defaultReviewShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the reviewList where title equals to UPDATED_TITLE
        defaultReviewShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultReviewShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the reviewList where title equals to UPDATED_TITLE
        defaultReviewShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title is not null
        defaultReviewShouldBeFound("title.specified=true");

        // Get all the reviewList where title is null
        defaultReviewShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByTitleContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title contains DEFAULT_TITLE
        defaultReviewShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the reviewList where title contains UPDATED_TITLE
        defaultReviewShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where title does not contain DEFAULT_TITLE
        defaultReviewShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the reviewList where title does not contain UPDATED_TITLE
        defaultReviewShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReviewsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where content equals to DEFAULT_CONTENT
        defaultReviewShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the reviewList where content equals to UPDATED_CONTENT
        defaultReviewShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllReviewsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultReviewShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the reviewList where content equals to UPDATED_CONTENT
        defaultReviewShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllReviewsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where content is not null
        defaultReviewShouldBeFound("content.specified=true");

        // Get all the reviewList where content is null
        defaultReviewShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByContentContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where content contains DEFAULT_CONTENT
        defaultReviewShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the reviewList where content contains UPDATED_CONTENT
        defaultReviewShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllReviewsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where content does not contain DEFAULT_CONTENT
        defaultReviewShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the reviewList where content does not contain UPDATED_CONTENT
        defaultReviewShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating equals to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultReviewShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is not null
        defaultReviewShouldBeFound("rating.specified=true");

        // Get all the reviewList where rating is null
        defaultReviewShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than or equal to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the reviewList where rating is greater than or equal to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than or equal to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the reviewList where rating is less than or equal to SMALLER_RATING
        defaultReviewShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating is less than UPDATED_RATING
        defaultReviewShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating is greater than SMALLER_RATING
        defaultReviewShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdBy equals to DEFAULT_CREATED_BY
        defaultReviewShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the reviewList where createdBy equals to UPDATED_CREATED_BY
        defaultReviewShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultReviewShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the reviewList where createdBy equals to UPDATED_CREATED_BY
        defaultReviewShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdBy is not null
        defaultReviewShouldBeFound("createdBy.specified=true");

        // Get all the reviewList where createdBy is null
        defaultReviewShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdBy contains DEFAULT_CREATED_BY
        defaultReviewShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the reviewList where createdBy contains UPDATED_CREATED_BY
        defaultReviewShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdBy does not contain DEFAULT_CREATED_BY
        defaultReviewShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the reviewList where createdBy does not contain UPDATED_CREATED_BY
        defaultReviewShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdDate equals to DEFAULT_CREATED_DATE
        defaultReviewShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the reviewList where createdDate equals to UPDATED_CREATED_DATE
        defaultReviewShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultReviewShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the reviewList where createdDate equals to UPDATED_CREATED_DATE
        defaultReviewShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdDate is not null
        defaultReviewShouldBeFound("createdDate.specified=true");

        // Get all the reviewList where createdDate is null
        defaultReviewShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultReviewShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reviewList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultReviewShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultReviewShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the reviewList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultReviewShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedBy is not null
        defaultReviewShouldBeFound("lastModifiedBy.specified=true");

        // Get all the reviewList where lastModifiedBy is null
        defaultReviewShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultReviewShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reviewList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultReviewShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultReviewShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the reviewList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultReviewShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultReviewShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the reviewList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultReviewShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultReviewShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the reviewList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultReviewShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReviewsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where lastModifiedDate is not null
        defaultReviewShouldBeFound("lastModifiedDate.specified=true");

        // Get all the reviewList where lastModifiedDate is null
        defaultReviewShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId equals to DEFAULT_USER_ID
        defaultReviewShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the reviewList where userId equals to UPDATED_USER_ID
        defaultReviewShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultReviewShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the reviewList where userId equals to UPDATED_USER_ID
        defaultReviewShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId is not null
        defaultReviewShouldBeFound("userId.specified=true");

        // Get all the reviewList where userId is null
        defaultReviewShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId is greater than or equal to DEFAULT_USER_ID
        defaultReviewShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the reviewList where userId is greater than or equal to UPDATED_USER_ID
        defaultReviewShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId is less than or equal to DEFAULT_USER_ID
        defaultReviewShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the reviewList where userId is less than or equal to SMALLER_USER_ID
        defaultReviewShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId is less than DEFAULT_USER_ID
        defaultReviewShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the reviewList where userId is less than UPDATED_USER_ID
        defaultReviewShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where userId is greater than DEFAULT_USER_ID
        defaultReviewShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the reviewList where userId is greater than SMALLER_USER_ID
        defaultReviewShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllReviewsByMenuItemIsEqualToSomething() throws Exception {
        MenuItem menuItem;
        if (TestUtil.findAll(em, MenuItem.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            menuItem = MenuItemResourceIT.createEntity(em);
        } else {
            menuItem = TestUtil.findAll(em, MenuItem.class).get(0);
        }
        em.persist(menuItem);
        em.flush();
        review.setMenuItem(menuItem);
        reviewRepository.saveAndFlush(review);
        UUID menuItemId = menuItem.getId();

        // Get all the reviewList where menuItem equals to menuItemId
        defaultReviewShouldBeFound("menuItemId.equals=" + menuItemId);

        // Get all the reviewList where menuItem equals to UUID.randomUUID()
        defaultReviewShouldNotBeFound("menuItemId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllReviewsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        review.setProduct(product);
        reviewRepository.saveAndFlush(review);
        UUID productId = product.getId();

        // Get all the reviewList where product equals to productId
        defaultReviewShouldBeFound("productId.equals=" + productId);

        // Get all the reviewList where product equals to UUID.randomUUID()
        defaultReviewShouldNotBeFound("productId.equals=" + UUID.randomUUID());
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).get();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .rating(UPDATED_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReview.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testReview.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testReview.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview.rating(UPDATED_RATING).createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReview.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testReview.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testReview.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .rating(UPDATED_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .userId(UPDATED_USER_ID);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testReview.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testReview.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testReview.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Delete the review
        restReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, review.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

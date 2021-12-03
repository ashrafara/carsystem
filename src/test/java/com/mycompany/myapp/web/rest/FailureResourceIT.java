package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.domain.Failure;
import com.mycompany.myapp.repository.FailureRepository;
import com.mycompany.myapp.service.criteria.FailureCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FailureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FailureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FAILURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FAILURE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FAILURE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_CAR_GUAGEFROM = 1D;
    private static final Double UPDATED_CAR_GUAGEFROM = 2D;
    private static final Double SMALLER_CAR_GUAGEFROM = 1D - 1D;

    private static final Double DEFAULT_CAR_GUAGE_TO = 1D;
    private static final Double UPDATED_CAR_GUAGE_TO = 2D;
    private static final Double SMALLER_CAR_GUAGE_TO = 1D - 1D;

    private static final String DEFAULT_CHANGEPART = "AAAAAAAAAA";
    private static final String UPDATED_CHANGEPART = "BBBBBBBBBB";

    private static final String DEFAULT_GARAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GARAGE_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String DEFAULT_INOVICE_1 = "AAAAAAAAAA";
    private static final String UPDATED_INOVICE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_INOVICE_2 = "AAAAAAAAAA";
    private static final String UPDATED_INOVICE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_INOVICE_3 = "AAAAAAAAAA";
    private static final String UPDATED_INOVICE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_INOVICE_4 = "AAAAAAAAAA";
    private static final String UPDATED_INOVICE_4 = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/failures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FailureRepository failureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFailureMockMvc;

    private Failure failure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Failure createEntity(EntityManager em) {
        Failure failure = new Failure()
            .name(DEFAULT_NAME)
            .failureDate(DEFAULT_FAILURE_DATE)
            .carGuagefrom(DEFAULT_CAR_GUAGEFROM)
            .carGuageTo(DEFAULT_CAR_GUAGE_TO)
            .changepart(DEFAULT_CHANGEPART)
            .garageName(DEFAULT_GARAGE_NAME)
            .price(DEFAULT_PRICE)
            .inovice1(DEFAULT_INOVICE_1)
            .inovice2(DEFAULT_INOVICE_2)
            .inovice3(DEFAULT_INOVICE_3)
            .inovice4(DEFAULT_INOVICE_4)
            .note(DEFAULT_NOTE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return failure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Failure createUpdatedEntity(EntityManager em) {
        Failure failure = new Failure()
            .name(UPDATED_NAME)
            .failureDate(UPDATED_FAILURE_DATE)
            .carGuagefrom(UPDATED_CAR_GUAGEFROM)
            .carGuageTo(UPDATED_CAR_GUAGE_TO)
            .changepart(UPDATED_CHANGEPART)
            .garageName(UPDATED_GARAGE_NAME)
            .price(UPDATED_PRICE)
            .inovice1(UPDATED_INOVICE_1)
            .inovice2(UPDATED_INOVICE_2)
            .inovice3(UPDATED_INOVICE_3)
            .inovice4(UPDATED_INOVICE_4)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return failure;
    }

    @BeforeEach
    public void initTest() {
        failure = createEntity(em);
    }

    @Test
    @Transactional
    void createFailure() throws Exception {
        int databaseSizeBeforeCreate = failureRepository.findAll().size();
        // Create the Failure
        restFailureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failure)))
            .andExpect(status().isCreated());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeCreate + 1);
        Failure testFailure = failureList.get(failureList.size() - 1);
        assertThat(testFailure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFailure.getFailureDate()).isEqualTo(DEFAULT_FAILURE_DATE);
        assertThat(testFailure.getCarGuagefrom()).isEqualTo(DEFAULT_CAR_GUAGEFROM);
        assertThat(testFailure.getCarGuageTo()).isEqualTo(DEFAULT_CAR_GUAGE_TO);
        assertThat(testFailure.getChangepart()).isEqualTo(DEFAULT_CHANGEPART);
        assertThat(testFailure.getGarageName()).isEqualTo(DEFAULT_GARAGE_NAME);
        assertThat(testFailure.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testFailure.getInovice1()).isEqualTo(DEFAULT_INOVICE_1);
        assertThat(testFailure.getInovice2()).isEqualTo(DEFAULT_INOVICE_2);
        assertThat(testFailure.getInovice3()).isEqualTo(DEFAULT_INOVICE_3);
        assertThat(testFailure.getInovice4()).isEqualTo(DEFAULT_INOVICE_4);
        assertThat(testFailure.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFailure.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFailure.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createFailureWithExistingId() throws Exception {
        // Create the Failure with an existing ID
        failure.setId(1L);

        int databaseSizeBeforeCreate = failureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFailureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failure)))
            .andExpect(status().isBadRequest());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFailures() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList
        restFailureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].failureDate").value(hasItem(DEFAULT_FAILURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].carGuagefrom").value(hasItem(DEFAULT_CAR_GUAGEFROM.doubleValue())))
            .andExpect(jsonPath("$.[*].carGuageTo").value(hasItem(DEFAULT_CAR_GUAGE_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].changepart").value(hasItem(DEFAULT_CHANGEPART)))
            .andExpect(jsonPath("$.[*].garageName").value(hasItem(DEFAULT_GARAGE_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].inovice1").value(hasItem(DEFAULT_INOVICE_1)))
            .andExpect(jsonPath("$.[*].inovice2").value(hasItem(DEFAULT_INOVICE_2)))
            .andExpect(jsonPath("$.[*].inovice3").value(hasItem(DEFAULT_INOVICE_3)))
            .andExpect(jsonPath("$.[*].inovice4").value(hasItem(DEFAULT_INOVICE_4)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getFailure() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get the failure
        restFailureMockMvc
            .perform(get(ENTITY_API_URL_ID, failure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(failure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.failureDate").value(DEFAULT_FAILURE_DATE.toString()))
            .andExpect(jsonPath("$.carGuagefrom").value(DEFAULT_CAR_GUAGEFROM.doubleValue()))
            .andExpect(jsonPath("$.carGuageTo").value(DEFAULT_CAR_GUAGE_TO.doubleValue()))
            .andExpect(jsonPath("$.changepart").value(DEFAULT_CHANGEPART))
            .andExpect(jsonPath("$.garageName").value(DEFAULT_GARAGE_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.inovice1").value(DEFAULT_INOVICE_1))
            .andExpect(jsonPath("$.inovice2").value(DEFAULT_INOVICE_2))
            .andExpect(jsonPath("$.inovice3").value(DEFAULT_INOVICE_3))
            .andExpect(jsonPath("$.inovice4").value(DEFAULT_INOVICE_4))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getFailuresByIdFiltering() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        Long id = failure.getId();

        defaultFailureShouldBeFound("id.equals=" + id);
        defaultFailureShouldNotBeFound("id.notEquals=" + id);

        defaultFailureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFailureShouldNotBeFound("id.greaterThan=" + id);

        defaultFailureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFailureShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFailuresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name equals to DEFAULT_NAME
        defaultFailureShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the failureList where name equals to UPDATED_NAME
        defaultFailureShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name not equals to DEFAULT_NAME
        defaultFailureShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the failureList where name not equals to UPDATED_NAME
        defaultFailureShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFailureShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the failureList where name equals to UPDATED_NAME
        defaultFailureShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name is not null
        defaultFailureShouldBeFound("name.specified=true");

        // Get all the failureList where name is null
        defaultFailureShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByNameContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name contains DEFAULT_NAME
        defaultFailureShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the failureList where name contains UPDATED_NAME
        defaultFailureShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where name does not contain DEFAULT_NAME
        defaultFailureShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the failureList where name does not contain UPDATED_NAME
        defaultFailureShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate equals to DEFAULT_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.equals=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate equals to UPDATED_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.equals=" + UPDATED_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate not equals to DEFAULT_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.notEquals=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate not equals to UPDATED_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.notEquals=" + UPDATED_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate in DEFAULT_FAILURE_DATE or UPDATED_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.in=" + DEFAULT_FAILURE_DATE + "," + UPDATED_FAILURE_DATE);

        // Get all the failureList where failureDate equals to UPDATED_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.in=" + UPDATED_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate is not null
        defaultFailureShouldBeFound("failureDate.specified=true");

        // Get all the failureList where failureDate is null
        defaultFailureShouldNotBeFound("failureDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate is greater than or equal to DEFAULT_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.greaterThanOrEqual=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate is greater than or equal to UPDATED_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.greaterThanOrEqual=" + UPDATED_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate is less than or equal to DEFAULT_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.lessThanOrEqual=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate is less than or equal to SMALLER_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.lessThanOrEqual=" + SMALLER_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate is less than DEFAULT_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.lessThan=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate is less than UPDATED_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.lessThan=" + UPDATED_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByFailureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where failureDate is greater than DEFAULT_FAILURE_DATE
        defaultFailureShouldNotBeFound("failureDate.greaterThan=" + DEFAULT_FAILURE_DATE);

        // Get all the failureList where failureDate is greater than SMALLER_FAILURE_DATE
        defaultFailureShouldBeFound("failureDate.greaterThan=" + SMALLER_FAILURE_DATE);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom equals to DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.equals=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom equals to UPDATED_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.equals=" + UPDATED_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom not equals to DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.notEquals=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom not equals to UPDATED_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.notEquals=" + UPDATED_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom in DEFAULT_CAR_GUAGEFROM or UPDATED_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.in=" + DEFAULT_CAR_GUAGEFROM + "," + UPDATED_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom equals to UPDATED_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.in=" + UPDATED_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom is not null
        defaultFailureShouldBeFound("carGuagefrom.specified=true");

        // Get all the failureList where carGuagefrom is null
        defaultFailureShouldNotBeFound("carGuagefrom.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom is greater than or equal to DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.greaterThanOrEqual=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom is greater than or equal to UPDATED_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.greaterThanOrEqual=" + UPDATED_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom is less than or equal to DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.lessThanOrEqual=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom is less than or equal to SMALLER_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.lessThanOrEqual=" + SMALLER_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsLessThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom is less than DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.lessThan=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom is less than UPDATED_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.lessThan=" + UPDATED_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuagefromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuagefrom is greater than DEFAULT_CAR_GUAGEFROM
        defaultFailureShouldNotBeFound("carGuagefrom.greaterThan=" + DEFAULT_CAR_GUAGEFROM);

        // Get all the failureList where carGuagefrom is greater than SMALLER_CAR_GUAGEFROM
        defaultFailureShouldBeFound("carGuagefrom.greaterThan=" + SMALLER_CAR_GUAGEFROM);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo equals to DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.equals=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo equals to UPDATED_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.equals=" + UPDATED_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo not equals to DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.notEquals=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo not equals to UPDATED_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.notEquals=" + UPDATED_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo in DEFAULT_CAR_GUAGE_TO or UPDATED_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.in=" + DEFAULT_CAR_GUAGE_TO + "," + UPDATED_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo equals to UPDATED_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.in=" + UPDATED_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo is not null
        defaultFailureShouldBeFound("carGuageTo.specified=true");

        // Get all the failureList where carGuageTo is null
        defaultFailureShouldNotBeFound("carGuageTo.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo is greater than or equal to DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.greaterThanOrEqual=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo is greater than or equal to UPDATED_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.greaterThanOrEqual=" + UPDATED_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo is less than or equal to DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.lessThanOrEqual=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo is less than or equal to SMALLER_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.lessThanOrEqual=" + SMALLER_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsLessThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo is less than DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.lessThan=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo is less than UPDATED_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.lessThan=" + UPDATED_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByCarGuageToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where carGuageTo is greater than DEFAULT_CAR_GUAGE_TO
        defaultFailureShouldNotBeFound("carGuageTo.greaterThan=" + DEFAULT_CAR_GUAGE_TO);

        // Get all the failureList where carGuageTo is greater than SMALLER_CAR_GUAGE_TO
        defaultFailureShouldBeFound("carGuageTo.greaterThan=" + SMALLER_CAR_GUAGE_TO);
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart equals to DEFAULT_CHANGEPART
        defaultFailureShouldBeFound("changepart.equals=" + DEFAULT_CHANGEPART);

        // Get all the failureList where changepart equals to UPDATED_CHANGEPART
        defaultFailureShouldNotBeFound("changepart.equals=" + UPDATED_CHANGEPART);
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart not equals to DEFAULT_CHANGEPART
        defaultFailureShouldNotBeFound("changepart.notEquals=" + DEFAULT_CHANGEPART);

        // Get all the failureList where changepart not equals to UPDATED_CHANGEPART
        defaultFailureShouldBeFound("changepart.notEquals=" + UPDATED_CHANGEPART);
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart in DEFAULT_CHANGEPART or UPDATED_CHANGEPART
        defaultFailureShouldBeFound("changepart.in=" + DEFAULT_CHANGEPART + "," + UPDATED_CHANGEPART);

        // Get all the failureList where changepart equals to UPDATED_CHANGEPART
        defaultFailureShouldNotBeFound("changepart.in=" + UPDATED_CHANGEPART);
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart is not null
        defaultFailureShouldBeFound("changepart.specified=true");

        // Get all the failureList where changepart is null
        defaultFailureShouldNotBeFound("changepart.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart contains DEFAULT_CHANGEPART
        defaultFailureShouldBeFound("changepart.contains=" + DEFAULT_CHANGEPART);

        // Get all the failureList where changepart contains UPDATED_CHANGEPART
        defaultFailureShouldNotBeFound("changepart.contains=" + UPDATED_CHANGEPART);
    }

    @Test
    @Transactional
    void getAllFailuresByChangepartNotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where changepart does not contain DEFAULT_CHANGEPART
        defaultFailureShouldNotBeFound("changepart.doesNotContain=" + DEFAULT_CHANGEPART);

        // Get all the failureList where changepart does not contain UPDATED_CHANGEPART
        defaultFailureShouldBeFound("changepart.doesNotContain=" + UPDATED_CHANGEPART);
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName equals to DEFAULT_GARAGE_NAME
        defaultFailureShouldBeFound("garageName.equals=" + DEFAULT_GARAGE_NAME);

        // Get all the failureList where garageName equals to UPDATED_GARAGE_NAME
        defaultFailureShouldNotBeFound("garageName.equals=" + UPDATED_GARAGE_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName not equals to DEFAULT_GARAGE_NAME
        defaultFailureShouldNotBeFound("garageName.notEquals=" + DEFAULT_GARAGE_NAME);

        // Get all the failureList where garageName not equals to UPDATED_GARAGE_NAME
        defaultFailureShouldBeFound("garageName.notEquals=" + UPDATED_GARAGE_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName in DEFAULT_GARAGE_NAME or UPDATED_GARAGE_NAME
        defaultFailureShouldBeFound("garageName.in=" + DEFAULT_GARAGE_NAME + "," + UPDATED_GARAGE_NAME);

        // Get all the failureList where garageName equals to UPDATED_GARAGE_NAME
        defaultFailureShouldNotBeFound("garageName.in=" + UPDATED_GARAGE_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName is not null
        defaultFailureShouldBeFound("garageName.specified=true");

        // Get all the failureList where garageName is null
        defaultFailureShouldNotBeFound("garageName.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName contains DEFAULT_GARAGE_NAME
        defaultFailureShouldBeFound("garageName.contains=" + DEFAULT_GARAGE_NAME);

        // Get all the failureList where garageName contains UPDATED_GARAGE_NAME
        defaultFailureShouldNotBeFound("garageName.contains=" + UPDATED_GARAGE_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByGarageNameNotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where garageName does not contain DEFAULT_GARAGE_NAME
        defaultFailureShouldNotBeFound("garageName.doesNotContain=" + DEFAULT_GARAGE_NAME);

        // Get all the failureList where garageName does not contain UPDATED_GARAGE_NAME
        defaultFailureShouldBeFound("garageName.doesNotContain=" + UPDATED_GARAGE_NAME);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price equals to DEFAULT_PRICE
        defaultFailureShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the failureList where price equals to UPDATED_PRICE
        defaultFailureShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price not equals to DEFAULT_PRICE
        defaultFailureShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the failureList where price not equals to UPDATED_PRICE
        defaultFailureShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultFailureShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the failureList where price equals to UPDATED_PRICE
        defaultFailureShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price is not null
        defaultFailureShouldBeFound("price.specified=true");

        // Get all the failureList where price is null
        defaultFailureShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price is greater than or equal to DEFAULT_PRICE
        defaultFailureShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the failureList where price is greater than or equal to UPDATED_PRICE
        defaultFailureShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price is less than or equal to DEFAULT_PRICE
        defaultFailureShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the failureList where price is less than or equal to SMALLER_PRICE
        defaultFailureShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price is less than DEFAULT_PRICE
        defaultFailureShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the failureList where price is less than UPDATED_PRICE
        defaultFailureShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where price is greater than DEFAULT_PRICE
        defaultFailureShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the failureList where price is greater than SMALLER_PRICE
        defaultFailureShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1IsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 equals to DEFAULT_INOVICE_1
        defaultFailureShouldBeFound("inovice1.equals=" + DEFAULT_INOVICE_1);

        // Get all the failureList where inovice1 equals to UPDATED_INOVICE_1
        defaultFailureShouldNotBeFound("inovice1.equals=" + UPDATED_INOVICE_1);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 not equals to DEFAULT_INOVICE_1
        defaultFailureShouldNotBeFound("inovice1.notEquals=" + DEFAULT_INOVICE_1);

        // Get all the failureList where inovice1 not equals to UPDATED_INOVICE_1
        defaultFailureShouldBeFound("inovice1.notEquals=" + UPDATED_INOVICE_1);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1IsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 in DEFAULT_INOVICE_1 or UPDATED_INOVICE_1
        defaultFailureShouldBeFound("inovice1.in=" + DEFAULT_INOVICE_1 + "," + UPDATED_INOVICE_1);

        // Get all the failureList where inovice1 equals to UPDATED_INOVICE_1
        defaultFailureShouldNotBeFound("inovice1.in=" + UPDATED_INOVICE_1);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1IsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 is not null
        defaultFailureShouldBeFound("inovice1.specified=true");

        // Get all the failureList where inovice1 is null
        defaultFailureShouldNotBeFound("inovice1.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1ContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 contains DEFAULT_INOVICE_1
        defaultFailureShouldBeFound("inovice1.contains=" + DEFAULT_INOVICE_1);

        // Get all the failureList where inovice1 contains UPDATED_INOVICE_1
        defaultFailureShouldNotBeFound("inovice1.contains=" + UPDATED_INOVICE_1);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice1NotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice1 does not contain DEFAULT_INOVICE_1
        defaultFailureShouldNotBeFound("inovice1.doesNotContain=" + DEFAULT_INOVICE_1);

        // Get all the failureList where inovice1 does not contain UPDATED_INOVICE_1
        defaultFailureShouldBeFound("inovice1.doesNotContain=" + UPDATED_INOVICE_1);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2IsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 equals to DEFAULT_INOVICE_2
        defaultFailureShouldBeFound("inovice2.equals=" + DEFAULT_INOVICE_2);

        // Get all the failureList where inovice2 equals to UPDATED_INOVICE_2
        defaultFailureShouldNotBeFound("inovice2.equals=" + UPDATED_INOVICE_2);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 not equals to DEFAULT_INOVICE_2
        defaultFailureShouldNotBeFound("inovice2.notEquals=" + DEFAULT_INOVICE_2);

        // Get all the failureList where inovice2 not equals to UPDATED_INOVICE_2
        defaultFailureShouldBeFound("inovice2.notEquals=" + UPDATED_INOVICE_2);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2IsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 in DEFAULT_INOVICE_2 or UPDATED_INOVICE_2
        defaultFailureShouldBeFound("inovice2.in=" + DEFAULT_INOVICE_2 + "," + UPDATED_INOVICE_2);

        // Get all the failureList where inovice2 equals to UPDATED_INOVICE_2
        defaultFailureShouldNotBeFound("inovice2.in=" + UPDATED_INOVICE_2);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2IsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 is not null
        defaultFailureShouldBeFound("inovice2.specified=true");

        // Get all the failureList where inovice2 is null
        defaultFailureShouldNotBeFound("inovice2.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2ContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 contains DEFAULT_INOVICE_2
        defaultFailureShouldBeFound("inovice2.contains=" + DEFAULT_INOVICE_2);

        // Get all the failureList where inovice2 contains UPDATED_INOVICE_2
        defaultFailureShouldNotBeFound("inovice2.contains=" + UPDATED_INOVICE_2);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice2NotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice2 does not contain DEFAULT_INOVICE_2
        defaultFailureShouldNotBeFound("inovice2.doesNotContain=" + DEFAULT_INOVICE_2);

        // Get all the failureList where inovice2 does not contain UPDATED_INOVICE_2
        defaultFailureShouldBeFound("inovice2.doesNotContain=" + UPDATED_INOVICE_2);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3IsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 equals to DEFAULT_INOVICE_3
        defaultFailureShouldBeFound("inovice3.equals=" + DEFAULT_INOVICE_3);

        // Get all the failureList where inovice3 equals to UPDATED_INOVICE_3
        defaultFailureShouldNotBeFound("inovice3.equals=" + UPDATED_INOVICE_3);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 not equals to DEFAULT_INOVICE_3
        defaultFailureShouldNotBeFound("inovice3.notEquals=" + DEFAULT_INOVICE_3);

        // Get all the failureList where inovice3 not equals to UPDATED_INOVICE_3
        defaultFailureShouldBeFound("inovice3.notEquals=" + UPDATED_INOVICE_3);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3IsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 in DEFAULT_INOVICE_3 or UPDATED_INOVICE_3
        defaultFailureShouldBeFound("inovice3.in=" + DEFAULT_INOVICE_3 + "," + UPDATED_INOVICE_3);

        // Get all the failureList where inovice3 equals to UPDATED_INOVICE_3
        defaultFailureShouldNotBeFound("inovice3.in=" + UPDATED_INOVICE_3);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3IsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 is not null
        defaultFailureShouldBeFound("inovice3.specified=true");

        // Get all the failureList where inovice3 is null
        defaultFailureShouldNotBeFound("inovice3.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3ContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 contains DEFAULT_INOVICE_3
        defaultFailureShouldBeFound("inovice3.contains=" + DEFAULT_INOVICE_3);

        // Get all the failureList where inovice3 contains UPDATED_INOVICE_3
        defaultFailureShouldNotBeFound("inovice3.contains=" + UPDATED_INOVICE_3);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice3NotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice3 does not contain DEFAULT_INOVICE_3
        defaultFailureShouldNotBeFound("inovice3.doesNotContain=" + DEFAULT_INOVICE_3);

        // Get all the failureList where inovice3 does not contain UPDATED_INOVICE_3
        defaultFailureShouldBeFound("inovice3.doesNotContain=" + UPDATED_INOVICE_3);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4IsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 equals to DEFAULT_INOVICE_4
        defaultFailureShouldBeFound("inovice4.equals=" + DEFAULT_INOVICE_4);

        // Get all the failureList where inovice4 equals to UPDATED_INOVICE_4
        defaultFailureShouldNotBeFound("inovice4.equals=" + UPDATED_INOVICE_4);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4IsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 not equals to DEFAULT_INOVICE_4
        defaultFailureShouldNotBeFound("inovice4.notEquals=" + DEFAULT_INOVICE_4);

        // Get all the failureList where inovice4 not equals to UPDATED_INOVICE_4
        defaultFailureShouldBeFound("inovice4.notEquals=" + UPDATED_INOVICE_4);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4IsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 in DEFAULT_INOVICE_4 or UPDATED_INOVICE_4
        defaultFailureShouldBeFound("inovice4.in=" + DEFAULT_INOVICE_4 + "," + UPDATED_INOVICE_4);

        // Get all the failureList where inovice4 equals to UPDATED_INOVICE_4
        defaultFailureShouldNotBeFound("inovice4.in=" + UPDATED_INOVICE_4);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4IsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 is not null
        defaultFailureShouldBeFound("inovice4.specified=true");

        // Get all the failureList where inovice4 is null
        defaultFailureShouldNotBeFound("inovice4.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4ContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 contains DEFAULT_INOVICE_4
        defaultFailureShouldBeFound("inovice4.contains=" + DEFAULT_INOVICE_4);

        // Get all the failureList where inovice4 contains UPDATED_INOVICE_4
        defaultFailureShouldNotBeFound("inovice4.contains=" + UPDATED_INOVICE_4);
    }

    @Test
    @Transactional
    void getAllFailuresByInovice4NotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where inovice4 does not contain DEFAULT_INOVICE_4
        defaultFailureShouldNotBeFound("inovice4.doesNotContain=" + DEFAULT_INOVICE_4);

        // Get all the failureList where inovice4 does not contain UPDATED_INOVICE_4
        defaultFailureShouldBeFound("inovice4.doesNotContain=" + UPDATED_INOVICE_4);
    }

    @Test
    @Transactional
    void getAllFailuresByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note equals to DEFAULT_NOTE
        defaultFailureShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the failureList where note equals to UPDATED_NOTE
        defaultFailureShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFailuresByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note not equals to DEFAULT_NOTE
        defaultFailureShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the failureList where note not equals to UPDATED_NOTE
        defaultFailureShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFailuresByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultFailureShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the failureList where note equals to UPDATED_NOTE
        defaultFailureShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFailuresByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note is not null
        defaultFailureShouldBeFound("note.specified=true");

        // Get all the failureList where note is null
        defaultFailureShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllFailuresByNoteContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note contains DEFAULT_NOTE
        defaultFailureShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the failureList where note contains UPDATED_NOTE
        defaultFailureShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFailuresByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        // Get all the failureList where note does not contain DEFAULT_NOTE
        defaultFailureShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the failureList where note does not contain UPDATED_NOTE
        defaultFailureShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFailuresByCarIsEqualToSomething() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            car = CarResourceIT.createEntity(em);
            em.persist(car);
            em.flush();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        em.persist(car);
        em.flush();
        failure.setCar(car);
        failureRepository.saveAndFlush(failure);
        Long carId = car.getId();

        // Get all the failureList where car equals to carId
        defaultFailureShouldBeFound("carId.equals=" + carId);

        // Get all the failureList where car equals to (carId + 1)
        defaultFailureShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFailureShouldBeFound(String filter) throws Exception {
        restFailureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].failureDate").value(hasItem(DEFAULT_FAILURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].carGuagefrom").value(hasItem(DEFAULT_CAR_GUAGEFROM.doubleValue())))
            .andExpect(jsonPath("$.[*].carGuageTo").value(hasItem(DEFAULT_CAR_GUAGE_TO.doubleValue())))
            .andExpect(jsonPath("$.[*].changepart").value(hasItem(DEFAULT_CHANGEPART)))
            .andExpect(jsonPath("$.[*].garageName").value(hasItem(DEFAULT_GARAGE_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].inovice1").value(hasItem(DEFAULT_INOVICE_1)))
            .andExpect(jsonPath("$.[*].inovice2").value(hasItem(DEFAULT_INOVICE_2)))
            .andExpect(jsonPath("$.[*].inovice3").value(hasItem(DEFAULT_INOVICE_3)))
            .andExpect(jsonPath("$.[*].inovice4").value(hasItem(DEFAULT_INOVICE_4)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restFailureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFailureShouldNotBeFound(String filter) throws Exception {
        restFailureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFailureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFailure() throws Exception {
        // Get the failure
        restFailureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFailure() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        int databaseSizeBeforeUpdate = failureRepository.findAll().size();

        // Update the failure
        Failure updatedFailure = failureRepository.findById(failure.getId()).get();
        // Disconnect from session so that the updates on updatedFailure are not directly saved in db
        em.detach(updatedFailure);
        updatedFailure
            .name(UPDATED_NAME)
            .failureDate(UPDATED_FAILURE_DATE)
            .carGuagefrom(UPDATED_CAR_GUAGEFROM)
            .carGuageTo(UPDATED_CAR_GUAGE_TO)
            .changepart(UPDATED_CHANGEPART)
            .garageName(UPDATED_GARAGE_NAME)
            .price(UPDATED_PRICE)
            .inovice1(UPDATED_INOVICE_1)
            .inovice2(UPDATED_INOVICE_2)
            .inovice3(UPDATED_INOVICE_3)
            .inovice4(UPDATED_INOVICE_4)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFailureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFailure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFailure))
            )
            .andExpect(status().isOk());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
        Failure testFailure = failureList.get(failureList.size() - 1);
        assertThat(testFailure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFailure.getFailureDate()).isEqualTo(UPDATED_FAILURE_DATE);
        assertThat(testFailure.getCarGuagefrom()).isEqualTo(UPDATED_CAR_GUAGEFROM);
        assertThat(testFailure.getCarGuageTo()).isEqualTo(UPDATED_CAR_GUAGE_TO);
        assertThat(testFailure.getChangepart()).isEqualTo(UPDATED_CHANGEPART);
        assertThat(testFailure.getGarageName()).isEqualTo(UPDATED_GARAGE_NAME);
        assertThat(testFailure.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFailure.getInovice1()).isEqualTo(UPDATED_INOVICE_1);
        assertThat(testFailure.getInovice2()).isEqualTo(UPDATED_INOVICE_2);
        assertThat(testFailure.getInovice3()).isEqualTo(UPDATED_INOVICE_3);
        assertThat(testFailure.getInovice4()).isEqualTo(UPDATED_INOVICE_4);
        assertThat(testFailure.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFailure.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFailure.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, failure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(failure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(failure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFailureWithPatch() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        int databaseSizeBeforeUpdate = failureRepository.findAll().size();

        // Update the failure using partial update
        Failure partialUpdatedFailure = new Failure();
        partialUpdatedFailure.setId(failure.getId());

        partialUpdatedFailure.failureDate(UPDATED_FAILURE_DATE).carGuagefrom(UPDATED_CAR_GUAGEFROM).carGuageTo(UPDATED_CAR_GUAGE_TO);

        restFailureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFailure))
            )
            .andExpect(status().isOk());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
        Failure testFailure = failureList.get(failureList.size() - 1);
        assertThat(testFailure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFailure.getFailureDate()).isEqualTo(UPDATED_FAILURE_DATE);
        assertThat(testFailure.getCarGuagefrom()).isEqualTo(UPDATED_CAR_GUAGEFROM);
        assertThat(testFailure.getCarGuageTo()).isEqualTo(UPDATED_CAR_GUAGE_TO);
        assertThat(testFailure.getChangepart()).isEqualTo(DEFAULT_CHANGEPART);
        assertThat(testFailure.getGarageName()).isEqualTo(DEFAULT_GARAGE_NAME);
        assertThat(testFailure.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testFailure.getInovice1()).isEqualTo(DEFAULT_INOVICE_1);
        assertThat(testFailure.getInovice2()).isEqualTo(DEFAULT_INOVICE_2);
        assertThat(testFailure.getInovice3()).isEqualTo(DEFAULT_INOVICE_3);
        assertThat(testFailure.getInovice4()).isEqualTo(DEFAULT_INOVICE_4);
        assertThat(testFailure.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFailure.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFailure.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFailureWithPatch() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        int databaseSizeBeforeUpdate = failureRepository.findAll().size();

        // Update the failure using partial update
        Failure partialUpdatedFailure = new Failure();
        partialUpdatedFailure.setId(failure.getId());

        partialUpdatedFailure
            .name(UPDATED_NAME)
            .failureDate(UPDATED_FAILURE_DATE)
            .carGuagefrom(UPDATED_CAR_GUAGEFROM)
            .carGuageTo(UPDATED_CAR_GUAGE_TO)
            .changepart(UPDATED_CHANGEPART)
            .garageName(UPDATED_GARAGE_NAME)
            .price(UPDATED_PRICE)
            .inovice1(UPDATED_INOVICE_1)
            .inovice2(UPDATED_INOVICE_2)
            .inovice3(UPDATED_INOVICE_3)
            .inovice4(UPDATED_INOVICE_4)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFailureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFailure))
            )
            .andExpect(status().isOk());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
        Failure testFailure = failureList.get(failureList.size() - 1);
        assertThat(testFailure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFailure.getFailureDate()).isEqualTo(UPDATED_FAILURE_DATE);
        assertThat(testFailure.getCarGuagefrom()).isEqualTo(UPDATED_CAR_GUAGEFROM);
        assertThat(testFailure.getCarGuageTo()).isEqualTo(UPDATED_CAR_GUAGE_TO);
        assertThat(testFailure.getChangepart()).isEqualTo(UPDATED_CHANGEPART);
        assertThat(testFailure.getGarageName()).isEqualTo(UPDATED_GARAGE_NAME);
        assertThat(testFailure.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFailure.getInovice1()).isEqualTo(UPDATED_INOVICE_1);
        assertThat(testFailure.getInovice2()).isEqualTo(UPDATED_INOVICE_2);
        assertThat(testFailure.getInovice3()).isEqualTo(UPDATED_INOVICE_3);
        assertThat(testFailure.getInovice4()).isEqualTo(UPDATED_INOVICE_4);
        assertThat(testFailure.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFailure.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFailure.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, failure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(failure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(failure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFailure() throws Exception {
        int databaseSizeBeforeUpdate = failureRepository.findAll().size();
        failure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(failure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Failure in the database
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFailure() throws Exception {
        // Initialize the database
        failureRepository.saveAndFlush(failure);

        int databaseSizeBeforeDelete = failureRepository.findAll().size();

        // Delete the failure
        restFailureMockMvc
            .perform(delete(ENTITY_API_URL_ID, failure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Failure> failureList = failureRepository.findAll();
        assertThat(failureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.Failure;
import com.mycompany.myapp.repository.CarRepository;
import com.mycompany.myapp.service.criteria.CarCriteria;
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

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAR_ISSUE = 1;
    private static final Integer UPDATED_CAR_ISSUE = 2;
    private static final Integer SMALLER_CAR_ISSUE = 1 - 1;

    private static final String DEFAULT_CARN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_MOTOR = "AAAAAAAAAA";
    private static final String UPDATED_CAR_MOTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_SHELL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CAR_SHELL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CLASSIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_MADEIN = "AAAAAAAAAA";
    private static final String UPDATED_MADEIN = "BBBBBBBBBB";

    private static final String DEFAULT_PANAELNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PANAELNUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .name(DEFAULT_NAME)
            .carIssue(DEFAULT_CAR_ISSUE)
            .carnNumber(DEFAULT_CARN_NUMBER)
            .carMotor(DEFAULT_CAR_MOTOR)
            .carShellNumber(DEFAULT_CAR_SHELL_NUMBER)
            .classification(DEFAULT_CLASSIFICATION)
            .madein(DEFAULT_MADEIN)
            .panaelnumber(DEFAULT_PANAELNUMBER)
            .notes(DEFAULT_NOTES);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .name(UPDATED_NAME)
            .carIssue(UPDATED_CAR_ISSUE)
            .carnNumber(UPDATED_CARN_NUMBER)
            .carMotor(UPDATED_CAR_MOTOR)
            .carShellNumber(UPDATED_CAR_SHELL_NUMBER)
            .classification(UPDATED_CLASSIFICATION)
            .madein(UPDATED_MADEIN)
            .panaelnumber(UPDATED_PANAELNUMBER)
            .notes(UPDATED_NOTES);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create the Car
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCar.getCarIssue()).isEqualTo(DEFAULT_CAR_ISSUE);
        assertThat(testCar.getCarnNumber()).isEqualTo(DEFAULT_CARN_NUMBER);
        assertThat(testCar.getCarMotor()).isEqualTo(DEFAULT_CAR_MOTOR);
        assertThat(testCar.getCarShellNumber()).isEqualTo(DEFAULT_CAR_SHELL_NUMBER);
        assertThat(testCar.getClassification()).isEqualTo(DEFAULT_CLASSIFICATION);
        assertThat(testCar.getMadein()).isEqualTo(DEFAULT_MADEIN);
        assertThat(testCar.getPanaelnumber()).isEqualTo(DEFAULT_PANAELNUMBER);
        assertThat(testCar.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);

        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].carIssue").value(hasItem(DEFAULT_CAR_ISSUE)))
            .andExpect(jsonPath("$.[*].carnNumber").value(hasItem(DEFAULT_CARN_NUMBER)))
            .andExpect(jsonPath("$.[*].carMotor").value(hasItem(DEFAULT_CAR_MOTOR)))
            .andExpect(jsonPath("$.[*].carShellNumber").value(hasItem(DEFAULT_CAR_SHELL_NUMBER)))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].madein").value(hasItem(DEFAULT_MADEIN)))
            .andExpect(jsonPath("$.[*].panaelnumber").value(hasItem(DEFAULT_PANAELNUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.carIssue").value(DEFAULT_CAR_ISSUE))
            .andExpect(jsonPath("$.carnNumber").value(DEFAULT_CARN_NUMBER))
            .andExpect(jsonPath("$.carMotor").value(DEFAULT_CAR_MOTOR))
            .andExpect(jsonPath("$.carShellNumber").value(DEFAULT_CAR_SHELL_NUMBER))
            .andExpect(jsonPath("$.classification").value(DEFAULT_CLASSIFICATION))
            .andExpect(jsonPath("$.madein").value(DEFAULT_MADEIN))
            .andExpect(jsonPath("$.panaelnumber").value(DEFAULT_PANAELNUMBER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name equals to DEFAULT_NAME
        defaultCarShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the carList where name equals to UPDATED_NAME
        defaultCarShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name not equals to DEFAULT_NAME
        defaultCarShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the carList where name not equals to UPDATED_NAME
        defaultCarShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCarShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the carList where name equals to UPDATED_NAME
        defaultCarShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name is not null
        defaultCarShouldBeFound("name.specified=true");

        // Get all the carList where name is null
        defaultCarShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByNameContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name contains DEFAULT_NAME
        defaultCarShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the carList where name contains UPDATED_NAME
        defaultCarShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name does not contain DEFAULT_NAME
        defaultCarShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the carList where name does not contain UPDATED_NAME
        defaultCarShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue equals to DEFAULT_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.equals=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue equals to UPDATED_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.equals=" + UPDATED_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue not equals to DEFAULT_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.notEquals=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue not equals to UPDATED_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.notEquals=" + UPDATED_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue in DEFAULT_CAR_ISSUE or UPDATED_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.in=" + DEFAULT_CAR_ISSUE + "," + UPDATED_CAR_ISSUE);

        // Get all the carList where carIssue equals to UPDATED_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.in=" + UPDATED_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue is not null
        defaultCarShouldBeFound("carIssue.specified=true");

        // Get all the carList where carIssue is null
        defaultCarShouldNotBeFound("carIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue is greater than or equal to DEFAULT_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.greaterThanOrEqual=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue is greater than or equal to UPDATED_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.greaterThanOrEqual=" + UPDATED_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue is less than or equal to DEFAULT_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.lessThanOrEqual=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue is less than or equal to SMALLER_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.lessThanOrEqual=" + SMALLER_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue is less than DEFAULT_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.lessThan=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue is less than UPDATED_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.lessThan=" + UPDATED_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarIssueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carIssue is greater than DEFAULT_CAR_ISSUE
        defaultCarShouldNotBeFound("carIssue.greaterThan=" + DEFAULT_CAR_ISSUE);

        // Get all the carList where carIssue is greater than SMALLER_CAR_ISSUE
        defaultCarShouldBeFound("carIssue.greaterThan=" + SMALLER_CAR_ISSUE);
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber equals to DEFAULT_CARN_NUMBER
        defaultCarShouldBeFound("carnNumber.equals=" + DEFAULT_CARN_NUMBER);

        // Get all the carList where carnNumber equals to UPDATED_CARN_NUMBER
        defaultCarShouldNotBeFound("carnNumber.equals=" + UPDATED_CARN_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber not equals to DEFAULT_CARN_NUMBER
        defaultCarShouldNotBeFound("carnNumber.notEquals=" + DEFAULT_CARN_NUMBER);

        // Get all the carList where carnNumber not equals to UPDATED_CARN_NUMBER
        defaultCarShouldBeFound("carnNumber.notEquals=" + UPDATED_CARN_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber in DEFAULT_CARN_NUMBER or UPDATED_CARN_NUMBER
        defaultCarShouldBeFound("carnNumber.in=" + DEFAULT_CARN_NUMBER + "," + UPDATED_CARN_NUMBER);

        // Get all the carList where carnNumber equals to UPDATED_CARN_NUMBER
        defaultCarShouldNotBeFound("carnNumber.in=" + UPDATED_CARN_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber is not null
        defaultCarShouldBeFound("carnNumber.specified=true");

        // Get all the carList where carnNumber is null
        defaultCarShouldNotBeFound("carnNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber contains DEFAULT_CARN_NUMBER
        defaultCarShouldBeFound("carnNumber.contains=" + DEFAULT_CARN_NUMBER);

        // Get all the carList where carnNumber contains UPDATED_CARN_NUMBER
        defaultCarShouldNotBeFound("carnNumber.contains=" + UPDATED_CARN_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarnNumberNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carnNumber does not contain DEFAULT_CARN_NUMBER
        defaultCarShouldNotBeFound("carnNumber.doesNotContain=" + DEFAULT_CARN_NUMBER);

        // Get all the carList where carnNumber does not contain UPDATED_CARN_NUMBER
        defaultCarShouldBeFound("carnNumber.doesNotContain=" + UPDATED_CARN_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor equals to DEFAULT_CAR_MOTOR
        defaultCarShouldBeFound("carMotor.equals=" + DEFAULT_CAR_MOTOR);

        // Get all the carList where carMotor equals to UPDATED_CAR_MOTOR
        defaultCarShouldNotBeFound("carMotor.equals=" + UPDATED_CAR_MOTOR);
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor not equals to DEFAULT_CAR_MOTOR
        defaultCarShouldNotBeFound("carMotor.notEquals=" + DEFAULT_CAR_MOTOR);

        // Get all the carList where carMotor not equals to UPDATED_CAR_MOTOR
        defaultCarShouldBeFound("carMotor.notEquals=" + UPDATED_CAR_MOTOR);
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor in DEFAULT_CAR_MOTOR or UPDATED_CAR_MOTOR
        defaultCarShouldBeFound("carMotor.in=" + DEFAULT_CAR_MOTOR + "," + UPDATED_CAR_MOTOR);

        // Get all the carList where carMotor equals to UPDATED_CAR_MOTOR
        defaultCarShouldNotBeFound("carMotor.in=" + UPDATED_CAR_MOTOR);
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor is not null
        defaultCarShouldBeFound("carMotor.specified=true");

        // Get all the carList where carMotor is null
        defaultCarShouldNotBeFound("carMotor.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor contains DEFAULT_CAR_MOTOR
        defaultCarShouldBeFound("carMotor.contains=" + DEFAULT_CAR_MOTOR);

        // Get all the carList where carMotor contains UPDATED_CAR_MOTOR
        defaultCarShouldNotBeFound("carMotor.contains=" + UPDATED_CAR_MOTOR);
    }

    @Test
    @Transactional
    void getAllCarsByCarMotorNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carMotor does not contain DEFAULT_CAR_MOTOR
        defaultCarShouldNotBeFound("carMotor.doesNotContain=" + DEFAULT_CAR_MOTOR);

        // Get all the carList where carMotor does not contain UPDATED_CAR_MOTOR
        defaultCarShouldBeFound("carMotor.doesNotContain=" + UPDATED_CAR_MOTOR);
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber equals to DEFAULT_CAR_SHELL_NUMBER
        defaultCarShouldBeFound("carShellNumber.equals=" + DEFAULT_CAR_SHELL_NUMBER);

        // Get all the carList where carShellNumber equals to UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldNotBeFound("carShellNumber.equals=" + UPDATED_CAR_SHELL_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber not equals to DEFAULT_CAR_SHELL_NUMBER
        defaultCarShouldNotBeFound("carShellNumber.notEquals=" + DEFAULT_CAR_SHELL_NUMBER);

        // Get all the carList where carShellNumber not equals to UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldBeFound("carShellNumber.notEquals=" + UPDATED_CAR_SHELL_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber in DEFAULT_CAR_SHELL_NUMBER or UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldBeFound("carShellNumber.in=" + DEFAULT_CAR_SHELL_NUMBER + "," + UPDATED_CAR_SHELL_NUMBER);

        // Get all the carList where carShellNumber equals to UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldNotBeFound("carShellNumber.in=" + UPDATED_CAR_SHELL_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber is not null
        defaultCarShouldBeFound("carShellNumber.specified=true");

        // Get all the carList where carShellNumber is null
        defaultCarShouldNotBeFound("carShellNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber contains DEFAULT_CAR_SHELL_NUMBER
        defaultCarShouldBeFound("carShellNumber.contains=" + DEFAULT_CAR_SHELL_NUMBER);

        // Get all the carList where carShellNumber contains UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldNotBeFound("carShellNumber.contains=" + UPDATED_CAR_SHELL_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByCarShellNumberNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carShellNumber does not contain DEFAULT_CAR_SHELL_NUMBER
        defaultCarShouldNotBeFound("carShellNumber.doesNotContain=" + DEFAULT_CAR_SHELL_NUMBER);

        // Get all the carList where carShellNumber does not contain UPDATED_CAR_SHELL_NUMBER
        defaultCarShouldBeFound("carShellNumber.doesNotContain=" + UPDATED_CAR_SHELL_NUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification equals to DEFAULT_CLASSIFICATION
        defaultCarShouldBeFound("classification.equals=" + DEFAULT_CLASSIFICATION);

        // Get all the carList where classification equals to UPDATED_CLASSIFICATION
        defaultCarShouldNotBeFound("classification.equals=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllCarsByClassificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification not equals to DEFAULT_CLASSIFICATION
        defaultCarShouldNotBeFound("classification.notEquals=" + DEFAULT_CLASSIFICATION);

        // Get all the carList where classification not equals to UPDATED_CLASSIFICATION
        defaultCarShouldBeFound("classification.notEquals=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllCarsByClassificationIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification in DEFAULT_CLASSIFICATION or UPDATED_CLASSIFICATION
        defaultCarShouldBeFound("classification.in=" + DEFAULT_CLASSIFICATION + "," + UPDATED_CLASSIFICATION);

        // Get all the carList where classification equals to UPDATED_CLASSIFICATION
        defaultCarShouldNotBeFound("classification.in=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllCarsByClassificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification is not null
        defaultCarShouldBeFound("classification.specified=true");

        // Get all the carList where classification is null
        defaultCarShouldNotBeFound("classification.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByClassificationContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification contains DEFAULT_CLASSIFICATION
        defaultCarShouldBeFound("classification.contains=" + DEFAULT_CLASSIFICATION);

        // Get all the carList where classification contains UPDATED_CLASSIFICATION
        defaultCarShouldNotBeFound("classification.contains=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllCarsByClassificationNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where classification does not contain DEFAULT_CLASSIFICATION
        defaultCarShouldNotBeFound("classification.doesNotContain=" + DEFAULT_CLASSIFICATION);

        // Get all the carList where classification does not contain UPDATED_CLASSIFICATION
        defaultCarShouldBeFound("classification.doesNotContain=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllCarsByMadeinIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein equals to DEFAULT_MADEIN
        defaultCarShouldBeFound("madein.equals=" + DEFAULT_MADEIN);

        // Get all the carList where madein equals to UPDATED_MADEIN
        defaultCarShouldNotBeFound("madein.equals=" + UPDATED_MADEIN);
    }

    @Test
    @Transactional
    void getAllCarsByMadeinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein not equals to DEFAULT_MADEIN
        defaultCarShouldNotBeFound("madein.notEquals=" + DEFAULT_MADEIN);

        // Get all the carList where madein not equals to UPDATED_MADEIN
        defaultCarShouldBeFound("madein.notEquals=" + UPDATED_MADEIN);
    }

    @Test
    @Transactional
    void getAllCarsByMadeinIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein in DEFAULT_MADEIN or UPDATED_MADEIN
        defaultCarShouldBeFound("madein.in=" + DEFAULT_MADEIN + "," + UPDATED_MADEIN);

        // Get all the carList where madein equals to UPDATED_MADEIN
        defaultCarShouldNotBeFound("madein.in=" + UPDATED_MADEIN);
    }

    @Test
    @Transactional
    void getAllCarsByMadeinIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein is not null
        defaultCarShouldBeFound("madein.specified=true");

        // Get all the carList where madein is null
        defaultCarShouldNotBeFound("madein.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByMadeinContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein contains DEFAULT_MADEIN
        defaultCarShouldBeFound("madein.contains=" + DEFAULT_MADEIN);

        // Get all the carList where madein contains UPDATED_MADEIN
        defaultCarShouldNotBeFound("madein.contains=" + UPDATED_MADEIN);
    }

    @Test
    @Transactional
    void getAllCarsByMadeinNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where madein does not contain DEFAULT_MADEIN
        defaultCarShouldNotBeFound("madein.doesNotContain=" + DEFAULT_MADEIN);

        // Get all the carList where madein does not contain UPDATED_MADEIN
        defaultCarShouldBeFound("madein.doesNotContain=" + UPDATED_MADEIN);
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber equals to DEFAULT_PANAELNUMBER
        defaultCarShouldBeFound("panaelnumber.equals=" + DEFAULT_PANAELNUMBER);

        // Get all the carList where panaelnumber equals to UPDATED_PANAELNUMBER
        defaultCarShouldNotBeFound("panaelnumber.equals=" + UPDATED_PANAELNUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber not equals to DEFAULT_PANAELNUMBER
        defaultCarShouldNotBeFound("panaelnumber.notEquals=" + DEFAULT_PANAELNUMBER);

        // Get all the carList where panaelnumber not equals to UPDATED_PANAELNUMBER
        defaultCarShouldBeFound("panaelnumber.notEquals=" + UPDATED_PANAELNUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber in DEFAULT_PANAELNUMBER or UPDATED_PANAELNUMBER
        defaultCarShouldBeFound("panaelnumber.in=" + DEFAULT_PANAELNUMBER + "," + UPDATED_PANAELNUMBER);

        // Get all the carList where panaelnumber equals to UPDATED_PANAELNUMBER
        defaultCarShouldNotBeFound("panaelnumber.in=" + UPDATED_PANAELNUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber is not null
        defaultCarShouldBeFound("panaelnumber.specified=true");

        // Get all the carList where panaelnumber is null
        defaultCarShouldNotBeFound("panaelnumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber contains DEFAULT_PANAELNUMBER
        defaultCarShouldBeFound("panaelnumber.contains=" + DEFAULT_PANAELNUMBER);

        // Get all the carList where panaelnumber contains UPDATED_PANAELNUMBER
        defaultCarShouldNotBeFound("panaelnumber.contains=" + UPDATED_PANAELNUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByPanaelnumberNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where panaelnumber does not contain DEFAULT_PANAELNUMBER
        defaultCarShouldNotBeFound("panaelnumber.doesNotContain=" + DEFAULT_PANAELNUMBER);

        // Get all the carList where panaelnumber does not contain UPDATED_PANAELNUMBER
        defaultCarShouldBeFound("panaelnumber.doesNotContain=" + UPDATED_PANAELNUMBER);
    }

    @Test
    @Transactional
    void getAllCarsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes equals to DEFAULT_NOTES
        defaultCarShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the carList where notes equals to UPDATED_NOTES
        defaultCarShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCarsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes not equals to DEFAULT_NOTES
        defaultCarShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the carList where notes not equals to UPDATED_NOTES
        defaultCarShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCarsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultCarShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the carList where notes equals to UPDATED_NOTES
        defaultCarShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCarsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes is not null
        defaultCarShouldBeFound("notes.specified=true");

        // Get all the carList where notes is null
        defaultCarShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByNotesContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes contains DEFAULT_NOTES
        defaultCarShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the carList where notes contains UPDATED_NOTES
        defaultCarShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCarsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where notes does not contain DEFAULT_NOTES
        defaultCarShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the carList where notes does not contain UPDATED_NOTES
        defaultCarShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllCarsByDispatchIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Dispatch dispatch;
        if (TestUtil.findAll(em, Dispatch.class).isEmpty()) {
            dispatch = DispatchResourceIT.createEntity(em);
            em.persist(dispatch);
            em.flush();
        } else {
            dispatch = TestUtil.findAll(em, Dispatch.class).get(0);
        }
        em.persist(dispatch);
        em.flush();
        car.addDispatch(dispatch);
        carRepository.saveAndFlush(car);
        Long dispatchId = dispatch.getId();

        // Get all the carList where dispatch equals to dispatchId
        defaultCarShouldBeFound("dispatchId.equals=" + dispatchId);

        // Get all the carList where dispatch equals to (dispatchId + 1)
        defaultCarShouldNotBeFound("dispatchId.equals=" + (dispatchId + 1));
    }

    @Test
    @Transactional
    void getAllCarsByFailureIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Failure failure;
        if (TestUtil.findAll(em, Failure.class).isEmpty()) {
            failure = FailureResourceIT.createEntity(em);
            em.persist(failure);
            em.flush();
        } else {
            failure = TestUtil.findAll(em, Failure.class).get(0);
        }
        em.persist(failure);
        em.flush();
        car.addFailure(failure);
        carRepository.saveAndFlush(car);
        Long failureId = failure.getId();

        // Get all the carList where failure equals to failureId
        defaultCarShouldBeFound("failureId.equals=" + failureId);

        // Get all the carList where failure equals to (failureId + 1)
        defaultCarShouldNotBeFound("failureId.equals=" + (failureId + 1));
    }

    @Test
    @Transactional
    void getAllCarsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        car.setEmployee(employee);
        employee.setCar(car);
        carRepository.saveAndFlush(car);
        Long employeeId = employee.getId();

        // Get all the carList where employee equals to employeeId
        defaultCarShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the carList where employee equals to (employeeId + 1)
        defaultCarShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].carIssue").value(hasItem(DEFAULT_CAR_ISSUE)))
            .andExpect(jsonPath("$.[*].carnNumber").value(hasItem(DEFAULT_CARN_NUMBER)))
            .andExpect(jsonPath("$.[*].carMotor").value(hasItem(DEFAULT_CAR_MOTOR)))
            .andExpect(jsonPath("$.[*].carShellNumber").value(hasItem(DEFAULT_CAR_SHELL_NUMBER)))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].madein").value(hasItem(DEFAULT_MADEIN)))
            .andExpect(jsonPath("$.[*].panaelnumber").value(hasItem(DEFAULT_PANAELNUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .name(UPDATED_NAME)
            .carIssue(UPDATED_CAR_ISSUE)
            .carnNumber(UPDATED_CARN_NUMBER)
            .carMotor(UPDATED_CAR_MOTOR)
            .carShellNumber(UPDATED_CAR_SHELL_NUMBER)
            .classification(UPDATED_CLASSIFICATION)
            .madein(UPDATED_MADEIN)
            .panaelnumber(UPDATED_PANAELNUMBER)
            .notes(UPDATED_NOTES);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getCarIssue()).isEqualTo(UPDATED_CAR_ISSUE);
        assertThat(testCar.getCarnNumber()).isEqualTo(UPDATED_CARN_NUMBER);
        assertThat(testCar.getCarMotor()).isEqualTo(UPDATED_CAR_MOTOR);
        assertThat(testCar.getCarShellNumber()).isEqualTo(UPDATED_CAR_SHELL_NUMBER);
        assertThat(testCar.getClassification()).isEqualTo(UPDATED_CLASSIFICATION);
        assertThat(testCar.getMadein()).isEqualTo(UPDATED_MADEIN);
        assertThat(testCar.getPanaelnumber()).isEqualTo(UPDATED_PANAELNUMBER);
        assertThat(testCar.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, car.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .name(UPDATED_NAME)
            .carIssue(UPDATED_CAR_ISSUE)
            .carnNumber(UPDATED_CARN_NUMBER)
            .carMotor(UPDATED_CAR_MOTOR)
            .carShellNumber(UPDATED_CAR_SHELL_NUMBER)
            .madein(UPDATED_MADEIN)
            .notes(UPDATED_NOTES);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getCarIssue()).isEqualTo(UPDATED_CAR_ISSUE);
        assertThat(testCar.getCarnNumber()).isEqualTo(UPDATED_CARN_NUMBER);
        assertThat(testCar.getCarMotor()).isEqualTo(UPDATED_CAR_MOTOR);
        assertThat(testCar.getCarShellNumber()).isEqualTo(UPDATED_CAR_SHELL_NUMBER);
        assertThat(testCar.getClassification()).isEqualTo(DEFAULT_CLASSIFICATION);
        assertThat(testCar.getMadein()).isEqualTo(UPDATED_MADEIN);
        assertThat(testCar.getPanaelnumber()).isEqualTo(DEFAULT_PANAELNUMBER);
        assertThat(testCar.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .name(UPDATED_NAME)
            .carIssue(UPDATED_CAR_ISSUE)
            .carnNumber(UPDATED_CARN_NUMBER)
            .carMotor(UPDATED_CAR_MOTOR)
            .carShellNumber(UPDATED_CAR_SHELL_NUMBER)
            .classification(UPDATED_CLASSIFICATION)
            .madein(UPDATED_MADEIN)
            .panaelnumber(UPDATED_PANAELNUMBER)
            .notes(UPDATED_NOTES);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getCarIssue()).isEqualTo(UPDATED_CAR_ISSUE);
        assertThat(testCar.getCarnNumber()).isEqualTo(UPDATED_CARN_NUMBER);
        assertThat(testCar.getCarMotor()).isEqualTo(UPDATED_CAR_MOTOR);
        assertThat(testCar.getCarShellNumber()).isEqualTo(UPDATED_CAR_SHELL_NUMBER);
        assertThat(testCar.getClassification()).isEqualTo(UPDATED_CLASSIFICATION);
        assertThat(testCar.getMadein()).isEqualTo(UPDATED_MADEIN);
        assertThat(testCar.getPanaelnumber()).isEqualTo(UPDATED_PANAELNUMBER);
        assertThat(testCar.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, car.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

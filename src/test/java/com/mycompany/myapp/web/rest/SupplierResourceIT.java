package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.domain.Supplier;
import com.mycompany.myapp.repository.SupplierRepository;
import com.mycompany.myapp.service.criteria.SupplierCriteria;
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

/**
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupplierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SUPPLIERDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUPPLIERDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SUPPLIERDATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_ITEM = "AAAAAAAAAA";
    private static final String UPDATED_ITEM = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .name(DEFAULT_NAME)
            .supplierdate(DEFAULT_SUPPLIERDATE)
            .quantity(DEFAULT_QUANTITY)
            .item(DEFAULT_ITEM)
            .note(DEFAULT_NOTE);
        return supplier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .name(UPDATED_NAME)
            .supplierdate(UPDATED_SUPPLIERDATE)
            .quantity(UPDATED_QUANTITY)
            .item(UPDATED_ITEM)
            .note(UPDATED_NOTE);
        return supplier;
    }

    @BeforeEach
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();
        // Create the Supplier
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getSupplierdate()).isEqualTo(DEFAULT_SUPPLIERDATE);
        assertThat(testSupplier.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSupplier.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testSupplier.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createSupplierWithExistingId() throws Exception {
        // Create the Supplier with an existing ID
        supplier.setId(1L);

        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].supplierdate").value(hasItem(DEFAULT_SUPPLIERDATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.supplierdate").value(DEFAULT_SUPPLIERDATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.item").value(DEFAULT_ITEM))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        Long id = supplier.getId();

        defaultSupplierShouldBeFound("id.equals=" + id);
        defaultSupplierShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name equals to DEFAULT_NAME
        defaultSupplierShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name not equals to DEFAULT_NAME
        defaultSupplierShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the supplierList where name not equals to UPDATED_NAME
        defaultSupplierShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSupplierShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name is not null
        defaultSupplierShouldBeFound("name.specified=true");

        // Get all the supplierList where name is null
        defaultSupplierShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByNameContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name contains DEFAULT_NAME
        defaultSupplierShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the supplierList where name contains UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name does not contain DEFAULT_NAME
        defaultSupplierShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the supplierList where name does not contain UPDATED_NAME
        defaultSupplierShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate equals to DEFAULT_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.equals=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate equals to UPDATED_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.equals=" + UPDATED_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate not equals to DEFAULT_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.notEquals=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate not equals to UPDATED_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.notEquals=" + UPDATED_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate in DEFAULT_SUPPLIERDATE or UPDATED_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.in=" + DEFAULT_SUPPLIERDATE + "," + UPDATED_SUPPLIERDATE);

        // Get all the supplierList where supplierdate equals to UPDATED_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.in=" + UPDATED_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate is not null
        defaultSupplierShouldBeFound("supplierdate.specified=true");

        // Get all the supplierList where supplierdate is null
        defaultSupplierShouldNotBeFound("supplierdate.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate is greater than or equal to DEFAULT_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.greaterThanOrEqual=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate is greater than or equal to UPDATED_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.greaterThanOrEqual=" + UPDATED_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate is less than or equal to DEFAULT_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.lessThanOrEqual=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate is less than or equal to SMALLER_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.lessThanOrEqual=" + SMALLER_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate is less than DEFAULT_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.lessThan=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate is less than UPDATED_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.lessThan=" + UPDATED_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersBySupplierdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where supplierdate is greater than DEFAULT_SUPPLIERDATE
        defaultSupplierShouldNotBeFound("supplierdate.greaterThan=" + DEFAULT_SUPPLIERDATE);

        // Get all the supplierList where supplierdate is greater than SMALLER_SUPPLIERDATE
        defaultSupplierShouldBeFound("supplierdate.greaterThan=" + SMALLER_SUPPLIERDATE);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity equals to DEFAULT_QUANTITY
        defaultSupplierShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity equals to UPDATED_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity not equals to DEFAULT_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity not equals to UPDATED_QUANTITY
        defaultSupplierShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultSupplierShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the supplierList where quantity equals to UPDATED_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity is not null
        defaultSupplierShouldBeFound("quantity.specified=true");

        // Get all the supplierList where quantity is null
        defaultSupplierShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultSupplierShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultSupplierShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity is less than or equal to SMALLER_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity is less than DEFAULT_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity is less than UPDATED_QUANTITY
        defaultSupplierShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where quantity is greater than DEFAULT_QUANTITY
        defaultSupplierShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the supplierList where quantity is greater than SMALLER_QUANTITY
        defaultSupplierShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllSuppliersByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item equals to DEFAULT_ITEM
        defaultSupplierShouldBeFound("item.equals=" + DEFAULT_ITEM);

        // Get all the supplierList where item equals to UPDATED_ITEM
        defaultSupplierShouldNotBeFound("item.equals=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllSuppliersByItemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item not equals to DEFAULT_ITEM
        defaultSupplierShouldNotBeFound("item.notEquals=" + DEFAULT_ITEM);

        // Get all the supplierList where item not equals to UPDATED_ITEM
        defaultSupplierShouldBeFound("item.notEquals=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllSuppliersByItemIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item in DEFAULT_ITEM or UPDATED_ITEM
        defaultSupplierShouldBeFound("item.in=" + DEFAULT_ITEM + "," + UPDATED_ITEM);

        // Get all the supplierList where item equals to UPDATED_ITEM
        defaultSupplierShouldNotBeFound("item.in=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllSuppliersByItemIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item is not null
        defaultSupplierShouldBeFound("item.specified=true");

        // Get all the supplierList where item is null
        defaultSupplierShouldNotBeFound("item.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByItemContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item contains DEFAULT_ITEM
        defaultSupplierShouldBeFound("item.contains=" + DEFAULT_ITEM);

        // Get all the supplierList where item contains UPDATED_ITEM
        defaultSupplierShouldNotBeFound("item.contains=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllSuppliersByItemNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where item does not contain DEFAULT_ITEM
        defaultSupplierShouldNotBeFound("item.doesNotContain=" + DEFAULT_ITEM);

        // Get all the supplierList where item does not contain UPDATED_ITEM
        defaultSupplierShouldBeFound("item.doesNotContain=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note equals to DEFAULT_NOTE
        defaultSupplierShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the supplierList where note equals to UPDATED_NOTE
        defaultSupplierShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note not equals to DEFAULT_NOTE
        defaultSupplierShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the supplierList where note not equals to UPDATED_NOTE
        defaultSupplierShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSupplierShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the supplierList where note equals to UPDATED_NOTE
        defaultSupplierShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note is not null
        defaultSupplierShouldBeFound("note.specified=true");

        // Get all the supplierList where note is null
        defaultSupplierShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note contains DEFAULT_NOTE
        defaultSupplierShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the supplierList where note contains UPDATED_NOTE
        defaultSupplierShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where note does not contain DEFAULT_NOTE
        defaultSupplierShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the supplierList where note does not contain UPDATED_NOTE
        defaultSupplierShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllSuppliersByDispatchIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);
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
        supplier.addDispatch(dispatch);
        supplierRepository.saveAndFlush(supplier);
        Long dispatchId = dispatch.getId();

        // Get all the supplierList where dispatch equals to dispatchId
        defaultSupplierShouldBeFound("dispatchId.equals=" + dispatchId);

        // Get all the supplierList where dispatch equals to (dispatchId + 1)
        defaultSupplierShouldNotBeFound("dispatchId.equals=" + (dispatchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierShouldBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].supplierdate").value(hasItem(DEFAULT_SUPPLIERDATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierShouldNotBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).get();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier
            .name(UPDATED_NAME)
            .supplierdate(UPDATED_SUPPLIERDATE)
            .quantity(UPDATED_QUANTITY)
            .item(UPDATED_ITEM)
            .note(UPDATED_NOTE);

        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getSupplierdate()).isEqualTo(UPDATED_SUPPLIERDATE);
        assertThat(testSupplier.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSupplier.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testSupplier.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier.supplierdate(UPDATED_SUPPLIERDATE).item(UPDATED_ITEM).note(UPDATED_NOTE);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getSupplierdate()).isEqualTo(UPDATED_SUPPLIERDATE);
        assertThat(testSupplier.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSupplier.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testSupplier.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .name(UPDATED_NAME)
            .supplierdate(UPDATED_SUPPLIERDATE)
            .quantity(UPDATED_QUANTITY)
            .item(UPDATED_ITEM)
            .note(UPDATED_NOTE);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getSupplierdate()).isEqualTo(UPDATED_SUPPLIERDATE);
        assertThat(testSupplier.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSupplier.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testSupplier.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Delete the supplier
        restSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

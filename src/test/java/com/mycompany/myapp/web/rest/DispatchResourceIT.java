package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.domain.Supplier;
import com.mycompany.myapp.repository.DispatchRepository;
import com.mycompany.myapp.service.criteria.DispatchCriteria;
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
 * Integration tests for the {@link DispatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispatchResourceIT {

    private static final LocalDate DEFAULT_DISPATCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISPATCH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DISPATCH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ITEM = "AAAAAAAAAA";
    private static final String UPDATED_ITEM = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dispatches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispatchRepository dispatchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispatchMockMvc;

    private Dispatch dispatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispatch createEntity(EntityManager em) {
        Dispatch dispatch = new Dispatch().dispatchDate(DEFAULT_DISPATCH_DATE).item(DEFAULT_ITEM).quantity(DEFAULT_QUANTITY);
        return dispatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispatch createUpdatedEntity(EntityManager em) {
        Dispatch dispatch = new Dispatch().dispatchDate(UPDATED_DISPATCH_DATE).item(UPDATED_ITEM).quantity(UPDATED_QUANTITY);
        return dispatch;
    }

    @BeforeEach
    public void initTest() {
        dispatch = createEntity(em);
    }

    @Test
    @Transactional
    void createDispatch() throws Exception {
        int databaseSizeBeforeCreate = dispatchRepository.findAll().size();
        // Create the Dispatch
        restDispatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatch)))
            .andExpect(status().isCreated());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeCreate + 1);
        Dispatch testDispatch = dispatchList.get(dispatchList.size() - 1);
        assertThat(testDispatch.getDispatchDate()).isEqualTo(DEFAULT_DISPATCH_DATE);
        assertThat(testDispatch.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testDispatch.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createDispatchWithExistingId() throws Exception {
        // Create the Dispatch with an existing ID
        dispatch.setId(1L);

        int databaseSizeBeforeCreate = dispatchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatch)))
            .andExpect(status().isBadRequest());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDispatches() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].dispatchDate").value(hasItem(DEFAULT_DISPATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getDispatch() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get the dispatch
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL_ID, dispatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispatch.getId().intValue()))
            .andExpect(jsonPath("$.dispatchDate").value(DEFAULT_DISPATCH_DATE.toString()))
            .andExpect(jsonPath("$.item").value(DEFAULT_ITEM))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getDispatchesByIdFiltering() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        Long id = dispatch.getId();

        defaultDispatchShouldBeFound("id.equals=" + id);
        defaultDispatchShouldNotBeFound("id.notEquals=" + id);

        defaultDispatchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDispatchShouldNotBeFound("id.greaterThan=" + id);

        defaultDispatchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDispatchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate equals to DEFAULT_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.equals=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate equals to UPDATED_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.equals=" + UPDATED_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate not equals to DEFAULT_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.notEquals=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate not equals to UPDATED_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.notEquals=" + UPDATED_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsInShouldWork() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate in DEFAULT_DISPATCH_DATE or UPDATED_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.in=" + DEFAULT_DISPATCH_DATE + "," + UPDATED_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate equals to UPDATED_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.in=" + UPDATED_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate is not null
        defaultDispatchShouldBeFound("dispatchDate.specified=true");

        // Get all the dispatchList where dispatchDate is null
        defaultDispatchShouldNotBeFound("dispatchDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate is greater than or equal to DEFAULT_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.greaterThanOrEqual=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate is greater than or equal to UPDATED_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.greaterThanOrEqual=" + UPDATED_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate is less than or equal to DEFAULT_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.lessThanOrEqual=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate is less than or equal to SMALLER_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.lessThanOrEqual=" + SMALLER_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsLessThanSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate is less than DEFAULT_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.lessThan=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate is less than UPDATED_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.lessThan=" + UPDATED_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByDispatchDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where dispatchDate is greater than DEFAULT_DISPATCH_DATE
        defaultDispatchShouldNotBeFound("dispatchDate.greaterThan=" + DEFAULT_DISPATCH_DATE);

        // Get all the dispatchList where dispatchDate is greater than SMALLER_DISPATCH_DATE
        defaultDispatchShouldBeFound("dispatchDate.greaterThan=" + SMALLER_DISPATCH_DATE);
    }

    @Test
    @Transactional
    void getAllDispatchesByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item equals to DEFAULT_ITEM
        defaultDispatchShouldBeFound("item.equals=" + DEFAULT_ITEM);

        // Get all the dispatchList where item equals to UPDATED_ITEM
        defaultDispatchShouldNotBeFound("item.equals=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllDispatchesByItemIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item not equals to DEFAULT_ITEM
        defaultDispatchShouldNotBeFound("item.notEquals=" + DEFAULT_ITEM);

        // Get all the dispatchList where item not equals to UPDATED_ITEM
        defaultDispatchShouldBeFound("item.notEquals=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllDispatchesByItemIsInShouldWork() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item in DEFAULT_ITEM or UPDATED_ITEM
        defaultDispatchShouldBeFound("item.in=" + DEFAULT_ITEM + "," + UPDATED_ITEM);

        // Get all the dispatchList where item equals to UPDATED_ITEM
        defaultDispatchShouldNotBeFound("item.in=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllDispatchesByItemIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item is not null
        defaultDispatchShouldBeFound("item.specified=true");

        // Get all the dispatchList where item is null
        defaultDispatchShouldNotBeFound("item.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchesByItemContainsSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item contains DEFAULT_ITEM
        defaultDispatchShouldBeFound("item.contains=" + DEFAULT_ITEM);

        // Get all the dispatchList where item contains UPDATED_ITEM
        defaultDispatchShouldNotBeFound("item.contains=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllDispatchesByItemNotContainsSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where item does not contain DEFAULT_ITEM
        defaultDispatchShouldNotBeFound("item.doesNotContain=" + DEFAULT_ITEM);

        // Get all the dispatchList where item does not contain UPDATED_ITEM
        defaultDispatchShouldBeFound("item.doesNotContain=" + UPDATED_ITEM);
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity equals to DEFAULT_QUANTITY
        defaultDispatchShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the dispatchList where quantity equals to UPDATED_QUANTITY
        defaultDispatchShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity not equals to DEFAULT_QUANTITY
        defaultDispatchShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the dispatchList where quantity not equals to UPDATED_QUANTITY
        defaultDispatchShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultDispatchShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the dispatchList where quantity equals to UPDATED_QUANTITY
        defaultDispatchShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity is not null
        defaultDispatchShouldBeFound("quantity.specified=true");

        // Get all the dispatchList where quantity is null
        defaultDispatchShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityContainsSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity contains DEFAULT_QUANTITY
        defaultDispatchShouldBeFound("quantity.contains=" + DEFAULT_QUANTITY);

        // Get all the dispatchList where quantity contains UPDATED_QUANTITY
        defaultDispatchShouldNotBeFound("quantity.contains=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDispatchesByQuantityNotContainsSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        // Get all the dispatchList where quantity does not contain DEFAULT_QUANTITY
        defaultDispatchShouldNotBeFound("quantity.doesNotContain=" + DEFAULT_QUANTITY);

        // Get all the dispatchList where quantity does not contain UPDATED_QUANTITY
        defaultDispatchShouldBeFound("quantity.doesNotContain=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDispatchesByCarIsEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);
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
        dispatch.setCar(car);
        dispatchRepository.saveAndFlush(dispatch);
        Long carId = car.getId();

        // Get all the dispatchList where car equals to carId
        defaultDispatchShouldBeFound("carId.equals=" + carId);

        // Get all the dispatchList where car equals to (carId + 1)
        defaultDispatchShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    @Test
    @Transactional
    void getAllDispatchesBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            supplier = SupplierResourceIT.createEntity(em);
            em.persist(supplier);
            em.flush();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        dispatch.setSupplier(supplier);
        dispatchRepository.saveAndFlush(dispatch);
        Long supplierId = supplier.getId();

        // Get all the dispatchList where supplier equals to supplierId
        defaultDispatchShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the dispatchList where supplier equals to (supplierId + 1)
        defaultDispatchShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDispatchShouldBeFound(String filter) throws Exception {
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].dispatchDate").value(hasItem(DEFAULT_DISPATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDispatchShouldNotBeFound(String filter) throws Exception {
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDispatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDispatch() throws Exception {
        // Get the dispatch
        restDispatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDispatch() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();

        // Update the dispatch
        Dispatch updatedDispatch = dispatchRepository.findById(dispatch.getId()).get();
        // Disconnect from session so that the updates on updatedDispatch are not directly saved in db
        em.detach(updatedDispatch);
        updatedDispatch.dispatchDate(UPDATED_DISPATCH_DATE).item(UPDATED_ITEM).quantity(UPDATED_QUANTITY);

        restDispatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDispatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDispatch))
            )
            .andExpect(status().isOk());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
        Dispatch testDispatch = dispatchList.get(dispatchList.size() - 1);
        assertThat(testDispatch.getDispatchDate()).isEqualTo(UPDATED_DISPATCH_DATE);
        assertThat(testDispatch.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testDispatch.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispatch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispatchWithPatch() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();

        // Update the dispatch using partial update
        Dispatch partialUpdatedDispatch = new Dispatch();
        partialUpdatedDispatch.setId(dispatch.getId());

        partialUpdatedDispatch.dispatchDate(UPDATED_DISPATCH_DATE).quantity(UPDATED_QUANTITY);

        restDispatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispatch))
            )
            .andExpect(status().isOk());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
        Dispatch testDispatch = dispatchList.get(dispatchList.size() - 1);
        assertThat(testDispatch.getDispatchDate()).isEqualTo(UPDATED_DISPATCH_DATE);
        assertThat(testDispatch.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testDispatch.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateDispatchWithPatch() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();

        // Update the dispatch using partial update
        Dispatch partialUpdatedDispatch = new Dispatch();
        partialUpdatedDispatch.setId(dispatch.getId());

        partialUpdatedDispatch.dispatchDate(UPDATED_DISPATCH_DATE).item(UPDATED_ITEM).quantity(UPDATED_QUANTITY);

        restDispatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispatch))
            )
            .andExpect(status().isOk());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
        Dispatch testDispatch = dispatchList.get(dispatchList.size() - 1);
        assertThat(testDispatch.getDispatchDate()).isEqualTo(UPDATED_DISPATCH_DATE);
        assertThat(testDispatch.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testDispatch.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispatch() throws Exception {
        int databaseSizeBeforeUpdate = dispatchRepository.findAll().size();
        dispatch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dispatch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispatch in the database
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispatch() throws Exception {
        // Initialize the database
        dispatchRepository.saveAndFlush(dispatch);

        int databaseSizeBeforeDelete = dispatchRepository.findAll().size();

        // Delete the dispatch
        restDispatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispatch> dispatchList = dispatchRepository.findAll();
        assertThat(dispatchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

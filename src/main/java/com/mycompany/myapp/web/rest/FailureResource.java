package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Failure;
import com.mycompany.myapp.repository.FailureRepository;
import com.mycompany.myapp.service.FailureQueryService;
import com.mycompany.myapp.service.FailureService;
import com.mycompany.myapp.service.criteria.FailureCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Failure}.
 */
@RestController
@RequestMapping("/api")
public class FailureResource {

    private final Logger log = LoggerFactory.getLogger(FailureResource.class);

    private static final String ENTITY_NAME = "failure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FailureService failureService;

    private final FailureRepository failureRepository;

    private final FailureQueryService failureQueryService;

    public FailureResource(FailureService failureService, FailureRepository failureRepository, FailureQueryService failureQueryService) {
        this.failureService = failureService;
        this.failureRepository = failureRepository;
        this.failureQueryService = failureQueryService;
    }

    /**
     * {@code POST  /failures} : Create a new failure.
     *
     * @param failure the failure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new failure, or with status {@code 400 (Bad Request)} if the failure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/failures")
    public ResponseEntity<Failure> createFailure(@RequestBody Failure failure) throws URISyntaxException {
        log.debug("REST request to save Failure : {}", failure);
        if (failure.getId() != null) {
            throw new BadRequestAlertException("A new failure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Failure result = failureService.save(failure);
        return ResponseEntity
            .created(new URI("/api/failures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /failures/:id} : Updates an existing failure.
     *
     * @param id the id of the failure to save.
     * @param failure the failure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failure,
     * or with status {@code 400 (Bad Request)} if the failure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the failure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/failures/{id}")
    public ResponseEntity<Failure> updateFailure(@PathVariable(value = "id", required = false) final Long id, @RequestBody Failure failure)
        throws URISyntaxException {
        log.debug("REST request to update Failure : {}, {}", id, failure);
        if (failure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Failure result = failureService.save(failure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, failure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /failures/:id} : Partial updates given fields of an existing failure, field will ignore if it is null
     *
     * @param id the id of the failure to save.
     * @param failure the failure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failure,
     * or with status {@code 400 (Bad Request)} if the failure is not valid,
     * or with status {@code 404 (Not Found)} if the failure is not found,
     * or with status {@code 500 (Internal Server Error)} if the failure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/failures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Failure> partialUpdateFailure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Failure failure
    ) throws URISyntaxException {
        log.debug("REST request to partial update Failure partially : {}, {}", id, failure);
        if (failure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Failure> result = failureService.partialUpdate(failure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, failure.getId().toString())
        );
    }

    /**
     * {@code GET  /failures} : get all the failures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of failures in body.
     */
    @GetMapping("/failures")
    public ResponseEntity<List<Failure>> getAllFailures(FailureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Failures by criteria: {}", criteria);
        Page<Failure> page = failureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /failures/count} : count all the failures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/failures/count")
    public ResponseEntity<Long> countFailures(FailureCriteria criteria) {
        log.debug("REST request to count Failures by criteria: {}", criteria);
        return ResponseEntity.ok().body(failureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /failures/:id} : get the "id" failure.
     *
     * @param id the id of the failure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the failure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/failures/{id}")
    public ResponseEntity<Failure> getFailure(@PathVariable Long id) {
        log.debug("REST request to get Failure : {}", id);
        Optional<Failure> failure = failureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(failure);
    }

    /**
     * {@code DELETE  /failures/:id} : delete the "id" failure.
     *
     * @param id the id of the failure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/failures/{id}")
    public ResponseEntity<Void> deleteFailure(@PathVariable Long id) {
        log.debug("REST request to delete Failure : {}", id);
        failureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

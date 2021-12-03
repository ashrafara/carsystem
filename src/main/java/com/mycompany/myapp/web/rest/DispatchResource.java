package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.repository.DispatchRepository;
import com.mycompany.myapp.service.DispatchQueryService;
import com.mycompany.myapp.service.DispatchService;
import com.mycompany.myapp.service.criteria.DispatchCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Dispatch}.
 */
@RestController
@RequestMapping("/api")
public class DispatchResource {

    private final Logger log = LoggerFactory.getLogger(DispatchResource.class);

    private static final String ENTITY_NAME = "dispatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispatchService dispatchService;

    private final DispatchRepository dispatchRepository;

    private final DispatchQueryService dispatchQueryService;

    public DispatchResource(
        DispatchService dispatchService,
        DispatchRepository dispatchRepository,
        DispatchQueryService dispatchQueryService
    ) {
        this.dispatchService = dispatchService;
        this.dispatchRepository = dispatchRepository;
        this.dispatchQueryService = dispatchQueryService;
    }

    /**
     * {@code POST  /dispatches} : Create a new dispatch.
     *
     * @param dispatch the dispatch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispatch, or with status {@code 400 (Bad Request)} if the dispatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dispatches")
    public ResponseEntity<Dispatch> createDispatch(@RequestBody Dispatch dispatch) throws URISyntaxException {
        log.debug("REST request to save Dispatch : {}", dispatch);
        if (dispatch.getId() != null) {
            throw new BadRequestAlertException("A new dispatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dispatch result = dispatchService.save(dispatch);
        return ResponseEntity
            .created(new URI("/api/dispatches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dispatches/:id} : Updates an existing dispatch.
     *
     * @param id the id of the dispatch to save.
     * @param dispatch the dispatch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatch,
     * or with status {@code 400 (Bad Request)} if the dispatch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispatch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dispatches/{id}")
    public ResponseEntity<Dispatch> updateDispatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispatch dispatch
    ) throws URISyntaxException {
        log.debug("REST request to update Dispatch : {}, {}", id, dispatch);
        if (dispatch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dispatch result = dispatchService.save(dispatch);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatch.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dispatches/:id} : Partial updates given fields of an existing dispatch, field will ignore if it is null
     *
     * @param id the id of the dispatch to save.
     * @param dispatch the dispatch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispatch,
     * or with status {@code 400 (Bad Request)} if the dispatch is not valid,
     * or with status {@code 404 (Not Found)} if the dispatch is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispatch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dispatches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dispatch> partialUpdateDispatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispatch dispatch
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispatch partially : {}, {}", id, dispatch);
        if (dispatch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispatch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dispatch> result = dispatchService.partialUpdate(dispatch);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dispatch.getId().toString())
        );
    }

    /**
     * {@code GET  /dispatches} : get all the dispatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispatches in body.
     */
    @GetMapping("/dispatches")
    public ResponseEntity<List<Dispatch>> getAllDispatches(DispatchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dispatches by criteria: {}", criteria);
        Page<Dispatch> page = dispatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispatches/count} : count all the dispatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dispatches/count")
    public ResponseEntity<Long> countDispatches(DispatchCriteria criteria) {
        log.debug("REST request to count Dispatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(dispatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dispatches/:id} : get the "id" dispatch.
     *
     * @param id the id of the dispatch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispatch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dispatches/{id}")
    public ResponseEntity<Dispatch> getDispatch(@PathVariable Long id) {
        log.debug("REST request to get Dispatch : {}", id);
        Optional<Dispatch> dispatch = dispatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispatch);
    }

    /**
     * {@code DELETE  /dispatches/:id} : delete the "id" dispatch.
     *
     * @param id the id of the dispatch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dispatches/{id}")
    public ResponseEntity<Void> deleteDispatch(@PathVariable Long id) {
        log.debug("REST request to delete Dispatch : {}", id);
        dispatchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

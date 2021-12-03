package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.repository.DispatchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dispatch}.
 */
@Service
@Transactional
public class DispatchService {

    private final Logger log = LoggerFactory.getLogger(DispatchService.class);

    private final DispatchRepository dispatchRepository;

    public DispatchService(DispatchRepository dispatchRepository) {
        this.dispatchRepository = dispatchRepository;
    }

    /**
     * Save a dispatch.
     *
     * @param dispatch the entity to save.
     * @return the persisted entity.
     */
    public Dispatch save(Dispatch dispatch) {
        log.debug("Request to save Dispatch : {}", dispatch);
        return dispatchRepository.save(dispatch);
    }

    /**
     * Partially update a dispatch.
     *
     * @param dispatch the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Dispatch> partialUpdate(Dispatch dispatch) {
        log.debug("Request to partially update Dispatch : {}", dispatch);

        return dispatchRepository
            .findById(dispatch.getId())
            .map(existingDispatch -> {
                if (dispatch.getDispatchDate() != null) {
                    existingDispatch.setDispatchDate(dispatch.getDispatchDate());
                }
                if (dispatch.getItem() != null) {
                    existingDispatch.setItem(dispatch.getItem());
                }
                if (dispatch.getQuantity() != null) {
                    existingDispatch.setQuantity(dispatch.getQuantity());
                }

                return existingDispatch;
            })
            .map(dispatchRepository::save);
    }

    /**
     * Get all the dispatches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Dispatch> findAll(Pageable pageable) {
        log.debug("Request to get all Dispatches");
        return dispatchRepository.findAll(pageable);
    }

    /**
     * Get one dispatch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Dispatch> findOne(Long id) {
        log.debug("Request to get Dispatch : {}", id);
        return dispatchRepository.findById(id);
    }

    /**
     * Delete the dispatch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dispatch : {}", id);
        dispatchRepository.deleteById(id);
    }
}

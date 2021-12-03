package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Failure;
import com.mycompany.myapp.repository.FailureRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Failure}.
 */
@Service
@Transactional
public class FailureService {

    private final Logger log = LoggerFactory.getLogger(FailureService.class);

    private final FailureRepository failureRepository;

    public FailureService(FailureRepository failureRepository) {
        this.failureRepository = failureRepository;
    }

    /**
     * Save a failure.
     *
     * @param failure the entity to save.
     * @return the persisted entity.
     */
    public Failure save(Failure failure) {
        log.debug("Request to save Failure : {}", failure);
        return failureRepository.save(failure);
    }

    /**
     * Partially update a failure.
     *
     * @param failure the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Failure> partialUpdate(Failure failure) {
        log.debug("Request to partially update Failure : {}", failure);

        return failureRepository
            .findById(failure.getId())
            .map(existingFailure -> {
                if (failure.getName() != null) {
                    existingFailure.setName(failure.getName());
                }
                if (failure.getFailureDate() != null) {
                    existingFailure.setFailureDate(failure.getFailureDate());
                }
                if (failure.getCarGuagefrom() != null) {
                    existingFailure.setCarGuagefrom(failure.getCarGuagefrom());
                }
                if (failure.getCarGuageTo() != null) {
                    existingFailure.setCarGuageTo(failure.getCarGuageTo());
                }
                if (failure.getChangepart() != null) {
                    existingFailure.setChangepart(failure.getChangepart());
                }
                if (failure.getGarageName() != null) {
                    existingFailure.setGarageName(failure.getGarageName());
                }
                if (failure.getPrice() != null) {
                    existingFailure.setPrice(failure.getPrice());
                }
                if (failure.getInovice1() != null) {
                    existingFailure.setInovice1(failure.getInovice1());
                }
                if (failure.getInovice2() != null) {
                    existingFailure.setInovice2(failure.getInovice2());
                }
                if (failure.getInovice3() != null) {
                    existingFailure.setInovice3(failure.getInovice3());
                }
                if (failure.getInovice4() != null) {
                    existingFailure.setInovice4(failure.getInovice4());
                }
                if (failure.getNote() != null) {
                    existingFailure.setNote(failure.getNote());
                }
                if (failure.getImage() != null) {
                    existingFailure.setImage(failure.getImage());
                }
                if (failure.getImageContentType() != null) {
                    existingFailure.setImageContentType(failure.getImageContentType());
                }

                return existingFailure;
            })
            .map(failureRepository::save);
    }

    /**
     * Get all the failures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Failure> findAll(Pageable pageable) {
        log.debug("Request to get all Failures");
        return failureRepository.findAll(pageable);
    }

    /**
     * Get one failure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Failure> findOne(Long id) {
        log.debug("Request to get Failure : {}", id);
        return failureRepository.findById(id);
    }

    /**
     * Delete the failure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Failure : {}", id);
        failureRepository.deleteById(id);
    }
}

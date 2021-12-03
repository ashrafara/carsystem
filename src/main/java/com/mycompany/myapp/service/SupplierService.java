package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Supplier;
import com.mycompany.myapp.repository.SupplierRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Supplier}.
 */
@Service
@Transactional
public class SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Partially update a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Supplier> partialUpdate(Supplier supplier) {
        log.debug("Request to partially update Supplier : {}", supplier);

        return supplierRepository
            .findById(supplier.getId())
            .map(existingSupplier -> {
                if (supplier.getName() != null) {
                    existingSupplier.setName(supplier.getName());
                }
                if (supplier.getSupplierdate() != null) {
                    existingSupplier.setSupplierdate(supplier.getSupplierdate());
                }
                if (supplier.getQuantity() != null) {
                    existingSupplier.setQuantity(supplier.getQuantity());
                }
                if (supplier.getItem() != null) {
                    existingSupplier.setItem(supplier.getItem());
                }
                if (supplier.getNote() != null) {
                    existingSupplier.setNote(supplier.getNote());
                }

                return existingSupplier;
            })
            .map(supplierRepository::save);
    }

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Supplier> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll(pageable);
    }

    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}

package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Dispatch;
import com.mycompany.myapp.repository.DispatchRepository;
import com.mycompany.myapp.service.criteria.DispatchCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Dispatch} entities in the database.
 * The main input is a {@link DispatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Dispatch} or a {@link Page} of {@link Dispatch} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DispatchQueryService extends QueryService<Dispatch> {

    private final Logger log = LoggerFactory.getLogger(DispatchQueryService.class);

    private final DispatchRepository dispatchRepository;

    public DispatchQueryService(DispatchRepository dispatchRepository) {
        this.dispatchRepository = dispatchRepository;
    }

    /**
     * Return a {@link List} of {@link Dispatch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Dispatch> findByCriteria(DispatchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dispatch> specification = createSpecification(criteria);
        return dispatchRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Dispatch} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Dispatch> findByCriteria(DispatchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dispatch> specification = createSpecification(criteria);
        return dispatchRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DispatchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dispatch> specification = createSpecification(criteria);
        return dispatchRepository.count(specification);
    }

    /**
     * Function to convert {@link DispatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dispatch> createSpecification(DispatchCriteria criteria) {
        Specification<Dispatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dispatch_.id));
            }
            if (criteria.getDispatchDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDispatchDate(), Dispatch_.dispatchDate));
            }
            if (criteria.getItem() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItem(), Dispatch_.item));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuantity(), Dispatch_.quantity));
            }
            if (criteria.getCarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCarId(), root -> root.join(Dispatch_.car, JoinType.LEFT).get(Car_.id))
                    );
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSupplierId(), root -> root.join(Dispatch_.supplier, JoinType.LEFT).get(Supplier_.id))
                    );
            }
        }
        return specification;
    }
}

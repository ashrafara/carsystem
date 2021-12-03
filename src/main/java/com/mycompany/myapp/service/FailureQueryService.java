package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Failure;
import com.mycompany.myapp.repository.FailureRepository;
import com.mycompany.myapp.service.criteria.FailureCriteria;
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
 * Service for executing complex queries for {@link Failure} entities in the database.
 * The main input is a {@link FailureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Failure} or a {@link Page} of {@link Failure} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FailureQueryService extends QueryService<Failure> {

    private final Logger log = LoggerFactory.getLogger(FailureQueryService.class);

    private final FailureRepository failureRepository;

    public FailureQueryService(FailureRepository failureRepository) {
        this.failureRepository = failureRepository;
    }

    /**
     * Return a {@link List} of {@link Failure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Failure> findByCriteria(FailureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Failure> specification = createSpecification(criteria);
        return failureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Failure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Failure> findByCriteria(FailureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Failure> specification = createSpecification(criteria);
        return failureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FailureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Failure> specification = createSpecification(criteria);
        return failureRepository.count(specification);
    }

    /**
     * Function to convert {@link FailureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Failure> createSpecification(FailureCriteria criteria) {
        Specification<Failure> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Failure_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Failure_.name));
            }
            if (criteria.getFailureDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFailureDate(), Failure_.failureDate));
            }
            if (criteria.getCarGuagefrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCarGuagefrom(), Failure_.carGuagefrom));
            }
            if (criteria.getCarGuageTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCarGuageTo(), Failure_.carGuageTo));
            }
            if (criteria.getChangepart() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChangepart(), Failure_.changepart));
            }
            if (criteria.getGarageName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGarageName(), Failure_.garageName));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Failure_.price));
            }
            if (criteria.getInovice1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInovice1(), Failure_.inovice1));
            }
            if (criteria.getInovice2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInovice2(), Failure_.inovice2));
            }
            if (criteria.getInovice3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInovice3(), Failure_.inovice3));
            }
            if (criteria.getInovice4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInovice4(), Failure_.inovice4));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Failure_.note));
            }
            if (criteria.getCarId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCarId(), root -> root.join(Failure_.car, JoinType.LEFT).get(Car_.id)));
            }
        }
        return specification;
    }
}

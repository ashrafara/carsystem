package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.repository.CarRepository;
import com.mycompany.myapp.service.criteria.CarCriteria;
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
 * Service for executing complex queries for {@link Car} entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Car} or a {@link Page} of {@link Car} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarQueryService extends QueryService<Car> {

    private final Logger log = LoggerFactory.getLogger(CarQueryService.class);

    private final CarRepository carRepository;

    public CarQueryService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Return a {@link List} of {@link Car} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Car> findByCriteria(CarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Car} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findByCriteria(CarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert {@link CarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Car_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Car_.name));
            }
            if (criteria.getCarIssue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCarIssue(), Car_.carIssue));
            }
            if (criteria.getCarnNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCarnNumber(), Car_.carnNumber));
            }
            if (criteria.getCarMotor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCarMotor(), Car_.carMotor));
            }
            if (criteria.getCarShellNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCarShellNumber(), Car_.carShellNumber));
            }
            if (criteria.getClassification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassification(), Car_.classification));
            }
            if (criteria.getMadein() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMadein(), Car_.madein));
            }
            if (criteria.getPanaelnumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPanaelnumber(), Car_.panaelnumber));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Car_.notes));
            }
            if (criteria.getDispatchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDispatchId(), root -> root.join(Car_.dispatches, JoinType.LEFT).get(Dispatch_.id))
                    );
            }
            if (criteria.getFailureId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFailureId(), root -> root.join(Car_.failures, JoinType.LEFT).get(Failure_.id))
                    );
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmployeeId(), root -> root.join(Car_.employee, JoinType.LEFT).get(Employee_.id))
                    );
            }
        }
        return specification;
    }
}

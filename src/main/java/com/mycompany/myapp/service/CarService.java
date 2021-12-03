package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.repository.CarRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarService {

    private final Logger log = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    public Car save(Car car) {
        log.debug("Request to save Car : {}", car);
        return carRepository.save(car);
    }

    /**
     * Partially update a car.
     *
     * @param car the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Car> partialUpdate(Car car) {
        log.debug("Request to partially update Car : {}", car);

        return carRepository
            .findById(car.getId())
            .map(existingCar -> {
                if (car.getName() != null) {
                    existingCar.setName(car.getName());
                }
                if (car.getCarIssue() != null) {
                    existingCar.setCarIssue(car.getCarIssue());
                }
                if (car.getCarnNumber() != null) {
                    existingCar.setCarnNumber(car.getCarnNumber());
                }
                if (car.getCarMotor() != null) {
                    existingCar.setCarMotor(car.getCarMotor());
                }
                if (car.getCarShellNumber() != null) {
                    existingCar.setCarShellNumber(car.getCarShellNumber());
                }
                if (car.getClassification() != null) {
                    existingCar.setClassification(car.getClassification());
                }
                if (car.getMadein() != null) {
                    existingCar.setMadein(car.getMadein());
                }
                if (car.getPanaelnumber() != null) {
                    existingCar.setPanaelnumber(car.getPanaelnumber());
                }
                if (car.getNotes() != null) {
                    existingCar.setNotes(car.getNotes());
                }

                return existingCar;
            })
            .map(carRepository::save);
    }

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAll(pageable);
    }

    /**
     *  Get all the cars where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Car> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all cars where Employee is null");
        return StreamSupport
            .stream(carRepository.findAll().spliterator(), false)
            .filter(car -> car.getEmployee() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Car> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}

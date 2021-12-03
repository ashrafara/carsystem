package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Car} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter carIssue;

    private StringFilter carnNumber;

    private StringFilter carMotor;

    private StringFilter carShellNumber;

    private StringFilter classification;

    private StringFilter madein;

    private StringFilter panaelnumber;

    private StringFilter notes;

    private LongFilter dispatchId;

    private LongFilter failureId;

    private LongFilter employeeId;

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.carIssue = other.carIssue == null ? null : other.carIssue.copy();
        this.carnNumber = other.carnNumber == null ? null : other.carnNumber.copy();
        this.carMotor = other.carMotor == null ? null : other.carMotor.copy();
        this.carShellNumber = other.carShellNumber == null ? null : other.carShellNumber.copy();
        this.classification = other.classification == null ? null : other.classification.copy();
        this.madein = other.madein == null ? null : other.madein.copy();
        this.panaelnumber = other.panaelnumber == null ? null : other.panaelnumber.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.dispatchId = other.dispatchId == null ? null : other.dispatchId.copy();
        this.failureId = other.failureId == null ? null : other.failureId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getCarIssue() {
        return carIssue;
    }

    public IntegerFilter carIssue() {
        if (carIssue == null) {
            carIssue = new IntegerFilter();
        }
        return carIssue;
    }

    public void setCarIssue(IntegerFilter carIssue) {
        this.carIssue = carIssue;
    }

    public StringFilter getCarnNumber() {
        return carnNumber;
    }

    public StringFilter carnNumber() {
        if (carnNumber == null) {
            carnNumber = new StringFilter();
        }
        return carnNumber;
    }

    public void setCarnNumber(StringFilter carnNumber) {
        this.carnNumber = carnNumber;
    }

    public StringFilter getCarMotor() {
        return carMotor;
    }

    public StringFilter carMotor() {
        if (carMotor == null) {
            carMotor = new StringFilter();
        }
        return carMotor;
    }

    public void setCarMotor(StringFilter carMotor) {
        this.carMotor = carMotor;
    }

    public StringFilter getCarShellNumber() {
        return carShellNumber;
    }

    public StringFilter carShellNumber() {
        if (carShellNumber == null) {
            carShellNumber = new StringFilter();
        }
        return carShellNumber;
    }

    public void setCarShellNumber(StringFilter carShellNumber) {
        this.carShellNumber = carShellNumber;
    }

    public StringFilter getClassification() {
        return classification;
    }

    public StringFilter classification() {
        if (classification == null) {
            classification = new StringFilter();
        }
        return classification;
    }

    public void setClassification(StringFilter classification) {
        this.classification = classification;
    }

    public StringFilter getMadein() {
        return madein;
    }

    public StringFilter madein() {
        if (madein == null) {
            madein = new StringFilter();
        }
        return madein;
    }

    public void setMadein(StringFilter madein) {
        this.madein = madein;
    }

    public StringFilter getPanaelnumber() {
        return panaelnumber;
    }

    public StringFilter panaelnumber() {
        if (panaelnumber == null) {
            panaelnumber = new StringFilter();
        }
        return panaelnumber;
    }

    public void setPanaelnumber(StringFilter panaelnumber) {
        this.panaelnumber = panaelnumber;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getDispatchId() {
        return dispatchId;
    }

    public LongFilter dispatchId() {
        if (dispatchId == null) {
            dispatchId = new LongFilter();
        }
        return dispatchId;
    }

    public void setDispatchId(LongFilter dispatchId) {
        this.dispatchId = dispatchId;
    }

    public LongFilter getFailureId() {
        return failureId;
    }

    public LongFilter failureId() {
        if (failureId == null) {
            failureId = new LongFilter();
        }
        return failureId;
    }

    public void setFailureId(LongFilter failureId) {
        this.failureId = failureId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CarCriteria that = (CarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(carIssue, that.carIssue) &&
            Objects.equals(carnNumber, that.carnNumber) &&
            Objects.equals(carMotor, that.carMotor) &&
            Objects.equals(carShellNumber, that.carShellNumber) &&
            Objects.equals(classification, that.classification) &&
            Objects.equals(madein, that.madein) &&
            Objects.equals(panaelnumber, that.panaelnumber) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(dispatchId, that.dispatchId) &&
            Objects.equals(failureId, that.failureId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            carIssue,
            carnNumber,
            carMotor,
            carShellNumber,
            classification,
            madein,
            panaelnumber,
            notes,
            dispatchId,
            failureId,
            employeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (carIssue != null ? "carIssue=" + carIssue + ", " : "") +
            (carnNumber != null ? "carnNumber=" + carnNumber + ", " : "") +
            (carMotor != null ? "carMotor=" + carMotor + ", " : "") +
            (carShellNumber != null ? "carShellNumber=" + carShellNumber + ", " : "") +
            (classification != null ? "classification=" + classification + ", " : "") +
            (madein != null ? "madein=" + madein + ", " : "") +
            (panaelnumber != null ? "panaelnumber=" + panaelnumber + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (dispatchId != null ? "dispatchId=" + dispatchId + ", " : "") +
            (failureId != null ? "failureId=" + failureId + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Dispatch} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DispatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dispatches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DispatchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dispatchDate;

    private StringFilter item;

    private StringFilter quantity;

    private LongFilter carId;

    private LongFilter supplierId;

    private Boolean distinct;

    public DispatchCriteria() {}

    public DispatchCriteria(DispatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dispatchDate = other.dispatchDate == null ? null : other.dispatchDate.copy();
        this.item = other.item == null ? null : other.item.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.carId = other.carId == null ? null : other.carId.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DispatchCriteria copy() {
        return new DispatchCriteria(this);
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

    public LocalDateFilter getDispatchDate() {
        return dispatchDate;
    }

    public LocalDateFilter dispatchDate() {
        if (dispatchDate == null) {
            dispatchDate = new LocalDateFilter();
        }
        return dispatchDate;
    }

    public void setDispatchDate(LocalDateFilter dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public StringFilter getItem() {
        return item;
    }

    public StringFilter item() {
        if (item == null) {
            item = new StringFilter();
        }
        return item;
    }

    public void setItem(StringFilter item) {
        this.item = item;
    }

    public StringFilter getQuantity() {
        return quantity;
    }

    public StringFilter quantity() {
        if (quantity == null) {
            quantity = new StringFilter();
        }
        return quantity;
    }

    public void setQuantity(StringFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getCarId() {
        return carId;
    }

    public LongFilter carId() {
        if (carId == null) {
            carId = new LongFilter();
        }
        return carId;
    }

    public void setCarId(LongFilter carId) {
        this.carId = carId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            supplierId = new LongFilter();
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
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
        final DispatchCriteria that = (DispatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dispatchDate, that.dispatchDate) &&
            Objects.equals(item, that.item) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dispatchDate, item, quantity, carId, supplierId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DispatchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dispatchDate != null ? "dispatchDate=" + dispatchDate + ", " : "") +
            (item != null ? "item=" + item + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (carId != null ? "carId=" + carId + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

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
 * Criteria class for the {@link com.mycompany.myapp.domain.Supplier} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SupplierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suppliers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SupplierCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter supplierdate;

    private IntegerFilter quantity;

    private StringFilter item;

    private StringFilter note;

    private LongFilter dispatchId;

    private Boolean distinct;

    public SupplierCriteria() {}

    public SupplierCriteria(SupplierCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.supplierdate = other.supplierdate == null ? null : other.supplierdate.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.item = other.item == null ? null : other.item.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.dispatchId = other.dispatchId == null ? null : other.dispatchId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SupplierCriteria copy() {
        return new SupplierCriteria(this);
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

    public LocalDateFilter getSupplierdate() {
        return supplierdate;
    }

    public LocalDateFilter supplierdate() {
        if (supplierdate == null) {
            supplierdate = new LocalDateFilter();
        }
        return supplierdate;
    }

    public void setSupplierdate(LocalDateFilter supplierdate) {
        this.supplierdate = supplierdate;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
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

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
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
        final SupplierCriteria that = (SupplierCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(supplierdate, that.supplierdate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(item, that.item) &&
            Objects.equals(note, that.note) &&
            Objects.equals(dispatchId, that.dispatchId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, supplierdate, quantity, item, note, dispatchId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (supplierdate != null ? "supplierdate=" + supplierdate + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (item != null ? "item=" + item + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (dispatchId != null ? "dispatchId=" + dispatchId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

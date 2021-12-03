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
 * Criteria class for the {@link com.mycompany.myapp.domain.Failure} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.FailureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /failures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FailureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter failureDate;

    private DoubleFilter carGuagefrom;

    private DoubleFilter carGuageTo;

    private StringFilter changepart;

    private StringFilter garageName;

    private DoubleFilter price;

    private StringFilter inovice1;

    private StringFilter inovice2;

    private StringFilter inovice3;

    private StringFilter inovice4;

    private StringFilter note;

    private LongFilter carId;

    private Boolean distinct;

    public FailureCriteria() {}

    public FailureCriteria(FailureCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.failureDate = other.failureDate == null ? null : other.failureDate.copy();
        this.carGuagefrom = other.carGuagefrom == null ? null : other.carGuagefrom.copy();
        this.carGuageTo = other.carGuageTo == null ? null : other.carGuageTo.copy();
        this.changepart = other.changepart == null ? null : other.changepart.copy();
        this.garageName = other.garageName == null ? null : other.garageName.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.inovice1 = other.inovice1 == null ? null : other.inovice1.copy();
        this.inovice2 = other.inovice2 == null ? null : other.inovice2.copy();
        this.inovice3 = other.inovice3 == null ? null : other.inovice3.copy();
        this.inovice4 = other.inovice4 == null ? null : other.inovice4.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.carId = other.carId == null ? null : other.carId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FailureCriteria copy() {
        return new FailureCriteria(this);
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

    public LocalDateFilter getFailureDate() {
        return failureDate;
    }

    public LocalDateFilter failureDate() {
        if (failureDate == null) {
            failureDate = new LocalDateFilter();
        }
        return failureDate;
    }

    public void setFailureDate(LocalDateFilter failureDate) {
        this.failureDate = failureDate;
    }

    public DoubleFilter getCarGuagefrom() {
        return carGuagefrom;
    }

    public DoubleFilter carGuagefrom() {
        if (carGuagefrom == null) {
            carGuagefrom = new DoubleFilter();
        }
        return carGuagefrom;
    }

    public void setCarGuagefrom(DoubleFilter carGuagefrom) {
        this.carGuagefrom = carGuagefrom;
    }

    public DoubleFilter getCarGuageTo() {
        return carGuageTo;
    }

    public DoubleFilter carGuageTo() {
        if (carGuageTo == null) {
            carGuageTo = new DoubleFilter();
        }
        return carGuageTo;
    }

    public void setCarGuageTo(DoubleFilter carGuageTo) {
        this.carGuageTo = carGuageTo;
    }

    public StringFilter getChangepart() {
        return changepart;
    }

    public StringFilter changepart() {
        if (changepart == null) {
            changepart = new StringFilter();
        }
        return changepart;
    }

    public void setChangepart(StringFilter changepart) {
        this.changepart = changepart;
    }

    public StringFilter getGarageName() {
        return garageName;
    }

    public StringFilter garageName() {
        if (garageName == null) {
            garageName = new StringFilter();
        }
        return garageName;
    }

    public void setGarageName(StringFilter garageName) {
        this.garageName = garageName;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getInovice1() {
        return inovice1;
    }

    public StringFilter inovice1() {
        if (inovice1 == null) {
            inovice1 = new StringFilter();
        }
        return inovice1;
    }

    public void setInovice1(StringFilter inovice1) {
        this.inovice1 = inovice1;
    }

    public StringFilter getInovice2() {
        return inovice2;
    }

    public StringFilter inovice2() {
        if (inovice2 == null) {
            inovice2 = new StringFilter();
        }
        return inovice2;
    }

    public void setInovice2(StringFilter inovice2) {
        this.inovice2 = inovice2;
    }

    public StringFilter getInovice3() {
        return inovice3;
    }

    public StringFilter inovice3() {
        if (inovice3 == null) {
            inovice3 = new StringFilter();
        }
        return inovice3;
    }

    public void setInovice3(StringFilter inovice3) {
        this.inovice3 = inovice3;
    }

    public StringFilter getInovice4() {
        return inovice4;
    }

    public StringFilter inovice4() {
        if (inovice4 == null) {
            inovice4 = new StringFilter();
        }
        return inovice4;
    }

    public void setInovice4(StringFilter inovice4) {
        this.inovice4 = inovice4;
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
        final FailureCriteria that = (FailureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(failureDate, that.failureDate) &&
            Objects.equals(carGuagefrom, that.carGuagefrom) &&
            Objects.equals(carGuageTo, that.carGuageTo) &&
            Objects.equals(changepart, that.changepart) &&
            Objects.equals(garageName, that.garageName) &&
            Objects.equals(price, that.price) &&
            Objects.equals(inovice1, that.inovice1) &&
            Objects.equals(inovice2, that.inovice2) &&
            Objects.equals(inovice3, that.inovice3) &&
            Objects.equals(inovice4, that.inovice4) &&
            Objects.equals(note, that.note) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            failureDate,
            carGuagefrom,
            carGuageTo,
            changepart,
            garageName,
            price,
            inovice1,
            inovice2,
            inovice3,
            inovice4,
            note,
            carId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailureCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (failureDate != null ? "failureDate=" + failureDate + ", " : "") +
            (carGuagefrom != null ? "carGuagefrom=" + carGuagefrom + ", " : "") +
            (carGuageTo != null ? "carGuageTo=" + carGuageTo + ", " : "") +
            (changepart != null ? "changepart=" + changepart + ", " : "") +
            (garageName != null ? "garageName=" + garageName + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (inovice1 != null ? "inovice1=" + inovice1 + ", " : "") +
            (inovice2 != null ? "inovice2=" + inovice2 + ", " : "") +
            (inovice3 != null ? "inovice3=" + inovice3 + ", " : "") +
            (inovice4 != null ? "inovice4=" + inovice4 + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (carId != null ? "carId=" + carId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

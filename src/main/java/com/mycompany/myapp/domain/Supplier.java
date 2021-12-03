package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Supplier.
 */
@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "supplierdate")
    private LocalDate supplierdate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "item")
    private String item;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = { "car", "supplier" }, allowSetters = true)
    private Set<Dispatch> dispatches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Supplier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Supplier name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSupplierdate() {
        return this.supplierdate;
    }

    public Supplier supplierdate(LocalDate supplierdate) {
        this.setSupplierdate(supplierdate);
        return this;
    }

    public void setSupplierdate(LocalDate supplierdate) {
        this.supplierdate = supplierdate;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Supplier quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getItem() {
        return this.item;
    }

    public Supplier item(String item) {
        this.setItem(item);
        return this;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNote() {
        return this.note;
    }

    public Supplier note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Dispatch> getDispatches() {
        return this.dispatches;
    }

    public void setDispatches(Set<Dispatch> dispatches) {
        if (this.dispatches != null) {
            this.dispatches.forEach(i -> i.setSupplier(null));
        }
        if (dispatches != null) {
            dispatches.forEach(i -> i.setSupplier(this));
        }
        this.dispatches = dispatches;
    }

    public Supplier dispatches(Set<Dispatch> dispatches) {
        this.setDispatches(dispatches);
        return this;
    }

    public Supplier addDispatch(Dispatch dispatch) {
        this.dispatches.add(dispatch);
        dispatch.setSupplier(this);
        return this;
    }

    public Supplier removeDispatch(Dispatch dispatch) {
        this.dispatches.remove(dispatch);
        dispatch.setSupplier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return id != null && id.equals(((Supplier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplier{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", supplierdate='" + getSupplierdate() + "'" +
            ", quantity=" + getQuantity() +
            ", item='" + getItem() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}

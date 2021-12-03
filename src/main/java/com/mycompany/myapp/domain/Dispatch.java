package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Dispatch.
 */
@Entity
@Table(name = "dispatch")
public class Dispatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dispatch_date")
    private LocalDate dispatchDate;

    @Column(name = "item")
    private String item;

    @Column(name = "quantity")
    private String quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dispatches", "failures", "employee" }, allowSetters = true)
    private Car car;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dispatches" }, allowSetters = true)
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dispatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDispatchDate() {
        return this.dispatchDate;
    }

    public Dispatch dispatchDate(LocalDate dispatchDate) {
        this.setDispatchDate(dispatchDate);
        return this;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getItem() {
        return this.item;
    }

    public Dispatch item(String item) {
        this.setItem(item);
        return this;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public Dispatch quantity(String quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Dispatch car(Car car) {
        this.setCar(car);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Dispatch supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dispatch)) {
            return false;
        }
        return id != null && id.equals(((Dispatch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dispatch{" +
            "id=" + getId() +
            ", dispatchDate='" + getDispatchDate() + "'" +
            ", item='" + getItem() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
}

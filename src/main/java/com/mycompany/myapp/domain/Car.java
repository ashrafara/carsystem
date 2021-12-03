package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "car_issue")
    private Integer carIssue;

    @Column(name = "carn_number")
    private String carnNumber;

    @Column(name = "car_motor")
    private String carMotor;

    @Column(name = "car_shell_number")
    private String carShellNumber;

    @Column(name = "classification")
    private String classification;

    @Column(name = "madein")
    private String madein;

    @Column(name = "panaelnumber")
    private String panaelnumber;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "car")
    @JsonIgnoreProperties(value = { "car", "supplier" }, allowSetters = true)
    private Set<Dispatch> dispatches = new HashSet<>();

    @OneToMany(mappedBy = "car")
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Set<Failure> failures = new HashSet<>();

    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    @OneToOne(mappedBy = "car")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Car name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCarIssue() {
        return this.carIssue;
    }

    public Car carIssue(Integer carIssue) {
        this.setCarIssue(carIssue);
        return this;
    }

    public void setCarIssue(Integer carIssue) {
        this.carIssue = carIssue;
    }

    public String getCarnNumber() {
        return this.carnNumber;
    }

    public Car carnNumber(String carnNumber) {
        this.setCarnNumber(carnNumber);
        return this;
    }

    public void setCarnNumber(String carnNumber) {
        this.carnNumber = carnNumber;
    }

    public String getCarMotor() {
        return this.carMotor;
    }

    public Car carMotor(String carMotor) {
        this.setCarMotor(carMotor);
        return this;
    }

    public void setCarMotor(String carMotor) {
        this.carMotor = carMotor;
    }

    public String getCarShellNumber() {
        return this.carShellNumber;
    }

    public Car carShellNumber(String carShellNumber) {
        this.setCarShellNumber(carShellNumber);
        return this;
    }

    public void setCarShellNumber(String carShellNumber) {
        this.carShellNumber = carShellNumber;
    }

    public String getClassification() {
        return this.classification;
    }

    public Car classification(String classification) {
        this.setClassification(classification);
        return this;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMadein() {
        return this.madein;
    }

    public Car madein(String madein) {
        this.setMadein(madein);
        return this;
    }

    public void setMadein(String madein) {
        this.madein = madein;
    }

    public String getPanaelnumber() {
        return this.panaelnumber;
    }

    public Car panaelnumber(String panaelnumber) {
        this.setPanaelnumber(panaelnumber);
        return this;
    }

    public void setPanaelnumber(String panaelnumber) {
        this.panaelnumber = panaelnumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public Car notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Dispatch> getDispatches() {
        return this.dispatches;
    }

    public void setDispatches(Set<Dispatch> dispatches) {
        if (this.dispatches != null) {
            this.dispatches.forEach(i -> i.setCar(null));
        }
        if (dispatches != null) {
            dispatches.forEach(i -> i.setCar(this));
        }
        this.dispatches = dispatches;
    }

    public Car dispatches(Set<Dispatch> dispatches) {
        this.setDispatches(dispatches);
        return this;
    }

    public Car addDispatch(Dispatch dispatch) {
        this.dispatches.add(dispatch);
        dispatch.setCar(this);
        return this;
    }

    public Car removeDispatch(Dispatch dispatch) {
        this.dispatches.remove(dispatch);
        dispatch.setCar(null);
        return this;
    }

    public Set<Failure> getFailures() {
        return this.failures;
    }

    public void setFailures(Set<Failure> failures) {
        if (this.failures != null) {
            this.failures.forEach(i -> i.setCar(null));
        }
        if (failures != null) {
            failures.forEach(i -> i.setCar(this));
        }
        this.failures = failures;
    }

    public Car failures(Set<Failure> failures) {
        this.setFailures(failures);
        return this;
    }

    public Car addFailure(Failure failure) {
        this.failures.add(failure);
        failure.setCar(this);
        return this;
    }

    public Car removeFailure(Failure failure) {
        this.failures.remove(failure);
        failure.setCar(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.setCar(null);
        }
        if (employee != null) {
            employee.setCar(this);
        }
        this.employee = employee;
    }

    public Car employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", carIssue=" + getCarIssue() +
            ", carnNumber='" + getCarnNumber() + "'" +
            ", carMotor='" + getCarMotor() + "'" +
            ", carShellNumber='" + getCarShellNumber() + "'" +
            ", classification='" + getClassification() + "'" +
            ", madein='" + getMadein() + "'" +
            ", panaelnumber='" + getPanaelnumber() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}

package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Failure.
 */
@Entity
@Table(name = "failure")
public class Failure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "failure_date")
    private LocalDate failureDate;

    @Column(name = "car_guagefrom")
    private Double carGuagefrom;

    @Column(name = "car_guage_to")
    private Double carGuageTo;

    @Column(name = "changepart")
    private String changepart;

    @Column(name = "garage_name")
    private String garageName;

    @Column(name = "price")
    private Double price;

    @Column(name = "inovice_1")
    private String inovice1;

    @Column(name = "inovice_2")
    private String inovice2;

    @Column(name = "inovice_3")
    private String inovice3;

    @Column(name = "inovice_4")
    private String inovice4;

    @Column(name = "note")
    private String note;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dispatches", "failures", "employee" }, allowSetters = true)
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Failure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Failure name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFailureDate() {
        return this.failureDate;
    }

    public Failure failureDate(LocalDate failureDate) {
        this.setFailureDate(failureDate);
        return this;
    }

    public void setFailureDate(LocalDate failureDate) {
        this.failureDate = failureDate;
    }

    public Double getCarGuagefrom() {
        return this.carGuagefrom;
    }

    public Failure carGuagefrom(Double carGuagefrom) {
        this.setCarGuagefrom(carGuagefrom);
        return this;
    }

    public void setCarGuagefrom(Double carGuagefrom) {
        this.carGuagefrom = carGuagefrom;
    }

    public Double getCarGuageTo() {
        return this.carGuageTo;
    }

    public Failure carGuageTo(Double carGuageTo) {
        this.setCarGuageTo(carGuageTo);
        return this;
    }

    public void setCarGuageTo(Double carGuageTo) {
        this.carGuageTo = carGuageTo;
    }

    public String getChangepart() {
        return this.changepart;
    }

    public Failure changepart(String changepart) {
        this.setChangepart(changepart);
        return this;
    }

    public void setChangepart(String changepart) {
        this.changepart = changepart;
    }

    public String getGarageName() {
        return this.garageName;
    }

    public Failure garageName(String garageName) {
        this.setGarageName(garageName);
        return this;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public Double getPrice() {
        return this.price;
    }

    public Failure price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getInovice1() {
        return this.inovice1;
    }

    public Failure inovice1(String inovice1) {
        this.setInovice1(inovice1);
        return this;
    }

    public void setInovice1(String inovice1) {
        this.inovice1 = inovice1;
    }

    public String getInovice2() {
        return this.inovice2;
    }

    public Failure inovice2(String inovice2) {
        this.setInovice2(inovice2);
        return this;
    }

    public void setInovice2(String inovice2) {
        this.inovice2 = inovice2;
    }

    public String getInovice3() {
        return this.inovice3;
    }

    public Failure inovice3(String inovice3) {
        this.setInovice3(inovice3);
        return this;
    }

    public void setInovice3(String inovice3) {
        this.inovice3 = inovice3;
    }

    public String getInovice4() {
        return this.inovice4;
    }

    public Failure inovice4(String inovice4) {
        this.setInovice4(inovice4);
        return this;
    }

    public void setInovice4(String inovice4) {
        this.inovice4 = inovice4;
    }

    public String getNote() {
        return this.note;
    }

    public Failure note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Failure image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Failure imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Failure car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Failure)) {
            return false;
        }
        return id != null && id.equals(((Failure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Failure{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", failureDate='" + getFailureDate() + "'" +
            ", carGuagefrom=" + getCarGuagefrom() +
            ", carGuageTo=" + getCarGuageTo() +
            ", changepart='" + getChangepart() + "'" +
            ", garageName='" + getGarageName() + "'" +
            ", price=" + getPrice() +
            ", inovice1='" + getInovice1() + "'" +
            ", inovice2='" + getInovice2() + "'" +
            ", inovice3='" + getInovice3() + "'" +
            ", inovice4='" + getInovice4() + "'" +
            ", note='" + getNote() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}

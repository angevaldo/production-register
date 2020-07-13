package br.com.aegro.production.domain.entities;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Document
public class Production implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private double value;
    @org.springframework.data.mongodb.core.mapping.Field("prod")
    private double productivity;
    @DBRef(lazy = true)
    private Farm farm;
    @DBRef(lazy = true)
    private Field field;

    public Production() {
    }

    public Production(String id, double value, Farm farm, Field field) {
        this.id = id;
        this.value = value;

        this.farm = farm;

        this.setField(field);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private void updateProductivity() {
        this.productivity = this.value / getField().getArea();
    }

    public double getProductivity() {
        return productivity;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
        this.updateProductivity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Production)) return false;
        Production that = (Production) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

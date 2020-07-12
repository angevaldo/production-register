package br.com.aegro.production.domain.entities;

import org.bson.types.ObjectId;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private double area;

    public Field() {
        this.setId(null);
    }

    public Field(String id, String name, double area) {
        this.setId(id);
        this.name = name;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? new ObjectId().toString() : id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return Objects.equals(id, field.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

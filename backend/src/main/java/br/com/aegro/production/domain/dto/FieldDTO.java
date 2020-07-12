package br.com.aegro.production.domain.dto;

import java.io.Serializable;
import java.util.Objects;

public class FieldDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double area;

    public FieldDTO() {
    }

    public FieldDTO(String id, String name, double area) {
        this.id = id;
        this.name = name;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(o instanceof FieldDTO)) return false;
        FieldDTO fieldDTO = (FieldDTO) o;
        return id.equals(fieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

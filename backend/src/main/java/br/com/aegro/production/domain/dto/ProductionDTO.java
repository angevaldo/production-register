package br.com.aegro.production.domain.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private double value;
    private double productivity;

    public ProductionDTO() {
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

    public double getProductivity() {
        return productivity;
    }

    public void setProductivity(double productivity) {
        this.productivity = productivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionDTO)) return false;
        ProductionDTO that = (ProductionDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

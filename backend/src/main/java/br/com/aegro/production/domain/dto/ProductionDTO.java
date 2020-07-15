package br.com.aegro.production.domain.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "Production registry")
public class ProductionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Min(value = 1, message = "A área deve ser maior que zero.")
    private double value;

    private double productivity;

    public ProductionDTO() {
    }

    public ProductionDTO(String id, double value, double productivity) {
        this.id = id;
        this.value = value;
        this.productivity = productivity;
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

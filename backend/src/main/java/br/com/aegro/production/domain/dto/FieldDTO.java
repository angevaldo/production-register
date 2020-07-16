package br.com.aegro.production.domain.dto;

import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "Field master data")
public class FieldDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotEmpty(message = "Name cannot be empty.")
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters.")
    private String name;

    @Min(value = 1, message = "The area must be greater than zero.")
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

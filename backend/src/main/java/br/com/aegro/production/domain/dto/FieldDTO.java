package br.com.aegro.production.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "Field master data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    @NotEmpty(message = "Name cannot be empty.")
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    private String name;
    @Min(value = 1, message = "Area must be greater than zero.")
    private double area;

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

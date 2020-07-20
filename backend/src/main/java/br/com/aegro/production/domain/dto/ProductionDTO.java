package br.com.aegro.production.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "Production registry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    @Min(value = 1, message = "Value must be greater than zero.")
    private double value;
    private String fieldId;

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

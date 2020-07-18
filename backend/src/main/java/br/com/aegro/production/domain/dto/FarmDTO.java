package br.com.aegro.production.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@ApiModel(description = "Farm master data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    @NotEmpty(message = "Name cannot be empty.")
    @Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    private String name;
    private Set<FieldDTO> fields;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FarmDTO)) return false;
        FarmDTO farmDTO = (FarmDTO) o;
        return id.equals(farmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

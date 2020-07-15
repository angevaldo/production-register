package br.com.aegro.production.domain.dto;

import br.com.aegro.production.domain.entities.Farm;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApiModel(description = "Farm master data")
public class FarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotEmpty(message = "Nome não pode ser vazio.")
    @Length(min = 3, max = 50, message = "Nome deve conter entre 3 e 50 caracteres.")
    private String name;

    private Set<FieldDTO> fields = new HashSet<>();

    public FarmDTO() {
    }

    public FarmDTO(Farm obj) {
        this.id = obj.getId();
        this.name = obj.getName();
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

    public Set<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(Set<FieldDTO> fields) {
        this.fields = fields;
    }

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

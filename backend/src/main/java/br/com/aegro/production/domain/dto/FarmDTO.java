package br.com.aegro.production.domain.dto;

import br.com.aegro.production.domain.entities.Farm;

import java.io.Serializable;
import java.util.Objects;

public class FarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

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

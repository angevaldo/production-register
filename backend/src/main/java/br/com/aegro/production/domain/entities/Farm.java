package br.com.aegro.production.domain.entities;

import io.swagger.annotations.ApiModel;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document
@ApiModel(description = "Farm master data")
public class Farm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private Set<Field> fields = new HashSet<>();

    public Farm() {
    }

    public Farm(String id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Farm)) return false;
        Farm farm = (Farm) o;
        return id.equals(farm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

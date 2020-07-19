package br.com.aegro.production.domain.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Document
public class Farm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;

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

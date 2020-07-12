package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Field;

import java.util.List;

public interface FieldService {

    List<Field> findAll();

    Field findById(String id);

    Field insert(Field field);

    Field save (Field field);

    double getProductivity(String id);

}

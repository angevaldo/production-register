package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Field;

import java.util.List;

public interface FieldService {

    List<Field> findByFarmId(String fieldId);

    Field findById(String fieldId);

    Field create(Field field);

    Field update(Field field);

    void deleteAll();

    void deleteById(String farmId);

    void deleteByFarmId(String farmId);

}

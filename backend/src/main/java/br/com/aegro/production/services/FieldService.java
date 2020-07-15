package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Field;

import java.util.List;

public interface FieldService {

    List<Field> findByFarmId(String fieldId);

    Field findByFieldsId(String fieldId);

    Field create(Field field, String farmId);

    Field update(Field field);

    void delete(String farmId);

}

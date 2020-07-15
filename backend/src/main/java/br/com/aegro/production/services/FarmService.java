package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;

import java.util.List;

public interface FarmService {

    List<Farm> findAllFarms();

    Farm findFarmById(String id);

    Field findFieldByFieldsId(String fieldId);

    Farm createFarm(Farm farm);

    Farm updateFarm(Farm farm, String farmId);

    void deleteAllFarms();

    void deleteFarmById(String id);

    Field createField(Field field, String farmId);

    Field updateField(Field field, String farmId, String fieldId);

    void deleteFieldById(String farmId, String fieldId);

    double getFarmProductivity(String farmId);

    double getFieldProductivity(String fieldId);

}

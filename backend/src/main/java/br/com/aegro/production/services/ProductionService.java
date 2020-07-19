package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Production;

import java.util.List;

public interface ProductionService {

    Production findById(String productionId);

    List<Production> findByFieldId(String fieldId);

    List<Production> findByFarmId(String farmId);

    double getProductivityByFieldId(String fieldId);

    double getProductivityByFarmId(String farmId);

    Production create(Production production);

    Production update(Production production);

    void deleteAll();

    void deleteById(String productionId);

    void deleteByFieldId(String fieldId);

    void deleteByFarmId(String farmId);

}

package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Production;

import java.util.List;

public interface ProductionService {

    Production findById(String productionId);

    List<Production> findByFarmId(String farmId);

    List<Production> findByFieldId(String fieldId);

    Production create(Production production);

    Production update(Production production);

    void deleteAll();

    void deleteById(String productionId);

    void deleteByFarmId(String farmId);

    void deleteByFieldId(String fieldId);

}

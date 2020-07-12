package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Production;

import java.util.List;

public interface ProductionService {

    List<Production> findByFieldId(String fieldId);

    List<Production> findByFarmId(String farmId);

    Production findById(String id);

    Production insert(Production production);

    Production save (Production production);

    void deleteAll();
}

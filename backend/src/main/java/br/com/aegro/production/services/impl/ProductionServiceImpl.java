package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.ProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements br.com.aegro.production.services.ProductionService {

    @Autowired
    private ProductionRepository repository;

    @Override
    public List<Production> findByFieldId(String fieldId) {
        return repository.findByFieldId(fieldId);
    }

    @Override
    public List<Production> findByFarmId(String farmId) {
        return repository.findByFarmId(farmId);
    }

    @Override
    public Production findById(String id) {
        Optional<Production> obj = repository.findById(id);
        return obj.get();
    }

    @Override
    public Production insert(Production production) {
        return repository.insert(production);
    }

    @Override
    public Production save(Production production) {
        return this.repository.save(production);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}

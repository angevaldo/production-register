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
    ProductionRepository productionRepository;

    @Override
    public Production findById(String productionId) {
        Optional<Production> obj = productionRepository.findById(productionId);
        return obj.get();
    }

    @Override
    public List<Production> findByFarmId(String farmId) {
        return productionRepository.findByFarmId(farmId);
    }

    @Override
    public List<Production> findByFieldId(String fieldId) {
        return productionRepository.findByFieldId(fieldId);
    }

    @Override
    public double getProductivityByFarmId(String farmId) {
        List<Production> productions = findByFarmId(farmId);
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

    @Override
    public double getProductivityByFieldId(String fieldId) {
        List<Production> productions = findByFieldId(fieldId);
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

    @Override
    public Production create(Production production) {
        return productionRepository.insert(production);
    }

    @Override
    public Production update(Production production) {
        return productionRepository.save(production);
    }

    @Override
    public void deleteAll() {
        productionRepository.deleteAll();
    }

    @Override
    public void delete(String id) {
        productionRepository.deleteById(id);
    }

    @Override
    public void deleteByFarmId(String farmId) {
        productionRepository.deleteByFarmId(farmId);
    }

    @Override
    public void deleteByFieldId(String fieldId) {
        productionRepository.deleteByFieldId(fieldId);
    }

}

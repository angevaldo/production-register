package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.dto.ProductionDTO;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.entities.exceptions.ProductivityException;
import br.com.aegro.production.domain.repositories.ProductionRepository;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements br.com.aegro.production.services.ProductionService {

    @Autowired
    ProductionRepository productionRepository;

    private void updateDataFromTo(Production productionFrom, Production productionTo) throws ProductivityException {
        productionFrom.setValue(productionTo.getValue());
    }

    public ProductionServiceImpl(ProductionRepository productionRepository) {
        this.productionRepository = productionRepository;
    }

    @Override
    public Production findById(String productionId) {
        Optional<Production> obj = productionRepository.findById(productionId);
        return obj.orElseThrow(() -> new ObjectNotFoundException(productionId));
    }

    @Override
    public List<Production> findByFieldId(String fieldId) {
        return productionRepository.findByFieldId(fieldId);
    }

    @Override
    public List<Production> findByFarmId(String farmId) {
        return productionRepository.findByFarmId(farmId);
    }

    @Override
    public double getProductivityByFieldId(String fieldId) {
        List<Production> productions = findByFieldId(fieldId);
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

    @Override
    public double getProductivityByFarmId(String farmId) {
        List<Production> productions = findByFarmId(farmId);
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

    @Override
    public Production create(Production production) {
        return productionRepository.insert(production);
    }

    @Override
    public Production update(Production production) throws ProductivityException {
        Production transientProduction = findById(production.getId());
        updateDataFromTo(production, transientProduction);
        return productionRepository.save(transientProduction);
    }

    @Override
    public void deleteAll() {
        productionRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        productionRepository.deleteById(id);
    }

    @Override
    public void deleteByFieldId(String fieldId) {
        productionRepository.deleteByFieldId(fieldId);
    }

    @Override
    public void deleteByFarmId(String farmId) {
        productionRepository.deleteByFarmId(farmId);
    }

}

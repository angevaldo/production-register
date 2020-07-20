package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.ProductionRepository;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionServiceImpl implements br.com.aegro.production.services.ProductionService {

    @Autowired
    ProductionRepository productionRepository;

    @Autowired
    FieldService fieldService;

    private void updateDataFromTo(Production productionFrom, Production productionTo) {
        productionTo.setValue(productionFrom.getValue());
    }

    private double calculateProductivity(List<Production> productions) {
        return productions.stream().mapToDouble(x -> x.getValue() / x.getField().getArea()).sum();
    }

    public ProductionServiceImpl(@Lazy ProductionRepository productionRepository, @Lazy FieldService fieldService) {
        this.productionRepository = productionRepository;
        this.fieldService = fieldService;
    }

    @Override
    public Production findById(String productionId) {
        Optional<Production> obj = productionRepository.findById(productionId);
        return obj.orElseThrow(() -> new ObjectNotFoundException(productionId));
    }

    @Override
    public List<Production> findByFieldId(String fieldId) {
        List<Production> list = productionRepository.findByFieldId(fieldId);
        if (list.size() == 0) {
            throw new ObjectNotFoundException(fieldId);
        }
        return list;
    }

    @Override
    public List<Production> findByFarmId(String farmId) {
        List<Production> list = productionRepository.findByFarmId(farmId);
        if (list.size() == 0) {
            throw new ObjectNotFoundException(farmId);
        }
        return list;
    }

    @Override
    public double getProductivityByFarmId(String farmId) {
        return calculateProductivity(findByFarmId(farmId));
    }

    @Override
    public double getProductivityByFieldId(String fieldId) {
        return calculateProductivity(findByFieldId(fieldId));
    }

    @Override
    public Production insert(Production production) {
        Field field = fieldService.findById(production.getField().getId());
        production.setField(field);
        production.setFarm(field.getFarm());
        return productionRepository.insert(production);
    }

    @Override
    public Production update(Production production) {
        Production persistProduction = findById(production.getId());
        updateDataFromTo(production, persistProduction);
        return productionRepository.save(persistProduction);
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

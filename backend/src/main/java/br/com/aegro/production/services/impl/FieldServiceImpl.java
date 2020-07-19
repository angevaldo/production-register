package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.repositories.FieldRepository;
import br.com.aegro.production.services.FarmService;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    FarmService farmService;

    @Autowired
    ProductionService productionService;

    private void updateFieldDataFromTo(Field fieldFrom, Field fieldTo) {
        fieldTo.setArea(fieldFrom.getArea());
        fieldTo.setName(fieldFrom.getName());
    }

    public FieldServiceImpl(@Lazy FieldRepository fieldRepository, @Lazy FarmService farmService, @Lazy ProductionService productionService) {
        this.fieldRepository = fieldRepository;
        this.farmService = farmService;
        this.productionService = productionService;
    }

    @Override
    public List<Field> findByFarmId(String farmId) {
        List<Field> fields = fieldRepository.findByFarmId(farmId);
        if (fields.size() == 0) {
            throw new ObjectNotFoundException(farmId);
        }
        return fields;
    }

    @Override
    public Field findById(String fieldId) {
        Optional<Field> obj = fieldRepository.findById(fieldId);
        return obj.orElseThrow(() -> new ObjectNotFoundException(fieldId));
    }

    @Override
    public Field insert(Field field) {
        Farm farm = farmService.findById(field.getFarm().getId());
        field.setFarm(farm);
        return fieldRepository.insert(field);
    }

    @Override
    public Field update(Field field) {
        Field persistField = findById(field.getId());
        updateFieldDataFromTo(field, persistField);
        return fieldRepository.save(field);
    }

    @Override
    public void deleteAll() {
        productionService.deleteAll();
        fieldRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        productionService.deleteByFieldId(id);
        fieldRepository.deleteById(id);
    }

    @Override
    public void deleteByFarmId(String farmId) {
        productionService.deleteByFarmId(farmId);
        fieldRepository.deleteByFarmId(farmId);
    }

}

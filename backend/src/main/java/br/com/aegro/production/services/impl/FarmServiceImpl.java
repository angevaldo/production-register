package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.services.FarmService;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmServiceImpl implements FarmService {

    @Autowired
    FarmRepository farmRepository;

    @Autowired
    ProductionService productionService;

    private void updateFarmDataFromTo(Farm farmFrom, Farm farmTo) {
        farmTo.setName(farmFrom.getName());
    }

    private void updateFieldDataFromTo(Field fieldFrom, Field fieldTo) {
        fieldTo.setArea(fieldFrom.getArea());
        fieldTo.setName(fieldFrom.getName());
    }

    @Override
    public List<Farm> findAllFarms() {
        return farmRepository.findAll();
    }

    @Override
    public Farm findFarmById(String farmId) {
        Optional<Farm> obj = farmRepository.findById(farmId);
        return obj.orElseThrow(() -> new ResourceNotFoundException(farmId));
    }

    @Override
    public Field findFieldByFieldsId(String fieldId) {
        Farm farm = farmRepository.findByFieldsId(fieldId);
        if (farm.getFields().size() == 0) {
            throw new ResourceNotFoundException(fieldId);
        }
        return farm.getFields().stream().filter(x -> x.getId().equals(fieldId)).findFirst().get();
    }

    @Override
    public Farm createFarm(Farm farm) {
        return farmRepository.insert(farm);
    }

    @Override
    public Farm updateFarm(Farm updatedFarm, String farmId) {
        Farm farm = findFarmById(farmId);
        updateFarmDataFromTo(updatedFarm, farm);
        farm = farmRepository.save(farm);

        return farm;
    }

    @Override
    public void deleteAllFarms() {
        productionService.deleteAll();
        farmRepository.deleteAll();
    }

    @Override
    public void deleteFarmById(String farmId) {
        Farm farm = findFarmById(farmId);

        productionService.deleteByFarmId(farmId);
        farmRepository.deleteById(farmId);
    }

    @Override
    public Field createField(Field field, String farmId) {
        Farm farm = findFarmById(farmId);
        farm.getFields().add(field);
        farmRepository.save(farm);

        return field;
    }

    @Override
    public Field updateField(Field updatedField, String farmId, String fieldId) {
        Farm farm = findFarmById(farmId);
        Optional<Field> field = farm.getFields().stream().filter(x -> x.getId().equals(fieldId)).findFirst();
        if (field.isEmpty()) {
            throw new ResourceNotFoundException(farmId, fieldId);
        }

        updateFieldDataFromTo(updatedField, field.get());
        farmRepository.save(farm);
        return field.get();
    }

    @Override
    public void deleteFieldById(String farmId, String fieldId) {
        Farm farm = findFarmById(farmId);
        if (!farm.getFields().removeIf(x -> x.getId().equals(fieldId))) {
            throw new ResourceNotFoundException(farmId, fieldId);
        }
        farmRepository.save(farm);
    }

    @Override
    public double getFarmProductivity(String farmId) {
        List<Production> productions = productionService.findByFarmId(farmId);
        if (productions.size() == 0) {
            throw new ResourceNotFoundException(farmId);
        }
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

    @Override
    public double getFieldProductivity(String fieldId) {
        List<Production> productions = productionService.findByFieldId(fieldId);
        if (productions.size() == 0) {
            throw new ResourceNotFoundException(fieldId);
        }
        return productions.stream().mapToDouble(Production::getProductivity).sum();
    }

}

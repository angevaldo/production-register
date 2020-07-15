package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    FarmRepository farmRepository;

    @Autowired
    ProductionService productionService;

    private Farm findFarmByFieldsId(String fieldId) {
        Optional<Farm> obj = farmRepository.findByFieldsId(fieldId);
        return obj.orElseThrow(() -> new ResourceNotFoundException(fieldId));
    }

    private void updateFieldDataFromTo(Field fieldFrom, Field fieldTo) {
        fieldTo.setArea(fieldFrom.getArea());
        fieldTo.setName(fieldFrom.getName());
    }

    @Override
    public List<Field> findByFarmId(String farmId) {
        Optional<Farm> farm = farmRepository.findById(farmId);
        if (farm.isEmpty()) {
            throw new ResourceNotFoundException(farmId);
        }

        List<Field> fields = new ArrayList<>();
        fields.addAll(farm.get().getFields());
        return fields;
    }

    @Override
    public Field findByFieldsId(String fieldId) {
        Farm farm = findFarmByFieldsId(fieldId);
        if (farm.getFields().size() == 0) {
            throw new ResourceNotFoundException(fieldId);
        }

        return farm.getFields().stream().filter(x -> x.getId().equals(fieldId)).findFirst().get();
    }

    @Override
    public Field create(Field field, String farmId) {
        Optional<Farm> farm = farmRepository.findById(farmId);
        if (farm.isEmpty()) {
            throw new ResourceNotFoundException(farmId);
        }

        farm.get().getFields().add(field);
        farmRepository.save(farm.get());

        return field;
    }

    @Override
    public Field update(Field field) {
        Farm farm = findFarmByFieldsId(field.getId());
        Optional<Field> transientField = farm.getFields().stream().filter(x -> x.getId().equals(field.getId())).findFirst();
        if (transientField.isEmpty()) {
            throw new ResourceNotFoundException(field.getId());
        }

        updateFieldDataFromTo(field, transientField.get());
        farmRepository.save(farm);

        return transientField.get();
    }

    @Override
    public void delete(String fieldId) {
        Farm farm = findFarmByFieldsId(fieldId);
        if (!farm.getFields().removeIf(x -> x.getId().equals(fieldId))) {
            throw new ResourceNotFoundException(fieldId);
        }

        farmRepository.save(farm);
        productionService.deleteByFieldId(fieldId);
    }

}

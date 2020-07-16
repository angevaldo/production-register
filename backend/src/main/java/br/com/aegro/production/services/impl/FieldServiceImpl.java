package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import com.sun.jdi.ObjectCollectedException;
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
        return obj.orElseThrow(() -> new ObjectNotFoundException(fieldId));
    }

    private Farm findFarmByFarmId(String farmId) {
        Optional<Farm> obj = farmRepository.findById(farmId);
        return obj.orElseThrow(() -> new ObjectNotFoundException(farmId));
    }

    private void updateFieldDataFromTo(Field fieldFrom, Field fieldTo) {
        fieldTo.setArea(fieldFrom.getArea());
        fieldTo.setName(fieldFrom.getName());
    }

    public FieldServiceImpl(FarmRepository farmRepository, ProductionService productionService) {
        this.farmRepository = farmRepository;
        this.productionService = productionService;
    }

    @Override
    public List<Field> findByFarmId(String farmId) {
        return new ArrayList<>(findFarmByFarmId(farmId).getFields());
    }

    @Override
    public Field findByFieldsId(String fieldId) {
        Farm farm = findFarmByFieldsId(fieldId);
        if (farm.getFields().size() == 0) {
            throw new ObjectNotFoundException(fieldId);
        }

        return farm.getFields().stream().filter(x -> x.getId().equals(fieldId)).findFirst().get();
    }

    @Override
    public Field create(Field field, String farmId) {
        Farm farm = findFarmByFarmId(farmId);
        farm.getFields().add(field);
        farmRepository.save(farm);

        return field;
    }

    @Override
    public Field update(Field field) {
        Farm farm = findFarmByFieldsId(field.getId());
        Optional<Field> transientField = farm.getFields().stream().filter(x -> x.getId().equals(field.getId())).findFirst();
        if (transientField.isEmpty()) {
            throw new ObjectNotFoundException(field.getId());
        }

        updateFieldDataFromTo(field, transientField.get());
        farmRepository.save(farm);

        return transientField.get();
    }

    @Override
    public void deleteById(String fieldId) {
        Farm farm = findFarmByFieldsId(fieldId);
        if (!farm.getFields().removeIf(x -> x.getId().equals(fieldId))) {
            throw new ObjectNotFoundException(fieldId);
        }

        farmRepository.save(farm);
        productionService.deleteByFieldId(fieldId);
    }

}

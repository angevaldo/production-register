package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.services.FarmService;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmServiceImpl implements FarmService {

    @Autowired
    FarmRepository farmRepository;

    @Autowired
    FieldService fieldService;

    private void updateFarmDataFromTo(Farm farmFrom, Farm farmTo) {
        farmTo.setName(farmFrom.getName());
    }

    public FarmServiceImpl(FarmRepository farmRepository, FieldService fieldService) {
        this.farmRepository = farmRepository;
        this.fieldService = fieldService;
    }

    @Override
    public List<Farm> findAll() {
        return farmRepository.findAll();
    }

    @Override
    public Farm findById(String id) {
        Optional<Farm> obj = farmRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(id));
    }

    @Override
    public Farm create(Farm farm) {
        return farmRepository.insert(farm);
    }

    @Override
    public Farm update(Farm farm) {
        Farm persistFarm = findById(farm.getId());
        updateFarmDataFromTo(farm, persistFarm);
        return farmRepository.save(persistFarm);
    }

    @Override
    public void deleteAll() {
        fieldService.deleteAll();
        farmRepository.deleteAll();
    }

    @Override
    public void deleteById(String id) {
        fieldService.deleteByFarmId(id);
        farmRepository.deleteById(id);
    }

}

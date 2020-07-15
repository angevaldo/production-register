package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
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

    @Override
    public List<Farm> findAll() {
        return farmRepository.findAll();
    }

    @Override
    public Farm findById(String id) {
        Optional<Farm> obj = farmRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public Farm create(Farm farm) {
        return farmRepository.save(farm);
    }

    @Override
    public Farm update(Farm farm) {
        Farm transientFarm = findById(farm.getId());
        updateFarmDataFromTo(farm, transientFarm);
        transientFarm = farmRepository.save(transientFarm);

        return transientFarm;
    }

    @Override
    public void deleteAll() {
        productionService.deleteAll();
        farmRepository.deleteAll();
    }

    @Override
    public void delete(String id) {
        farmRepository.deleteById(id);
        productionService.deleteByFarmId(id);
    }

}

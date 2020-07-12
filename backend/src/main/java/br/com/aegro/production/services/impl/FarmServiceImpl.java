package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.domain.repositories.ProductionRepository;
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
    private FarmRepository repository;

    @Autowired
    private ProductionRepository productionRepository;

    @Autowired
    private ProductionService productionService;

    @Override
    public List<Farm> findAll() {
        return repository.findAll();
    }

    @Override
    public Farm findById(String id) {
        Optional<Farm> obj = repository.findById(id);
        return obj.get();
    }

    @Override
    public Farm insert(Farm farm) {
        return repository.insert(farm);
    }

    @Override
    public Farm save(Farm farm) {
        return repository.save(farm);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public double getProductivity(String id) {
        List<Production> productions = productionService.findByFarmId(id);
        if (productions.size() == 0) {
            throw new ResourceNotFoundException(id);
        }
        return productions.stream().mapToDouble(x -> x.getProductivity()).sum();
    }
}

package br.com.aegro.production.services.impl;

import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.FieldRepository;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements br.com.aegro.production.services.FieldService {

    @Autowired
    private FieldRepository repository;

    @Autowired
    private ProductionService productionService;

    @Override
    public List<Field> findAll() {
        return repository.findAll();
    }

    @Override
    public Field findById(String id) {
        Optional<Field> obj = repository.findById(id);
        return obj.get();
    }

    @Override
    public Field insert(Field field) {
        return this.repository.insert(field);
    }

    @Override
    public Field save(Field field) {
        return this.repository.save(field);
    }

    @Override
    public double getProductivity(String id) {
        List<Production> productions = productionService.findByFieldId(id);
        if (productions.size() == 0) {
            throw new ResourceNotFoundException(id);
        }
        return productions.stream().mapToDouble(x -> x.getProductivity()).sum();
    }

}

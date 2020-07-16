package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Production;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductionRepository extends MongoRepository<Production, String> {

    List<Production> findByFieldId(String fieldId);

    List<Production> findByFarmId(String farmId);

    void deleteByFieldId(String fieldId);

    void deleteByFarmId(String farmId);

}

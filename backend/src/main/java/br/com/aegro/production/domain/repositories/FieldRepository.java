package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FieldRepository extends MongoRepository<Field, String> {

    List<Field> findByFarmId(String fieldId);

    void deleteByFarmId(String farmId);

}

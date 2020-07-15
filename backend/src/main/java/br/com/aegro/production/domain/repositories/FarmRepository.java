package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FarmRepository extends MongoRepository<Farm, String> {

    Optional<Farm> findByFieldsId(String fieldId);

}

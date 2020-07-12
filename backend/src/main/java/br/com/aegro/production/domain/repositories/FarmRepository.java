package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmRepository extends MongoRepository<Farm, String> {
}

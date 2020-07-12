package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FieldRepository extends MongoRepository<Field, String> {
}

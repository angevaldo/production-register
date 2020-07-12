package br.com.aegro.production.domain.repositories;

import br.com.aegro.production.domain.entities.Production;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductionRepository extends MongoRepository<Production, String> {

    @Query("{'field.$id':ObjectId('?0')}")
    List<Production> findByFieldId(String id);

    @Query("{'farm.$id':ObjectId('?0')}")
    List<Production> findByFarmId(String id);
}

package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;

import java.util.List;

public interface FarmService {

    List<Farm> findAll();

    Farm findById(String id);

    Farm insert(Farm farm);

    Farm update(Farm farm);

    void deleteAll();

    void deleteById(String id);

}

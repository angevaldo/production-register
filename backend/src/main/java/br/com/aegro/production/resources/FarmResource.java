package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.FarmDTO;
import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.services.FarmService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "/farms")
@Api(value = "Farm Endpoint", produces = "A Farm master data", tags = { "FarmEndpoint" })
public class FarmResource {

    private final ModelMapper modelMapper;

    public FarmResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    private FarmService service;

    @GetMapping
    public ResponseEntity<List<Farm>> findAll() {
        List<Farm> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Farm> findById(@PathVariable String id) {
        Farm farm = service.findById(id);
        return ResponseEntity.ok(farm);
    }

    @GetMapping(value = "/{id}/productivity")
    public ResponseEntity<Double> findByFarmId(@PathVariable String id) {
        double productivity = service.getProductivity(id);
        return ResponseEntity.ok(productivity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Farm> create(@RequestBody @Valid FarmDTO farmDTO) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm = service.insert(farm);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(farmDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(farm);
    }

    @PutMapping
    public ResponseEntity<Farm> update(@RequestBody @Valid FarmDTO farmDTO) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm = service.save(farm);
        return ResponseEntity.ok(farm);
    }

}

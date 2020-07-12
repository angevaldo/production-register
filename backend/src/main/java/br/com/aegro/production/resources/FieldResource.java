package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.FarmDTO;
import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.services.FieldService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "/fields")
@Api(value = "Field Endpoint", produces = "A Field master data", tags = { "FieldEndpoint" })
public class FieldResource {

    @Autowired
    private FieldService service;

    @GetMapping
    public ResponseEntity<List<Field>> findAll() {
        List<Field> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Field> findById(@PathVariable String id) {
        Field field = service.findById(id);
        return ResponseEntity.ok().body(field);
    }

    @GetMapping(value = "/{id}/productivity")
    public ResponseEntity<Double> findByFieldId(@PathVariable String id) {
        double productivity = service.getProductivity(id);
        return ResponseEntity.ok().body(productivity);
    }

    @PostMapping
    public ResponseEntity<Field> create(@RequestBody Field field) {
        field = service.insert(field);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(field.getId()).toUri();
        return ResponseEntity.created(uri).body(field);
    }

    @PutMapping
    public ResponseEntity<Field> update(@RequestBody @Valid Field field) {
        field = service.save(field);
        return ResponseEntity.ok(field);
    }

}

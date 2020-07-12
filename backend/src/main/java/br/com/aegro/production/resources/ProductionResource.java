package br.com.aegro.production.resources;

import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.services.ProductionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "/productions")
@Api(value = "Production Endpoint", produces = "A Production master data", tags = { "ProductionEndpoint" })
public class ProductionResource {

    @Autowired
    private ProductionService service;

    @GetMapping(value = "/findByFieldId")
    public ResponseEntity<List<Production>> findByFieldId(@PathParam("fieldId") String fieldId) {
        List<Production> list = service.findByFieldId(fieldId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/findByFarmId")
    public ResponseEntity<List<Production>> findByFarmId(@PathParam("farmId") String farmId) {
        List<Production> list = service.findByFarmId(farmId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Production> findById(@PathVariable String id) {
        Production production = service.findById(id);
        return ResponseEntity.ok().body(production);
    }

    @PostMapping
    public ResponseEntity<Production> create(@RequestBody Production production) {
        production = service.insert(production);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(production.getId()).toUri();
        return ResponseEntity.created(uri).body(production);
    }

    @PutMapping
    public ResponseEntity<Production> update(@RequestBody @Valid Production production) {
        production = service.save(production);
        return ResponseEntity.ok(production);
    }

}

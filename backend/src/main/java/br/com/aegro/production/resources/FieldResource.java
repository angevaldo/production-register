package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.FieldDTO;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.services.FieldService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/fields")
@Api(value = "Field Endpoint", produces = "A Field master data", tags = { "FieldEndpoint" })
public class FieldResource {

    @Autowired
    private FieldService service;

    private final ModelMapper modelMapper;

    public FieldResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<FieldDTO>> findAll() {
        List<Field> list = service.findAll();
        List<FieldDTO> listDTO = list.stream()
                                    .map(x -> modelMapper.map(x, FieldDTO.class))
                                    .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FieldDTO> findById(@PathVariable String id) {
        Field field = service.findById(id);
        FieldDTO fieldDTO = modelMapper.map(field, FieldDTO.class);

        return ResponseEntity.ok(fieldDTO);
    }

    @GetMapping(value = "/{id}/productivity")
    public ResponseEntity<Double> findByFieldId(@PathVariable String id) {
        double productivity = service.getProductivity(id);

        return ResponseEntity.ok(productivity);
    }

    @PostMapping
    public ResponseEntity<FieldDTO> create(@RequestBody @Valid FieldDTO fieldDTO) {
        Field field = modelMapper.map(fieldDTO, Field.class);
        field = service.insert(field);
        fieldDTO = modelMapper.map(field, FieldDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(field.getId()).toUri();

        return ResponseEntity.created(uri).body(fieldDTO);
    }

    @PutMapping
    public ResponseEntity<FieldDTO> update(@RequestBody @Valid FieldDTO fieldDTO) {
        Field field = service.findById(fieldDTO.getId());
        updateDataFromTo(fieldDTO, field);
        field = service.save(field);
        fieldDTO = modelMapper.map(field, FieldDTO.class);

        return ResponseEntity.ok(fieldDTO);
    }

    private void updateDataFromTo(FieldDTO fieldDTO, Field field) {
        field.setArea(fieldDTO.getArea());
        field.setName(fieldDTO.getName());
    }

}

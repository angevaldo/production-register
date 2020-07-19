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
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/fields")
@Api(value = "Field Endpoint", produces = "A Field master data", tags = { "FieldEndpoint" })
public class FieldResource {

    @Autowired
    FieldService fieldService;

    @Autowired
    ModelMapper modMapper;

    public FieldResource() {
    }

    @GetMapping
    public ResponseEntity<List<FieldDTO>> findByFarmId(@PathParam("farmId") String farmId) {
        List<Field> fields = fieldService.findByFarmId(farmId);
        List<FieldDTO> fieldsDTO = fields.stream()
                                        .map(x -> modMapper.map(x, FieldDTO.class))
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(fieldsDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FieldDTO> findById(@PathVariable String id) {
        Field field = fieldService.findById(id);
        FieldDTO fieldDTO = modMapper.map(field, FieldDTO.class);

        return ResponseEntity.ok(fieldDTO);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FieldDTO fieldDTO) {
        Field field = modMapper.map(fieldDTO, Field.class);
        field = fieldService.create(field);
        modMapper.map(field, FieldDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(field.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid FieldDTO fieldDTO,
                                           @PathVariable String id) {
        Field field = modMapper.map(fieldDTO, Field.class);
        field.setId(id);
        fieldService.update(field);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        fieldService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

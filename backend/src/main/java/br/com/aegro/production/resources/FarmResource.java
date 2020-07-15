package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.FarmDTO;
import br.com.aegro.production.domain.dto.FieldDTO;
import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/farms")
@Api(value = "Farm Endpoint", produces = "A Farm master data", tags = { "FarmEndpoint" })
public class FarmResource {

    @Autowired
    private FarmService farmService;

    private final ModelMapper modelMapper;

    public FarmResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<FarmDTO>> findAllFarms() {
        List<Farm> list = farmService.findAllFarms();
        List<FarmDTO> listDTO = list.stream()
                                    .map(x -> modelMapper.map(x, FarmDTO.class))
                                    .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(value = "/{farmId}")
    public ResponseEntity<FarmDTO> findFarmById(@PathVariable String farmId) {
        Farm farm = farmService.findFarmById(farmId);
        FarmDTO farmDTO = modelMapper.map(farm, FarmDTO.class);
        return ResponseEntity.ok(farmDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FarmDTO> createFarm(@RequestBody @Valid FarmDTO farmDTO) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm = farmService.createFarm(farm);
        farmDTO = modelMapper.map(farm, FarmDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{fieldId}")
                    .buildAndExpand(farmDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(farmDTO);
    }

    @PutMapping(value = "/{farmId}")
    public ResponseEntity<FarmDTO> updateFarm(@RequestBody @Valid FarmDTO farmDTO,
                                              @PathVariable("farmId") String farmId) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm = farmService.updateFarm(farm, farmId);
        farmDTO = modelMapper.map(farm, FarmDTO.class);

        return ResponseEntity.ok(farmDTO);
    }

    @DeleteMapping(value = "/{farmId}")
    public ResponseEntity<String> deleteFarm(@PathVariable String farmId) {
        farmService.deleteFarmById(farmId);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{farmId}/fields/")
    public ResponseEntity<Set<FieldDTO>> findAllFieldsByFarmId(@PathVariable String farmId) {
        Farm farm = farmService.findFarmById(farmId);
        Set<FieldDTO> fieldsDTO = farm.getFields().stream().map(x -> modelMapper.map(x, FieldDTO.class)).collect(Collectors.toSet());

        return ResponseEntity.ok(fieldsDTO);
    }

    @GetMapping(value = "/{farmId}/fields/{fieldId}")
    public ResponseEntity<FieldDTO> findFieldByFieldId(@PathVariable String fieldId) {
        Field field = farmService.findFieldByFieldsId(fieldId);
        FieldDTO fieldDTO = modelMapper.map(field, FieldDTO.class);

        return ResponseEntity.ok(fieldDTO);
    }

    @PostMapping(value = "/{farmId}/fields")
    public ResponseEntity<FieldDTO> createField(@RequestBody @Valid FieldDTO fieldDTO,
                                                @PathVariable("farmId") @Valid String farmId) {
        Field field = modelMapper.map(fieldDTO, Field.class);
        field = farmService.createField(field, farmId);
        fieldDTO = modelMapper.map(field, FieldDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                .buildAndExpand(field.getId()).toUri();

        return ResponseEntity.created(uri).body(fieldDTO);
    }

    @PutMapping(value = "/{farmId}/fields/{fieldId}")
    public ResponseEntity<FieldDTO> updateField(@RequestBody @Valid FieldDTO fieldDTO,
                                                @PathVariable("farmId") @Valid String farmId,
                                                @PathVariable("fieldId") @Valid String fieldId) {
        Field field = modelMapper.map(fieldDTO, Field.class);
        field = farmService.updateField(field, farmId, fieldId);
        fieldDTO = modelMapper.map(field, FieldDTO.class);

        return ResponseEntity.ok(fieldDTO);
    }

    @DeleteMapping(value = "/{farmId}/fields/{fieldId}")
    public ResponseEntity<String> deleteField(@PathVariable String farmId,
                                              @PathVariable String fieldId) {
        farmService.deleteFieldById(farmId, fieldId);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{farmId}/productivity")
    public ResponseEntity<Double> getFarmProductivity(@PathVariable String farmId) {
        double productivity = farmService.getFarmProductivity(farmId);
        return ResponseEntity.ok(productivity);
    }

    @GetMapping(value = "/{farmId}/fields/{fieldId}/productivity")
    public ResponseEntity<Double> getFieldProductivity(@PathVariable String fieldId) {
        double productivity = farmService.getFieldProductivity(fieldId);
        return ResponseEntity.ok(productivity);
    }

}

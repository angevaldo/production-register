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
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/farms")
@Api(value = "Farm Endpoint", produces = "A Farm master data", tags = { "FarmEndpoint" })
public class FarmResource {

    @Autowired
    FarmService farmService;

    @Autowired
    ModelMapper modelMapper;

    public FarmResource() {
    }

    @GetMapping
    public ResponseEntity<List<FarmDTO>> findAll() {
        List<Farm> list = farmService.findAll();
        List<FarmDTO> listDTO = list.stream()
                                    .map(x -> modelMapper.map(x, FarmDTO.class))
                                    .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FarmDTO> findById(@PathVariable String id) {
        Farm farm = farmService.findById(id);
        FarmDTO farmDTO = modelMapper.map(farm, FarmDTO.class);
        return ResponseEntity.ok(farmDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FarmDTO> create(@RequestBody @Valid FarmDTO farmDTO) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm = farmService.create(farm);
        farmDTO = modelMapper.map(farm, FarmDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{fieldId}")
                    .buildAndExpand(farmDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(farmDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<FarmDTO> update(@RequestBody @Valid FarmDTO farmDTO,
                                              @PathVariable("id") String id) {
        Farm farm = modelMapper.map(farmDTO, Farm.class);
        farm.setId(id);
        farm = farmService.update(farm);
        farmDTO = modelMapper.map(farm, FarmDTO.class);

        return ResponseEntity.ok(farmDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable String id) {
        farmService.deleteById(id);
    }

}

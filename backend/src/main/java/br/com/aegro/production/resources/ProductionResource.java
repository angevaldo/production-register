package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.ProductionDTO;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.services.ProductionService;
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
@RequestMapping(value = "/api/productions")
@Api(value = "Production Endpoint", produces = "A Production master data", tags = { "ProductionEndpoint" })
public class ProductionResource {

    @Autowired
    private ProductionService service;

    private final ModelMapper modelMapper;

    public ProductionResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/findByFieldId")
    public ResponseEntity<List<ProductionDTO>> findByFieldId(@PathParam("fieldId") String fieldId) {
        List<Production> list = service.findByFieldId(fieldId);
        List<ProductionDTO> listDTO = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(value = "/findByFarmId")
    public ResponseEntity<List<ProductionDTO>> findByFarmId(@PathParam("farmId") String farmId) {
        List<Production> list = service.findByFarmId(farmId);
        List<ProductionDTO> listDto = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductionDTO> findById(@PathVariable String id) {
        Production production = service.findById(id);
        ProductionDTO productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @PostMapping
    public ResponseEntity<ProductionDTO> create(@RequestBody @Valid ProductionDTO productionDTO) {
        Production production = modelMapper.map(productionDTO, Production.class);
        production = service.insert(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(production.getId()).toUri();

        return ResponseEntity.created(uri).body(productionDTO);
    }

    @PutMapping
    public ResponseEntity<ProductionDTO> update(@RequestBody @Valid ProductionDTO productionDTO) {
        Production production = service.findById(productionDTO.getId());
        updateDataFromTo(productionDTO, production);
        production = service.save(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    private void updateDataFromTo(ProductionDTO productionDTO, Production production) {
        production.setValue(productionDTO.getValue());
    }

}

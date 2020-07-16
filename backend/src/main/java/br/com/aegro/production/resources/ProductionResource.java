package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.ProductionDTO;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.entities.exceptions.ProductivityException;
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
    private ProductionService productionService;

    private final ModelMapper modelMapper;

    public ProductionResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private void updateDataFromTo(ProductionDTO productionDTO, Production production) throws ProductivityException {
        production.setValue(productionDTO.getValue());
    }

    @GetMapping(value = "/{productionId}")
    public ResponseEntity<ProductionDTO> findById(@PathVariable String productionId) {
        Production production = productionService.findById(productionId);
        ProductionDTO productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @GetMapping(value = "/findByFarmId")
    public ResponseEntity<List<ProductionDTO>> findByFarmId(@PathParam("farmId") @Valid String farmId) {
        List<Production> list =  productionService.findByFarmId(farmId);
        List<ProductionDTO> listDto = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @GetMapping(value = "/findByFieldId")
    public ResponseEntity<List<ProductionDTO>> findByFieldId(@PathParam("fieldId") @Valid String fieldId) {
        List<Production> list =  productionService.findByFieldId(fieldId);
        List<ProductionDTO> listDto = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @GetMapping(value = "/productivityByFarmId")
    public ResponseEntity<Double> getProductivityByFarmId(@PathParam("farmId") @Valid String farmId) {
        double productivity = productionService.getProductivityByFarmId(farmId);
        return ResponseEntity.ok(productivity);
    }

    @GetMapping(value = "/productivityByFieldId")
    public ResponseEntity<Double> getProductivityByFieldId(@PathParam("fieldId") @Valid String fieldId) {
        double productivity = productionService.getProductivityByFieldId(fieldId);
        return ResponseEntity.ok(productivity);
    }

    @PostMapping
    public ResponseEntity<ProductionDTO> create(@RequestBody @Valid ProductionDTO productionDTO) {
        Production production = modelMapper.map(productionDTO, Production.class);
        production = productionService.create(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{productionId}")
                    .buildAndExpand(production.getId()).toUri();

        return ResponseEntity.created(uri).body(productionDTO);
    }

    @PutMapping(value = "/{productionId}")
    public ResponseEntity<ProductionDTO> update(@RequestBody @Valid ProductionDTO productionDTO,
                                                @PathVariable String productionId) throws ProductivityException {
        Production production = productionService.findById(productionId);
        updateDataFromTo(productionDTO, production);
        production = productionService.update(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @DeleteMapping(path = "/{productionId}")
    public void deleteById(@PathVariable String productionId) {
        this.productionService.deleteById(productionId);
    }

}

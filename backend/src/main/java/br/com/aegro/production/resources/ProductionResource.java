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
    private ProductionService productionService;

    private final ModelMapper modelMapper;

    public ProductionResource(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private void updateDataFromTo(ProductionDTO productionDTO, Production production) {
        production.setValue(productionDTO.getValue());
    }

    @GetMapping(value = "/findByFarmId")
    public ResponseEntity<List<ProductionDTO>> findProductionsByFarmId(@PathParam("farmId") String farmId) {
        List<Production> list = productionService.findByFarmId(farmId);
        List<ProductionDTO> listDto = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @GetMapping(value = "/findByFieldId")
    public ResponseEntity<List<ProductionDTO>> findProductionsByFieldId(@PathParam("fieldId") String fieldId) {
        List<Production> list = productionService.findByFieldId(fieldId);
        List<ProductionDTO> listDTO = list.stream().map(x -> modelMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @GetMapping(value = "/{productionId}")
    public ResponseEntity<ProductionDTO> findProductionById(@PathVariable String productionId) {
        Production production = productionService.findById(productionId);
        ProductionDTO productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @PostMapping
    public ResponseEntity<ProductionDTO> createProduction(@RequestBody @Valid ProductionDTO productionDTO) {
        Production production = modelMapper.map(productionDTO, Production.class);
        production = productionService.create(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{productionId}")
                    .buildAndExpand(production.getId()).toUri();

        return ResponseEntity.created(uri).body(productionDTO);
    }

    @PutMapping
    public ResponseEntity<ProductionDTO> updateProduction(@RequestBody @Valid ProductionDTO productionDTO) {
        Production production = productionService.findById(productionDTO.getId());
        updateDataFromTo(productionDTO, production);
        production = productionService.update(production);
        productionDTO = modelMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @DeleteMapping(path = "{productionId}")
    public void deleteProduction(@PathVariable @Valid String productionId) {
        this.productionService.deleteById(productionId);
    }

}

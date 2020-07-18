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
    ProductionService productionService;

    @Autowired
    ModelMapper modMapper;

    public ProductionResource() {
    }

    @GetMapping(value = "/{productionId}")
    public ResponseEntity<ProductionDTO> findById(@PathVariable String productionId) {
        Production production = productionService.findById(productionId);
        ProductionDTO productionDTO = modMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @GetMapping(value = "/findByFarmId")
    public ResponseEntity<List<ProductionDTO>> findByFarmId(@PathParam("farmId") @Valid String farmId) {
        List<Production> list =  productionService.findByFarmId(farmId);
        List<ProductionDTO> listDto = list.stream().map(x -> modMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDto);
    }

    @GetMapping(value = "/findByFieldId")
    public ResponseEntity<List<ProductionDTO>> findByFieldId(@PathParam("fieldId") @Valid String fieldId) {
        List<Production> list =  productionService.findByFieldId(fieldId);
        List<ProductionDTO> listDto = list.stream().map(x -> modMapper.map(x, ProductionDTO.class))
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
        Production production = modMapper.map(productionDTO, Production.class);
        production = productionService.create(production);
        productionDTO = modMapper.map(production, ProductionDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{productionId}")
                    .buildAndExpand(production.getId()).toUri();

        return ResponseEntity.created(uri).body(productionDTO);
    }

    @PutMapping(value = "/{productionId}")
    public ResponseEntity<ProductionDTO> update(@RequestBody @Valid ProductionDTO productionDTO,
                                                @PathVariable String productionId) throws ProductivityException {
        Production production = modMapper.map(productionDTO, Production.class);
        production.setId(productionId);
        production = productionService.update(production);
        productionDTO = modMapper.map(production, ProductionDTO.class);

        return ResponseEntity.ok(productionDTO);
    }

    @DeleteMapping(path = "/{productionId}")
    public void deleteById(@PathVariable String productionId) {
        this.productionService.deleteById(productionId);
    }

}

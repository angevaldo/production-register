package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.entities.exceptions.ProductivityException;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.domain.repositories.ProductionRepository;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import br.com.aegro.production.services.impl.FarmServiceImpl;
import br.com.aegro.production.services.impl.ProductionServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
public class ProductionServiceTests {

    ProductionService productionService;

    @Mock
    ProductionRepository productionRepository;

    @Mock
    FarmService farmService;

    private Field field1;
    private Field field2;
    private Farm farm;
    private Production production1_1;
    private Production production1_2;
    private Production production2_1;
    private Production production2_2;

    @BeforeEach
    public void setUp() throws ProductivityException {
        this.productionService = new ProductionServiceImpl(productionRepository);

        this.field1 = new Field(ObjectId.get().toString(), "Field 1", 100d);
        this.field2 = new Field(ObjectId.get().toString(), "Field 2", 200d);
        this.farm = new Farm(ObjectId.get().toString(), "Farm");
        this.farm.getFields().addAll(Arrays.asList(field1, field2));
        this.production1_1 = new Production(ObjectId.get().toString(), 50, farm, field1);
        this.production1_2 = new Production(ObjectId.get().toString(), 25, farm, field1);
        this.production2_1 = new Production(ObjectId.get().toString(), 100, farm, field2);
        this.production2_2 = new Production(ObjectId.get().toString(), 75, farm, field2);
    }

    @Test
    @DisplayName("Should throws exception when invalid filter")
    public void updateAndFindById_invalidFilter_notFoundException() throws ProductivityException {
        // scenario
        Production production = new Production(null, 10d, farm, field1);

        // verification
        assertThrows(ObjectNotFoundException.class, () -> productionService.findById(production.getId()));
    }

    @Test
    @DisplayName("Should return all farm productions.")
    public void findByFarm_farmId_productionsList() throws ProductivityException {
        // scenario
        List<Production> expectedProductions = new ArrayList<>();
        expectedProductions.addAll(Arrays.asList(production1_1, production1_2, production2_1, production2_2));

        when(productionRepository.findByFarmId(farm.getId())).thenReturn(expectedProductions);

        // execution
        List<Production> actualProductions = productionService.findByFarmId(farm.getId());

        // verification
        assertTrue(actualProductions.containsAll(expectedProductions));
    }

    @Test
    @DisplayName("Should return all field productions.")
    public void findByFarm_fieldId_productionsList() throws ProductivityException {
        // scenario
        List<Production> expectedProductions = new ArrayList<>();
        expectedProductions.addAll(Arrays.asList(production1_1, production1_2));

        List<Production> actualProductions = new ArrayList<>();
        actualProductions.addAll(Arrays.asList(production1_1, production1_2, production2_1, production2_2));

        when(productionRepository.findByFieldId(field1.getId())).thenReturn(expectedProductions);

        // execution
        actualProductions = productionService.findByFieldId(field1.getId());

        // verification
        assertTrue(actualProductions.containsAll(expectedProductions));
    }
/*
    @Test
    @DisplayName("Should return a production with passed id.")
    public void findById_validId_production() throws Exception {
        // scenario
        String productionId = ObjectId.get().toString();

        Production expectedProduction = new Production(productionId, "Production 1");

        when(productionRepository.findById(productionId)).thenReturn(Optional.of(expectedProduction));

        // execution
        Production actualProduction = productionService.findById(productionId);

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getName(), actualProduction.getName());
    }

    @Test
    @DisplayName("Should create and return a new production.")
    public void create_production_production() throws Exception {
        // scenario
        String productionId = ObjectId.get().toString();

        Production actualProduction = new Production(null, "Production 1");
        Production expectedProduction = new Production(productionId, "Production 1");

        when(productionRepository.insert(actualProduction)).thenReturn(expectedProduction);

        // execution
        actualProduction = productionService.create(actualProduction);

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getName(), actualProduction.getName());
    }

    @Test
    @DisplayName("Should update and return a production.")
    public void update_production_production() throws Exception {
        // scenario
        String productionId = ObjectId.get().toString();

        Production actualProduction = new Production(productionId, "Production old");
        Production expectedProduction = new Production(productionId, "Production new");

        when(productionRepository.findById(productionId)).thenReturn(Optional.of(actualProduction));
        when(productionRepository.save(expectedProduction)).thenReturn(expectedProduction);

        // execution
        actualProduction = productionService.update(actualProduction);

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getName(), actualProduction.getName());
    }

    @Test
    @DisplayName("Should delete all productions.")
    public void deleteAll_void_void() throws Exception {
        // execution
        productionService.deleteAll();

        // verification
        verify(productionRepository, times(1)).deleteAll();
        verify(productionService, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should delete a production with passed id.")
    public void deleteById_validId_void() throws Exception {
        // scenario
        String productionId = ObjectId.get().toString();

        // execution
        productionService.deleteById(productionId);

        // verification
        verify(productionRepository, times(1)).deleteById(eq(productionId));
        verify(productionService, times(1)).deleteByProductionId(eq(productionId));
    }
*/
}

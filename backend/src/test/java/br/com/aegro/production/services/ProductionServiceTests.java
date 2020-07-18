package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.entities.exceptions.ProductivityException;
import br.com.aegro.production.domain.repositories.ProductionRepository;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import br.com.aegro.production.services.impl.ProductionServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
public class ProductionServiceTests {

    private ProductionService productionService;

    @Mock
    private ProductionRepository productionRepository;

    private Farm farm;

    private Field field_1;
    private Field field_2;

    private Production prod_1_1;
    private Production prod_1_2;
    private Production prod_2_1;
    private final List<Production> productions = new ArrayList<>();

    @BeforeEach
    public void setUp() throws ProductivityException {
        this.productionService = new ProductionServiceImpl(productionRepository);

        this.farm = new Farm(ObjectId.get().toString(), "Farm");

        this.field_1 = new Field(ObjectId.get().toString(), "Field 1", 100d, farm);
        this.field_2 = new Field(ObjectId.get().toString(), "Field 2", 250d, farm);
        this.farm.getFields().addAll(Arrays.asList(field_1, field_2));

        this.prod_1_1 = new Production(ObjectId.get().toString(), 50, farm, field_1);
        this.prod_1_2 = new Production(ObjectId.get().toString(), 25, farm, field_1);
        this.prod_2_1 = new Production(ObjectId.get().toString(), 100, farm, field_2);
        this.productions.addAll(Arrays.asList(prod_1_1, prod_1_2, prod_2_1));
    }

    @Test
    @DisplayName("Should throws exception when invalid filter")
    public void findById_invalidFilter_notFoundException() throws ProductivityException {
        // scenario
        Production production = new Production(null, 10d, farm, field_1);

        // verification
        assertThrows(ObjectNotFoundException.class, () -> productionService.findById(production.getId()));
    }

    @Test
    @DisplayName("Should return a production with passed id.")
    public void findById_validId_production() {
        // scenario
        Production expectedProduction = prod_1_1;
        when(productionRepository.findById(prod_1_1.getId())).thenReturn(Optional.of(expectedProduction));

        // execution
        Production actualProduction = productionService.findById(prod_1_1.getId());

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getValue(), actualProduction.getValue());
        assertEquals(expectedProduction.getProductivity(), actualProduction.getProductivity());
        assertEquals(expectedProduction.getFarm(), actualProduction.getFarm());
        assertEquals(expectedProduction.getField(), actualProduction.getField());
    }

    @Test
    @DisplayName("Should return all field productions.")
    public void findByFieldId_fieldId_productionsList() {
        // scenario
        List<Production> expectedProductions = Arrays.asList(prod_1_1, prod_1_2);
        when(productionRepository.findByFieldId(field_1.getId())).thenReturn(expectedProductions);

        // execution
        List<Production> actualProductions = productionService.findByFieldId(field_1.getId());

        // verification
        assertTrue(actualProductions.containsAll(expectedProductions));
    }

    @Test
    @DisplayName("Should return all farm productions.")
    public void findByFarmId_farmId_productionsList() {
        // scenario
        List<Production> expectedProductions = new ArrayList<>(productions);
        when(productionRepository.findByFarmId(farm.getId())).thenReturn(expectedProductions);

        // execution
        List<Production> actualProductions = productionService.findByFarmId(farm.getId());

        // verification
        assertTrue(actualProductions.containsAll(expectedProductions));
    }

    @Test
    @DisplayName("Should return the productivity from a field")
    public void productivityByFieldId_fieldId_productivity()  {
        // scenario
        String fieldId = field_1.getId();
        double expectedProductivity = .75d; // productivity = (50/100) + (25/100) = 0,75
        when(productionRepository.findByFieldId(fieldId)).thenReturn(Arrays.asList(prod_1_1, prod_1_2));

        // execution
        double actualProductivity = productionService.getProductivityByFieldId(fieldId);

        // verification
        assertEquals(expectedProductivity, actualProductivity);
    }

    @Test
    @DisplayName("Should return the productivity from a farm")
    public void productivityByFarmId_farmId_productivity() {
        // scenario
        String farmId = farm.getId();
        double expectedProductivity = 1.15d; // productivity = (50/100) + (25/100) + (100/250) = 1,15
        when(productionRepository.findByFarmId(farmId)).thenReturn(Arrays.asList(prod_1_1, prod_1_2, prod_2_1));

        // execution
        double actualProductivity = productionService.getProductivityByFarmId(farmId);

        // verification
        assertEquals(expectedProductivity, actualProductivity);
    }

    @Test
    @DisplayName("Should create and return a new production.")
    public void create_production_production() throws ProductivityException {
        // scenario
        Production expectedProduction = prod_2_1;
        Production actualProduction = new Production(null, prod_2_1.getValue(), farm, field_2);
        when(productionRepository.insert(actualProduction)).thenReturn(expectedProduction);

        // execution
        actualProduction = productionService.create(actualProduction);

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getValue(), actualProduction.getValue());
        assertEquals(expectedProduction.getProductivity(), actualProduction.getProductivity());
        assertEquals(expectedProduction.getFarm(), actualProduction.getFarm());
        assertEquals(expectedProduction.getField(), actualProduction.getField());
    }

    @Test
    @DisplayName("Should update and return a production.")
    public void update_production_production() throws ProductivityException {
        // scenario
        Production expectedProduction = prod_1_1;
        Production actualProduction = new Production(prod_1_1.getId(), 33d, farm, field_1);
        when(productionRepository.findById(expectedProduction.getId())).thenReturn(Optional.of(expectedProduction));
        when(productionRepository.save(expectedProduction)).thenReturn(expectedProduction);

        // execution
        actualProduction = productionService.update(actualProduction);

        // verification
        assertEquals(expectedProduction.getId(), actualProduction.getId());
        assertEquals(expectedProduction.getValue(), actualProduction.getValue());
        assertEquals(expectedProduction.getProductivity(), actualProduction.getProductivity());
        assertEquals(expectedProduction.getFarm(), actualProduction.getFarm());
        assertEquals(expectedProduction.getField(), actualProduction.getField());
    }

    @Test
    @DisplayName("Should delete all productions.")
    public void deleteAll_void_void() {
        // execution
        productionService.deleteAll();

        // verification
        verify(productionRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should delete a production with passed id.")
    public void deleteById_validId_void() {
        // scenario
        String productionId = prod_1_1.getId();
        when(productionRepository.findById(productionId)).thenThrow(ObjectNotFoundException.class);

        // execution
        productionService.deleteById(productionId);

        // verification
        verify(productionRepository, times(1)).deleteById(eq(productionId));
        assertThrows(ObjectNotFoundException.class, () -> productionService.findById(productionId));
    }

    @Test
    @DisplayName("Should delete a production with passed Field id.")
    public void deleteByFieldId_validId_void() {
        // scenario
        String fieldId = field_1.getId();
        List<Production> expectedProductions = Collections.singletonList(prod_1_2);
        when(productionRepository.findByFieldId(fieldId)).thenReturn(expectedProductions);

        // execution
        productionService.deleteByFieldId(fieldId);
        List<Production> actualProductions = productionService.findByFieldId(fieldId);

        // verification
        verify(productionRepository, times(1)).deleteByFieldId(eq(fieldId));
        assertEquals(actualProductions, expectedProductions);
    }

    @Test
    @DisplayName("Should delete a production with passed Farm id.")
    public void deleteByFarmId_validId_void() {
        // scenario
        String farmId = farm.getId();
        when(productionRepository.findByFarmId(farmId)).thenThrow(ObjectNotFoundException.class);

        // execution
        productionService.deleteByFarmId(farmId);

        // verification
        verify(productionRepository, times(1)).deleteByFarmId(eq(farmId));
        assertThrows(ObjectNotFoundException.class, () -> productionService.findByFarmId(farmId));
    }

}

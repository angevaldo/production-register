package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.repositories.FieldRepository;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import br.com.aegro.production.services.impl.FieldServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
public class FieldServiceTests {

    private FieldService fieldService;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private ProductionService productionService;

    @BeforeEach
    public void setUp() {
        this.fieldService = new FieldServiceImpl(fieldRepository, productionService);
    }

    @Test
    @DisplayName("Should throws exception when invalid filter.")
    public void updateAndFindById_invalidFilter_notFoundException() {
        // scenario
        Field expectedField = new Field(null, null, 0, new Farm(null, null));

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.update(expectedField));
        assertThrows(ObjectNotFoundException.class, () -> fieldService.findById(null));
    }

    @Test
    @DisplayName("Should return all fields.")
    public void findByFarmId_farmId_fieldsList() {
        // scenario
        Farm farm = new Farm(ObjectId.get().toString(), "Farm 1");
        Field field1 = new Field(ObjectId.get().toString(), "Field 1", 10d, farm);
        Field field2 = new Field(ObjectId.get().toString(), "Field 2", 20d, farm);

        when(fieldRepository.findByFarmId(farm.getId())).thenReturn(Arrays.asList(field1, field2));

        // execution
        List<Field> actualFields = fieldService.findByFarmId(farm.getId());

        // verification
        assertTrue(actualFields.containsAll(farm.getFields()));
    }

    @Test
    @DisplayName("Should return a field with passed id.")
    public void findById_validId_field() {
        // scenario
        Farm expectedFarm = new Farm(ObjectId.get().toString(), "Farm 1");
        Field expectedField = new Field(ObjectId.get().toString(), "Field 1", 10d, expectedFarm);

        when(fieldRepository.findById(expectedField.getId())).thenReturn(Optional.of(expectedField));

        // execution
        Field actualField = fieldService.findById(expectedField.getId());

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
        assertEquals(expectedField.getFarm(), actualField.getFarm());
    }

    @Test
    @DisplayName("Should create and return a new field.")
    public void create_field_field() {
        // scenario
        Farm farm = new Farm(ObjectId.get().toString(), "Farm 1");
        Field expectedField = new Field(ObjectId.get().toString(), "Field 1", 10d, farm);
        Field actualField = new Field(null, "Field 1", 10d, farm);

        when(fieldRepository.insert(actualField)).thenReturn(expectedField);

        // execution
        actualField = fieldService.create(actualField, farm.getId());

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
        assertEquals(expectedField.getFarm(), actualField.getFarm());
    }

    @Test
    @DisplayName("Should update and return a field.")
    public void update_field_field() {
        // scenario
        String fieldId = ObjectId.get().toString();
        Farm farm = new Farm(ObjectId.get().toString(), "Farm 1");
        Field expectedField = new Field(fieldId, "Field new", 10d, farm);
        Field actualField = new Field(fieldId, "Field old", 15d, farm);

        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(actualField));
        when(fieldRepository.save(expectedField)).thenReturn(expectedField);

        // execution
        actualField = fieldService.update(expectedField);

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
        assertEquals(expectedField.getFarm(), actualField.getFarm());
    }

    @Test
    @DisplayName("Should throws exception when field not found on update.")
    public void update_field_notFoundException() {
        // scenario
        Farm farm = new Farm(ObjectId.get().toString(), "Farm 1");
        Field expectedField = new Field(ObjectId.get().toString(), "Field new", 10d, farm);
        when(fieldRepository.findById(expectedField.getId())).thenThrow(ObjectNotFoundException.class);

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.update(expectedField));
    }

    @Test
    @DisplayName("Should delete a field with passed id.")
    public void deleteById_validId_void() {
        // scenario
        String fieldId = ObjectId.get().toString();

        // execution
        fieldService.deleteById(fieldId);

        // verification
        verify(fieldRepository, times(1)).deleteById(eq(fieldId));
        verify(productionService, times(1)).deleteByFieldId(eq(fieldId));
    }

    @Test
    @DisplayName("Should delete fields from farm with passed farm id.")
    public void deleteByFarmId_validId_void() {
        // scenario
        String farmId = ObjectId.get().toString();

        // execution
        fieldService.deleteByFarmId(farmId);

        // verification
        verify(fieldRepository, times(1)).deleteByFarmId(eq(farmId));
        verify(productionService, times(1)).deleteByFarmId(eq(farmId));
    }

}

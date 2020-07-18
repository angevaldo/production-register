package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.repositories.FarmRepository;
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
/*
    @Test
    @DisplayName("Should throws exception when invalid filter.")
    public void updateAndFindById_invalidFilter_notFoundException() {
        // scenario
        Field expectedField = new Field(null, null, 0);

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.update(expectedField));
        assertThrows(ObjectNotFoundException.class, () -> fieldService.findByFarmId(null));
        assertThrows(ObjectNotFoundException.class, () -> fieldService.findById(null));
    }

    @Test
    @DisplayName("Should throws exception when field is not found.")
    public void findById_fieldId_notFoundException() {
        // scenario
        Field expectedField = new Field(ObjectId.get().toString(), "Field 1", 5d);
        Farm expectedFarm = new Farm(ObjectId.get().toString(), "Farm 1");
        when(fieldRepository.findByFieldsId(expectedField.getId())).thenReturn(Optional.of(expectedFarm));

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.findById(expectedField.getId()));
    }

    @Test
    @DisplayName("Should return all fields.")
    public void findByFarmId_farmId_fieldsList() {
        // scenario
        String farmId = ObjectId.get().toString();

        Field field1 = new Field(ObjectId.get().toString(), "Field 1", 10d);
        Field field2 = new Field(ObjectId.get().toString(), "Field 2", 20d);

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        expectedFarm.getFields().addAll(Arrays.asList(field1, field2));

        when(fieldRepository.findById(farmId)).thenReturn(Optional.of(expectedFarm));

        // execution
        List<Field> actualFields = fieldService.findByFarmId(farmId);

        // verification
        assertTrue(actualFields.containsAll(expectedFarm.getFields()));
    }

    @Test
    @DisplayName("Should return a field with passed id.")
    public void findById_validId_field() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Field expectedField = new Field(fieldId, "Field 1", 10d);
        expectedFarm.getFields().add(expectedField);

        when(fieldRepository.findByFieldsId(expectedField.getId())).thenReturn(Optional.of(expectedFarm));

        // execution
        Field actualField = fieldService.findById(expectedField.getId());

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
    }

    @Test
    @DisplayName("Should create and return a new field.")
    public void create_field_field() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Field expectedField = new Field(fieldId, "Field 1", 10d);
        expectedFarm.getFields().add(expectedField);

        Farm actualFarm = new Farm(farmId, "Farm 1");
        Field actualField = new Field(fieldId, "Field 1", 10d);

        when(fieldRepository.findById(farmId)).thenReturn(Optional.of(actualFarm));
        when(fieldRepository.save(expectedFarm)).thenReturn(expectedFarm);

        // execution
        actualField = fieldService.create(actualField, farmId);

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
    }

    @Test
    @DisplayName("Should update and return a field.")
    public void update_field_field() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Field expectedField = new Field(fieldId, "Field new", 10d);
        expectedFarm.getFields().add(expectedField);

        Farm actualFarm = new Farm(farmId, "Farm 1");
        Field actualField = new Field(fieldId, "Field old", 15d);
        actualFarm.getFields().add(actualField);

        when(fieldRepository.findByFieldsId(fieldId)).thenReturn(Optional.of(actualFarm));
        when(fieldRepository.save(expectedFarm)).thenReturn(expectedFarm);

        // execution
        actualField = fieldService.update(expectedField);

        // verification
        assertEquals(expectedField.getId(), actualField.getId());
        assertEquals(expectedField.getName(), actualField.getName());
        assertEquals(expectedField.getArea(), actualField.getArea());
    }

    @Test
    @DisplayName("Should throws exception when field not found on update.")
    public void update_field_notFoundException() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Field expectedField = new Field(fieldId, "Field new", 10d);
        expectedFarm.getFields().add(expectedField);

        Farm actualFarm = new Farm(farmId, "Farm 1");

        when(fieldRepository.findByFieldsId(fieldId)).thenReturn(Optional.of(actualFarm));

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.update(expectedField));
    }

    @Test
    @DisplayName("Should delete a field with passed id.")
    public void deleteById_validId_void() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");

        Farm actualFarm = new Farm(farmId, "Farm 1");
        actualFarm.getFields().add(new Field(fieldId, "Field 1", 10d));

        when(fieldRepository.findByFieldsId(fieldId)).thenReturn(Optional.of(actualFarm));

        // execution
        fieldService.deleteById(fieldId);

        // verification
        verify(fieldRepository, times(1)).save(eq(expectedFarm));
        verify(productionService, times(1)).deleteByFieldId(eq(fieldId));
    }

    @Test
    @DisplayName("Should throws exception when field not found on delete.")
    public void delete_field_notFoundException() {
        // scenario
        String farmId = ObjectId.get().toString();
        String fieldId = ObjectId.get().toString();

        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Field expectedField = new Field(fieldId, "Field new", 10d);
        expectedFarm.getFields().add(expectedField);

        Farm actualFarm = new Farm(farmId, "Farm 1");

        when(fieldRepository.findByFieldsId(fieldId)).thenReturn(Optional.of(actualFarm));

        // verification
        assertThrows(ObjectNotFoundException.class, () -> fieldService.deleteById(fieldId));
    }
*/
}

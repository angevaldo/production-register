package br.com.aegro.production.services;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import br.com.aegro.production.services.impl.FarmServiceImpl;
import org.bson.types.ObjectId;
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
public class FarmServiceTests {

    private FarmService farmService;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldService fieldService;

    @BeforeEach
    public void setUp() {
        this.farmService = new FarmServiceImpl(farmRepository, fieldService);
    }

    @Test
    @DisplayName("Should throws exception when invalid filter")
    public void updateAndFindById_invalidFilter_notFoundException() {
        // scenario
        Farm farm = new Farm(null, null);

        // verification
        assertThrows(ObjectNotFoundException.class, () -> farmService.update(farm));
        assertThrows(ObjectNotFoundException.class, () -> farmService.findById(null));
    }

    @Test
    @DisplayName("Should return all farms.")
    public void findAll_void_farmsList() {
        // scenario
        Farm farm_1 = new Farm(ObjectId.get().toString(), "Farm 1");
        Farm farm_2 = new Farm(ObjectId.get().toString(), "Farm 2");
        List<Farm> expectedFarms = new ArrayList<>(Arrays.asList(farm_1, farm_2));

        when(farmRepository.findAll()).thenReturn(expectedFarms);

        // execution
        List<Farm> actualFarms = farmService.findAll();

        // verification
        assertTrue(actualFarms.containsAll(expectedFarms));
    }

    @Test
    @DisplayName("Should return a farm with passed id.")
    public void findById_validId_farm() {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm 1");

        when(farmRepository.findById(farmId)).thenReturn(Optional.of(expectedFarm));

        // execution
        Farm actualFarm = farmService.findById(farmId);

        // verification
        assertEquals(expectedFarm.getId(), actualFarm.getId());
        assertEquals(expectedFarm.getName(), actualFarm.getName());
    }

    @Test
    @DisplayName("Should insert and return a new farm.")
    public void insert_farm_farm() {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm 1");
        Farm actualFarm = new Farm(null, "Farm 1");

        when(farmRepository.insert(actualFarm)).thenReturn(expectedFarm);

        // execution
        actualFarm = farmService.insert(actualFarm);

        // verification
        assertEquals(expectedFarm.getId(), actualFarm.getId());
        assertEquals(expectedFarm.getName(), actualFarm.getName());
    }

    @Test
    @DisplayName("Should update and return a farm.")
    public void update_farm_farm() {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm new");
        Farm actualFarm = new Farm(farmId, "Farm old");

        when(farmRepository.findById(farmId)).thenReturn(Optional.of(actualFarm));
        when(farmRepository.save(expectedFarm)).thenReturn(expectedFarm);

        // execution
        actualFarm = farmService.update(actualFarm);

        // verification
        assertEquals(expectedFarm.getId(), actualFarm.getId());
        assertEquals(expectedFarm.getName(), actualFarm.getName());
    }

    @Test
    @DisplayName("Should delete all farms.")
    public void deleteAll_void_void() {
        // execution
        farmService.deleteAll();

        // verification
        verify(farmRepository, times(1)).deleteAll();
        verify(fieldService, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should delete a farm with passed id.")
    public void deleteById_validId_void() {
        // scenario
        String farmId = ObjectId.get().toString();

        // execution
        farmService.deleteById(farmId);

        // verification
        verify(farmRepository, times(1)).deleteById(eq(farmId));
        verify(fieldService, times(1)).deleteByFarmId(eq(farmId));
    }

}

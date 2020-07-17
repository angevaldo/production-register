package br.com.aegro.production.resources;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.services.FarmService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FarmResource.class)
@ActiveProfiles(value = "test")
public class FarmResourceTest {

    static final String URI = "/api/farms";
    static final MediaType MEDIA = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objMapper;

    @MockBean
    FarmService farmService;

    @Test
    @DisplayName("Should return 200 and farms json when get without params.")
    void findAll_none_farmsList() throws Exception {
        // scenario
        Field expectedField = new Field(ObjectId.get().toString(), "Field 1", 10d);
        Farm expectedFarm_1 = new Farm(ObjectId.get().toString(), "Farm 1");
        expectedFarm_1.getFields().add(expectedField);
        Farm expectedFarm_2 = new Farm(ObjectId.get().toString(), "Farm 2");

        given(farmService.findAll()).willReturn(Arrays.asList(expectedFarm_1, expectedFarm_2));

        // execution
        ResultActions result = mvc.perform(get(URI).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedFarm_1.getId()))
                .andExpect(jsonPath("$[0].name").value(expectedFarm_1.getName()))
                .andExpect(jsonPath("$[0].fields[0].id").value(expectedField.getId()))
                .andExpect(jsonPath("$[0].fields[0].name").value(expectedField.getName()))
                .andExpect(jsonPath("$[0].fields[0].area").value(expectedField.getArea()))
                .andExpect(jsonPath("$[1].id").value(expectedFarm_2.getId()))
                .andExpect(jsonPath("$[1].name").value(expectedFarm_2.getName()));
    }

    @Test
    @DisplayName("Should return 200 and farm json when get with valid params.")
    void findById_validParams_farm() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm");
        given(farmService.findById(farmId)).willReturn(expectedFarm);

        // execution
        ResultActions result = mvc.perform(get(URI + "/{id}", farmId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedFarm.getId()))
                .andExpect(jsonPath("name").value(expectedFarm.getName()));
    }

    @Test
    @DisplayName("Should return 400 and errors messages when post with invalid params.")
    void create_invalidParams_error() throws Exception {
        // scenario
        String json_1 = objMapper.writeValueAsString(new Farm(null, null));
        String json_2 = objMapper.writeValueAsString(new Farm(null, "n"));

        // execution
        ResultActions result_1 = mvc.perform(post(URI).contentType(MEDIA).content(json_1));
        ResultActions result_2 = mvc.perform(post(URI).contentType(MEDIA).content(json_2));

        // verification
        result_1.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name cannot be empty."));
        result_2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name must be between 3 and 50 characters."));
    }

    @Test
    @DisplayName("Should return 201 and farm created when post with valid params.")
    void create_validParams_farm() throws Exception {
        // scenario
        Farm expectedFarm = new Farm(ObjectId.get().toString(), "Farm 1");
        Farm actualFarm = new Farm(null, "Farm 1");
        String json = objMapper.writeValueAsString(actualFarm);
        given(farmService.create(Mockito.any(Farm.class))).willReturn(expectedFarm);

        // execution
        ResultActions result = mvc.perform(post(URI).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(expectedFarm.getId()))
                .andExpect(jsonPath("name").value(expectedFarm.getName()));
    }

    @Test
    @DisplayName("Should return 40* and errors when put with invalid params.")
    void update_invalidParams_errors() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm");
        String json = objMapper.writeValueAsString(expectedFarm);
        given(farmService.update(expectedFarm)).willThrow(ObjectNotFoundException.class);

        // execution
        ResultActions result_1 = mvc.perform(put(URI + "/{id}", farmId).contentType(MEDIA).content(json));
        ResultActions result_2 = mvc.perform(put(URI + "/{id}", farmId).contentType(MEDIA).content("{}"));
        ResultActions result_3 = mvc.perform(put(URI + "/{id}", farmId).contentType(MEDIA).content("{\"name\":1}"));
        ResultActions result_4 = mvc.perform(put(URI + "/{id}", farmId).contentType(MEDIA));

        // verification
        result_1.andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Object not found"));
        result_2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Invalid argument"));
        result_3.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Invalid argument"));
        result_4.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Object not valid"));
    }

    @Test
    @DisplayName("Should return 200 and farm saved when put with valid params.")
    void update_validParams_farm() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Farm expectedFarm = new Farm(farmId, "Farm new");
        String json = objMapper.writeValueAsString(expectedFarm);
        given(farmService.update(expectedFarm)).willReturn(expectedFarm);

        // execution
        ResultActions result = mvc.perform(put(URI + "/{id}", farmId).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedFarm.getId()))
                .andExpect(jsonPath("name").value(expectedFarm.getName()));
    }

    @Test
    @DisplayName("Should return 200 when delete with valid params.")
    void delete_validParams_farm() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();

        // execution
        ResultActions result = mvc.perform(delete(URI + "/{id}", farmId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk());
        verify(farmService, times(1)).deleteById(farmId);
    }

}

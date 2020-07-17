package br.com.aegro.production.resources;

import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FieldResource.class)
@ActiveProfiles(value = "test")
public class FieldResourceTest {

    static final String URI = "/api/fields";
    static final MediaType MEDIA = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objMapper;

    @MockBean
    FieldService fieldService;

    @Test
    @DisplayName("Should return 200 and fields json when get with valid params.")
    void findByFarmId_validParams_fields() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Field expectField_1 = new Field(ObjectId.get().toString(), "Field 1", 10d);
        Field expectField_2 = new Field(ObjectId.get().toString(), "Field 2", 20d);
        given(fieldService.findByFarmId(farmId)).willReturn(Arrays.asList(expectField_1, expectField_2));

        // execution
        ResultActions result = mvc.perform(get(URI).param("farmId", farmId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(Arrays.asList(expectField_1, expectField_2))));
    }

    @Test
    @DisplayName("Should return 200 and field json when get with valid params.")
    void findById_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectField = new Field(fieldId, "Field", 15d);
        given(fieldService.findByFieldsId(fieldId)).willReturn(expectField);

        // execution
        ResultActions result = mvc.perform(get(URI + "/{id}", fieldId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(expectField)));
    }

    @Test
    @DisplayName("Should return 400 and errors messages when post with invalid params.")
    void create_invalidParams_error() throws Exception {
        // scenario
        String json_1 = objMapper.writeValueAsString(new Field(null, null, 10d));
        String json_2 = objMapper.writeValueAsString(new Field(null, "F", 10d));
        String json_3 = objMapper.writeValueAsString(new Field(null, "Field", 0));

        // execution
        ResultActions result_1 = mvc.perform(post(URI).contentType(MEDIA).content(json_1));
        ResultActions result_2 = mvc.perform(post(URI).contentType(MEDIA).content(json_2));
        ResultActions result_3 = mvc.perform(post(URI).contentType(MEDIA).content(json_3));

        // verification
        result_1.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name cannot be empty."));
        result_2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name must be between 3 and 50 characters."));
        result_3.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("The area must be greater than zero."));
    }

    @Test
    @DisplayName("Should return 201 and field created when post with valid params.")
    void create_validParams_field() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Field expectField = new Field(null, "Field 1", 15d);
        String json = objMapper.writeValueAsString(expectField);

        given(fieldService.create(expectField, farmId)).willReturn(expectField);

        // execution
        ResultActions result = mvc.perform(post(URI).param("farmId", farmId).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isCreated())
                .andExpect(content().json(objMapper.writeValueAsString(expectField)));
    }

    @Test
    @DisplayName("Should return 40* and errors when put with invalid params.")
    void update_invalidParams_errors() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectField = new Field(fieldId, "Field", 15d);
        String json = objMapper.writeValueAsString(expectField);
        given(fieldService.update(expectField)).willThrow(ObjectNotFoundException.class);

        // execution
        ResultActions result_1 = mvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content(json));
        ResultActions result_2 = mvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content("{}"));
        ResultActions result_3 = mvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content("{\"name\":1}"));
        ResultActions result_4 = mvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA));

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
    @DisplayName("Should return 200 and field saved when put with valid params.")
    void update_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectField = new Field(fieldId, "Field new", 15d);
        String json = objMapper.writeValueAsString(expectField);
        given(fieldService.update(expectField)).willReturn(expectField);

        // execution
        ResultActions result = mvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(expectField)));
    }

    @Test
    @DisplayName("Should return 200 when delete with valid params.")
    void delete_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();

        // execution
        ResultActions result = mvc.perform(delete(URI + "/{id}", fieldId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk());
        verify(fieldService, times(1)).deleteById(fieldId);
    }

}

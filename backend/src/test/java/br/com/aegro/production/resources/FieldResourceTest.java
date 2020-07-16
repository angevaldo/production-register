package br.com.aegro.production.resources;

import br.com.aegro.production.domain.entities.Farm;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FieldResource.class)
@ActiveProfiles(value = "test")
public class FieldResourceTest {

    static final String URI = "/api/fields";
    static final MediaType MEDIA = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FieldService fieldService;

    @Test
    @DisplayName("Should return 200 and fields json when get without params.")
    void findAll_none_fieldsList() throws Exception {
        // execution
        ResultActions result = mockMvc.perform(get(URI).contentType(MEDIA).accept(MEDIA));

        // verification
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 200 and field json when get with valid params.")
    void findById_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectedField = new Field(fieldId, "Field", 15d);
        given(fieldService.findByFieldsId(fieldId)).willReturn(expectedField);

        // execution
        ResultActions result = mockMvc.perform(get(URI + "/{id}", fieldId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedField.getId()))
                .andExpect(jsonPath("name").value(expectedField.getName()))
                .andExpect(jsonPath("area").value(expectedField.getArea()));
    }

    @Test
    @DisplayName("Should return 400 and errors messages when post with invalid params.")
    void create_invalidParams_error() throws Exception {
        // scenario
        String json1 = objectMapper.writeValueAsString(new Field(null, null, 10d));
        String json2 = objectMapper.writeValueAsString(new Field(null, "F", 10d));
        String json3 = objectMapper.writeValueAsString(new Field(null, "Field", 0));

        // execution
        ResultActions result1 = mockMvc.perform(post(URI).contentType(MEDIA).content(json1));
        ResultActions result2 = mockMvc.perform(post(URI).contentType(MEDIA).content(json2));
        ResultActions result3 = mockMvc.perform(post(URI).contentType(MEDIA).content(json3));

        // verification
        result1.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name cannot be empty."));
        result2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("The name must be between 3 and 50 characters."));
        result3.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("The area must be greater than zero."));
    }

    @Test
    @DisplayName("Should return 201 and field created when post with valid params.")
    void create_validParams_field() throws Exception {
        // scenario
        String farmId = ObjectId.get().toString();
        Field expectedField = new Field(null, "Field 1", 15d);
        String json = objectMapper.writeValueAsString(expectedField);

        given(fieldService.create(expectedField, farmId)).willReturn(expectedField);

        // execution
        ResultActions result = mockMvc.perform(post(URI).param("farmId", farmId).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(expectedField.getId()))
                .andExpect(jsonPath("name").value(expectedField.getName()))
                .andExpect(jsonPath("area").value(expectedField.getArea()));
    }

    @Test
    @DisplayName("Should return 40* and errors when put with invalid params.")
    void update_invalidParams_errors() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectedField = new Field(fieldId, "Field", 15d);
        String json = objectMapper.writeValueAsString(expectedField);
        given(fieldService.update(expectedField)).willThrow(ObjectNotFoundException.class);

        // execution
        ResultActions result1 = mockMvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content(json));
        ResultActions result2 = mockMvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content("{}"));
        ResultActions result3 = mockMvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content("{\"name\":1}"));
        ResultActions result4 = mockMvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA));

        // verification
        result1.andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Object not found"));
        result2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Invalid argument"));
        result3.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Invalid argument"));
        result4.andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("Object not valid"));
    }

    @Test
    @DisplayName("Should return 200 and field saved when put with valid params.")
    void update_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();
        Field expectedField = new Field(fieldId, "Field new", 15d);
        String json = objectMapper.writeValueAsString(expectedField);
        given(fieldService.update(expectedField)).willReturn(expectedField);

        // execution
        ResultActions result = mockMvc.perform(put(URI + "/{id}", fieldId).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedField.getId()))
                .andExpect(jsonPath("name").value(expectedField.getName()))
                .andExpect(jsonPath("area").value(expectedField.getArea()));
    }

    @Test
    @DisplayName("Should return 200 when delete with valid params.")
    void delete_validParams_field() throws Exception {
        // scenario
        String fieldId = ObjectId.get().toString();

        // execution
        ResultActions result = mockMvc.perform(delete(URI + "/{id}", fieldId).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk());
        verify(fieldService, times(1)).deleteById(fieldId);
    }

}

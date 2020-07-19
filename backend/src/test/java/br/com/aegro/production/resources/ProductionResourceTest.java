package br.com.aegro.production.resources;

import br.com.aegro.production.domain.dto.ProductionDTO;
import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.services.ProductionService;
import br.com.aegro.production.services.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductionResource.class)
@ActiveProfiles(value = "test")
public class ProductionResourceTest {

    static final String URI = "/api/productions";
    static final MediaType MEDIA = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @Autowired
    ModelMapper modMapper;

    @Autowired
    ObjectMapper objMapper;

    @MockBean
    ProductionService productionService;

    private Farm farm;
    private Field field_1;
    private Field field_2;
    private Production prod_1_1;
    private Production prod_1_2;
    private Production prod_2_1;
    private final List<Production> productions = new ArrayList<>();

    @BeforeEach
    public void setUp()  {
        this.farm = new Farm(ObjectId.get().toString(), "Farm");

        this.field_1 = new Field(ObjectId.get().toString(), "Field 1", 100d, farm);
        this.field_2 = new Field(ObjectId.get().toString(), "Field 2", 250d, farm);

        this.prod_1_1 = new Production(ObjectId.get().toString(), 50, farm, field_1);
        this.prod_1_2 = new Production(ObjectId.get().toString(), 25, farm, field_1);
        this.prod_2_1 = new Production(ObjectId.get().toString(), 100, farm, field_2);
        this.productions.addAll(Arrays.asList(prod_1_1, prod_1_2, prod_2_1));
    }

    @Test
    @DisplayName("Should return 200 and production json when get with valid params.")
    void findById_validParams_production() throws Exception {
        // scenario
        Production expectProd = prod_1_1;
        given(productionService.findById(prod_1_1.getId())).willReturn(expectProd);

        // execution
        ResultActions result = mvc.perform(get(URI + "/{id}", prod_1_1.getId()).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(modMapper.map(expectProd, ProductionDTO.class))));
    }

    @Test
    @DisplayName("Should return 200 and productions json when get with valid farm param.")
    void findByFarmId_validParams_productions() throws Exception {
        // scenario
        List<ProductionDTO> expectProdsDTO = productions.stream()
                .map(x -> modMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());
        given(productionService.findByFarmId(farm.getId())).willReturn(productions);

        // execution
        ResultActions result = mvc.perform(get(URI + "/findByFarmId")
                .param("farmId", farm.getId()).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(expectProdsDTO)));
    }

    @Test
    @DisplayName("Should return 200 and productions json when get with valid field param.")
    void findByFieldId_validParams_productions() throws Exception {
        // scenario
        List<Production> expectProd = Arrays.asList(prod_1_1, prod_1_2);
        List<ProductionDTO> expectProdsDTO = expectProd.stream()
                .map(x -> modMapper.map(x, ProductionDTO.class))
                .collect(Collectors.toList());
        given(productionService.findByFieldId(field_1.getId())).willReturn(expectProd);

        // execution
        ResultActions result = mvc.perform(get(URI + "/findByFieldId")
                .param("fieldId", field_1.getId()).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk())
                .andExpect(content().json(objMapper.writeValueAsString(expectProdsDTO)));
    }

    @Test
    @DisplayName("Should return 200 and productivity when get with valid farm params.")
    void productivityByFarmId_validParams_productivity() throws Exception {
        // scenario
        double expectProductivity = 1.15d; // productivity = (50/100) + (25/100) + (100/250) = 1,15
        given(productionService.getProductivityByFarmId(farm.getId())).willReturn(expectProductivity);

        // execution
        ResultActions result = mvc.perform(get(URI + "/productivityByFarmId")
                .param("farmId", farm.getId()).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk()).andExpect(content().json(String.valueOf(expectProductivity)));
    }

    @Test
    @DisplayName("Should return 200 and productivity when get with valid field params.")
    void productivityByFieldId_validParams_productivity() throws Exception {
        // scenario
        double expectProductivity = 75d; // productivity = (50/100) + (25/100) = 0,75
        given(productionService.getProductivityByFieldId(field_1.getId())).willReturn(expectProductivity);

        // execution
        ResultActions result = mvc.perform(get(URI + "/productivityByFieldId")
                .param("fieldId", field_1.getId()).contentType(MEDIA));

        // verification
        result.andExpect(status().isOk()).andExpect(content().json(String.valueOf(expectProductivity)));
    }

    @Test
    @DisplayName("Should return 400 and errors messages when post with invalid params.")
    void create_invalidParams_error() throws Exception {
        // scenario
        String json_1 = objMapper.writeValueAsString(ProductionDTO.builder().fieldId(prod_1_1.getId()).build());
        String json_2 = objMapper.writeValueAsString(ProductionDTO.builder().value(0).fieldId(prod_1_1.getId()).build());
        String json_3 = objMapper.writeValueAsString(modMapper.map(prod_1_2.getId(), Production.class));
        when(productionService.create(prod_1_2)).thenThrow(ObjectNotFoundException.class);

        // execution
        ResultActions result_1 = mvc.perform(post(URI).contentType(MEDIA).content(json_1));
        ResultActions result_2 = mvc.perform(post(URI).contentType(MEDIA).content(json_2));
        mvc.perform(post(URI).contentType(MEDIA).content(json_3));

        // verification
        result_1.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Area must be greater than zero."));
        result_2.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Area must be greater than zero."));
        assertThrows(ObjectNotFoundException.class, () -> productionService.create(prod_1_2));
    }

    @Test
    @DisplayName("Should return 201 and production created when post with valid params.")
    void create_validParams_production() throws Exception {
        // scenario
        Production expectProd = prod_1_2;
        Production actualProd = prod_1_1;
        String json = objMapper.writeValueAsString(modMapper.map(actualProd, ProductionDTO.class));

        given(productionService.create(actualProd)).willReturn(expectProd);

        // execution
        ResultActions result = mvc.perform(post(URI).contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 40* and errors when put with invalid params.")
    void update_invalidParams_errors() throws Exception {
        // scenario
        String prodId = ObjectId.get().toString();
        ProductionDTO expectProdDTO = ProductionDTO.builder().id(prodId).value(15d).fieldId("ID").build();
        String json = objMapper.writeValueAsString(expectProdDTO);

        given(productionService.update(modMapper.map(expectProdDTO, Production.class)))
                .willThrow(ObjectNotFoundException.class);

        // execution
        ResultActions result_1 = mvc.perform(put(URI + "/{id}", prodId).contentType(MEDIA).content(json));
        ResultActions result_2 = mvc.perform(put(URI + "/{id}", prodId).contentType(MEDIA).content("{}"));
        ResultActions result_3 = mvc.perform(put(URI + "/{id}", prodId).contentType(MEDIA).content("{\"area\":\"a\"}"));
        ResultActions result_4 = mvc.perform(put(URI + "/{id}", prodId).contentType(MEDIA));

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
    @DisplayName("Should return 204 and production saved when put with valid params.")
    void update_validParams_production() throws Exception {
        // scenario
        Production expectProd = prod_2_1;
        ProductionDTO expectProdDTO = modMapper.map(expectProd, ProductionDTO.class);
        Production actualProd = new Production(prod_2_1.getId(), 23d, farm, field_2);
        String json = objMapper.writeValueAsString(expectProdDTO);

        given(productionService.update(actualProd)).willReturn(expectProd);

        // execution
        ResultActions result = mvc.perform(put(URI + "/{id}", expectProd.getId())
                .contentType(MEDIA).content(json));

        // verification
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 204 when delete with valid params.")
    void deleteById_validParams_production() throws Exception {
        // scenario
        String prodId = ObjectId.get().toString();

        // execution
        ResultActions result = mvc.perform(delete(URI + "/{id}", prodId).contentType(MEDIA));

        // verification
        result.andExpect(status().isNoContent());
        verify(productionService, times(1)).deleteById(prodId);
    }
}

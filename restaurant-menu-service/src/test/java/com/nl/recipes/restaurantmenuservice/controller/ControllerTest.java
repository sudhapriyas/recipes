package com.nl.recipes.restaurantmenuservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.recipes.restaurantmenuservice.model.*;
import com.nl.recipes.restaurantmenuservice.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class ControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).setControllerAdvice().build();
    }

    @Test
    void createRecipeSuccess() throws Exception {
        RecipeVariant recipeVariant = createRecipeVO();
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest;
        jsonRequest = mapper.writeValueAsString(recipeVariant);

        when(recipeService.saveRecipe(recipeVariant)).thenReturn(recipeVariant);
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void modifyRecipeSuccess() throws Exception {
        RecipeVariant recipeVariant = createRecipeVO();
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest;
        jsonRequest = mapper.writeValueAsString(recipeVariant);

        when(recipeService.modifyExistingRecipe(recipeVariant)).thenReturn(recipeVariant);
        mockMvc.perform(MockMvcRequestBuilders.put("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRecipeSuccess() throws Exception {
        doNothing().when(recipeService).deleteRecipe(101);
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipe/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRecipeSuccess() throws Exception {
        RecipeVariant recipeVariant = createRecipeVO();

        when(recipeService.getRecipe(101)).thenReturn(recipeVariant);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRecipes() throws Exception {
        when(recipeService.getAllRecipes()).thenReturn(List.of(createRecipeVO()));
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void filterRecipes() throws Exception {
        RecipeFilterCondition recipeFilterCriteria = createFilterCriteria();
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest;
        jsonRequest = mapper.writeValueAsString(recipeFilterCriteria);
        when(recipeService.filterRecipes(recipeFilterCriteria)).thenReturn(List.of(createRecipeVO()));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    private RecipeFilterCondition createFilterCriteria() {
        RecipeFilterCondition recipeFilterCriteria = new RecipeFilterCondition();
        recipeFilterCriteria.setInstructions("oven");
        recipeFilterCriteria.setType("VEG");
        recipeFilterCriteria.setServingCapacity(2);
        recipeFilterCriteria.setIngredientSearchVO(createIngredientSearchVO());
        return recipeFilterCriteria;
    }

    private IngredientSearchVariant createIngredientSearchVO() {
        IngredientSearchVariant ingredientSearchVO = new IngredientSearchVariant();
        ingredientSearchVO.setInclusion(Inclusion.INCLUDE);
        ingredientSearchVO.setIngredientVOList(createIngredientsList());
        return ingredientSearchVO;
    }

    private RecipeVariant createRecipeVO() {
        return RecipeVariant
                .builder()
                .id(101)
                .name("dish1")
                .instructions("oven")
                .type("VEG")
                .ingredientsList(createIngredientsList())
                .servingCapacity(2)
                .build();
    }

    private List<IngredientVariant> createIngredientsList() {
        return List.of(new IngredientVariant("tomato"), new IngredientVariant("potato"));
    }
}
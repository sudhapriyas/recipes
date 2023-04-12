package com.nl.recipes.restaurantmenuservice.service;

import com.nl.recipes.restaurantmenuservice.errorHandling.BadRequestException;
import com.nl.recipes.restaurantmenuservice.errorHandling.RecipesNotFoundException;
import com.nl.recipes.restaurantmenuservice.model.*;
import com.nl.recipes.restaurantmenuservice.repository.RecipeRepository;
import com.nl.recipes.restaurantmenuservice.util.MappingRecipe;
import com.nl.recipes.restaurantmenuservice.util.SpecificationRecipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(SpringExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void saveRecipeToRepositorySuccess() {
        RecipeVariant recipeVariant = createRecipeVariant();
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVariant());
        when(recipeRepository.save(recipeEntity)).thenReturn(recipeEntity);
        RecipeVariant result = recipeService.saveRecipe(recipeVariant);
        assertNotNull(result);
        assertEquals(101, result.getId());
    }

    @Test
    void saveRecipeToRepositoryBadRequest() {
        RecipeVariant recipeVariant = createRecipeVariant();
        recipeVariant.setServingCapacity(null);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> {
            RecipeVariant result = recipeService.saveRecipe(recipeVariant);
        });
        assertEquals("400 BAD_REQUEST \"BAD_REQUEST_MSG\"", badRequestException.getMessage());
    }

   /* @Test
    void filterRecipesWithResults() {
        RecipeFilterCondition filterCriteria = createFilterCondtion();
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findAll(any(SpecificationRecipe.class))).thenReturn(recipeEntities);
        List<RecipeVariant> recipeVOS = recipeService.filterRecipes(filterCriteria);
        assertEquals(3, recipeVOS.size());
    }*/

    @Test
    void filterRecipesWithEmptyContent() {
        RecipeFilterCondition filterCriteria = new RecipeFilterCondition();
        filterCriteria.getIngredientSearchVO().getIngredientVOList().get(0).setName("Cabbage");
        List<RecipeEntity> recipeEntities = new ArrayList<>();
        when(recipeRepository.findAll(any(SpecificationRecipe.class))).thenReturn(recipeEntities);
        RecipesNotFoundException noRecipesFoundException = Assertions.assertThrows(RecipesNotFoundException.class,
                () -> {
                    List<RecipeVariant> RecipeVariantS = recipeService.filterRecipes(filterCriteria);
                });
        assertEquals("204 NO_CONTENT \"RECIPES_NOT_FOUND_FILTER_MSG\"",
                noRecipesFoundException.getMessage());
    }

    @Test
    void getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeEntityList();
        when(recipeRepository.findAll()).thenReturn(recipeEntities);
        List<RecipeVariant> allRecipes = recipeService.getAllRecipes();
        assertEquals(3, allRecipes.size());
    }

    @Test
    void getRecipe() {
        Integer request = 101;
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVariant());
        when(recipeRepository.findById(request)).thenReturn(java.util.Optional.of(recipeEntity));
        RecipeVariant recipe = recipeService.getRecipe(101);
        assertEquals(101, recipe.getId());
    }

    @Test
    void modifyExistingRecipe() {
        RecipeVariant request = createRecipeVariant();
        request.setName("dish3");
        RecipeEntity existingRecipeEntity = createRecipeEntity(createRecipeVariant());
        RecipeEntity newRecipeEntity = createRecipeEntity(request);
        when(recipeRepository.findById(request.getId())).thenReturn(java.util.Optional.of(existingRecipeEntity));
        when(recipeRepository.save(newRecipeEntity)).thenReturn(newRecipeEntity);
        RecipeVariant recipe = recipeService.modifyExistingRecipe(request);
        assertEquals(101, recipe.getId());
        assertEquals("dish3", recipe.getName());
    }

    @Test
    void deleteRecipe() {
        Integer request = 101;
        RecipeEntity recipeEntity = createRecipeEntity(createRecipeVariant());
        when(recipeRepository.findById(request)).thenReturn(java.util.Optional.of(recipeEntity));
        recipeService.deleteRecipe(request);
        verify(recipeRepository, times(1)).deleteById(101);
    }

    private RecipeVariant createRecipeVariant() {
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

    private RecipeEntity createRecipeEntity(RecipeVariant RecipeVariant) {
        return MappingRecipe.mapToRecipeEntity(RecipeVariant);
    }

   /* private RecipeFilterCondition createFilterCriteria() {
        RecipeFilterCondition recipeFilterCriteria = new RecipeFilterCondition();
        recipeFilterCriteria.setInstructions("oven");
        recipeFilterCriteria.setType("VEG");
        recipeFilterCriteria.setServingCapacity(2);
        recipeFilterCriteria.getIngredientSearchVO(createIngredientSearchVariant());
        return recipeFilterCriteria;
    }*/
    private IngredientSearchVariant createIngredientSearchVariant() {
        IngredientSearchVariant IngredientSearchVariant = new IngredientSearchVariant();
        IngredientSearchVariant.setInclusion(Inclusion.INCLUDE);
        IngredientSearchVariant.setIngredientVOList(createIngredientsList());
        return IngredientSearchVariant;
    }

    private List<RecipeEntity> recipeEntityList() {
        RecipeVariant RecipeVariant1 = createRecipeVariant();

        RecipeVariant RecipeVariant2 = createRecipeVariant();
        RecipeVariant2.setId(102);
        RecipeVariant2.setInstructions("normal");
        RecipeVariant2.setServingCapacity(5);
        RecipeVariant2.setType("NON-VEG");
        RecipeVariant2.getIngredientsList().get(0).setName("chicken");

        RecipeVariant RecipeVariant3 = createRecipeVariant();
        RecipeVariant3.setId(103);
        RecipeVariant3.setServingCapacity(2);
        RecipeVariant3.setType("VEG");
        RecipeVariant3.getIngredientsList().get(0).setName("tomato");

        return List.of(createRecipeEntity(RecipeVariant1), createRecipeEntity(RecipeVariant2), createRecipeEntity(RecipeVariant3));
    }

}

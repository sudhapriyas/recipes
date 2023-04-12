package com.nl.recipes.restaurantmenuservice.repository;

import com.nl.recipes.restaurantmenuservice.model.IngredientsEntity;
import com.nl.recipes.restaurantmenuservice.model.RecipeEntity;
import com.nl.recipes.restaurantmenuservice.util.SpecificationRecipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


    @ExtendWith(SpringExtension.class)
    @DataJpaTest
    class RecipeRepositoryTest {

        @Autowired
        private RecipeRepository recipeRepository;

        @Test
        void findAllRecipesWithMatchingRecords() {
            List<RecipeEntity> result = recipeRepository
                    .findAll(SpecificationRecipe.builder().type("VEG").servCapacity(3).instructions("oven").build());
            assertEquals(2, result.size());
        }

        @Test
        void findAllRecipesWithMatchingRecordsIncludeIngredients() {
            IngredientsEntity ingredient1 = new IngredientsEntity();
            ingredient1.setName("tomato");
            IngredientsEntity ingredient2 = new IngredientsEntity();
            ingredient2.setName("potato");
            List<RecipeEntity> result = recipeRepository
                    .findAll(SpecificationRecipe.builder()
                            .type("VEG")
                            .servCapacity(3)
                            .instructions("oven")
                            .ingredientsInclude(List.of(ingredient1, ingredient2))
                            .build());
            assertEquals(1, result.size());
        }

        @Test
        void findAllRecipesWithMatchingRecordsExcludeIngredients() {
            IngredientsEntity ingreTomato = new IngredientsEntity();
            ingreTomato.setName("tomato");
            IngredientsEntity ingregPotato = new IngredientsEntity();
            ingregPotato.setName("potato");
            List<RecipeEntity> result = recipeRepository
                    .findAll(SpecificationRecipe.builder()
                            .type("VEG")
                            .servCapacity(3)
                            .instructions("oven")
                            .ingredientsExclude(List.of(ingregPotato))
                            .build());
            assertEquals(1, result.size());
        }

        @Test
        void findAllRecipeEmptyRecords() {
            List<RecipeEntity> result = recipeRepository
                    .findAll(SpecificationRecipe.builder().type("NON-VEG").servCapacity(4).instructions("oven").build());
            assertEquals(0, result.size());
        }
    }
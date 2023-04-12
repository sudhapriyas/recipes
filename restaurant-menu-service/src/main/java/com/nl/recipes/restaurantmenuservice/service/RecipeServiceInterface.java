package com.nl.recipes.restaurantmenuservice.service;

import com.nl.recipes.restaurantmenuservice.model.RecipeFilterCondition;
import com.nl.recipes.restaurantmenuservice.model.RecipeVariant;

import java.util.List;

public interface RecipeServiceInterface {
    RecipeVariant saveRecipe(RecipeVariant recipeVO);

    List<RecipeVariant> filterRecipes(RecipeFilterCondition recipeFilterCondition);

    void deleteRecipe(Integer id);

    RecipeVariant modifyExistingRecipe(RecipeVariant recipeVO);

    List<RecipeVariant> getAllRecipes();

    RecipeVariant getRecipe(Integer id);
}
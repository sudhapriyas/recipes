package com.nl.recipes.restaurantmenuservice.controller;

import com.nl.recipes.restaurantmenuservice.model.RecipeFilterCondition;
import com.nl.recipes.restaurantmenuservice.model.RecipeVariant;
import com.nl.recipes.restaurantmenuservice.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * saves new recipe
     *
     */
    @PostMapping("/recipe")
    public ResponseEntity<RecipeVariant> createRecipe(@RequestBody RecipeVariant recipeVariant) {
        log.info("create new recipe: {}", recipeVariant);
        RecipeVariant savedRecipeVariant = recipeService.saveRecipe(recipeVariant);
        log.info("successfully saved new recipe: {}", savedRecipeVariant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipeVariant);
    }

    /**
     * fetches the list of recipes based on the search
     *  criteria
     */
    @PostMapping("/recipes/filter")
    public ResponseEntity<List<RecipeVariant>> filterRecipes(@RequestBody RecipeFilterCondition recipeFilterCriteria) {
        log.info("filter the recipes with the criteria: {}", recipeFilterCriteria);
        List<RecipeVariant> recipeList = recipeService.filterRecipes(recipeFilterCriteria);
        log.info("filtered recipes: {} ", recipeList);
        return ResponseEntity.status(HttpStatus.OK).body(recipeList);
    }

    /**
     * fetch Recipe by ID
     *
     */
    @GetMapping("/recipe/{id}")
    public ResponseEntity<RecipeVariant> getRecipe(@PathVariable Integer id) {
        log.info("retrieve recipe for ID: {}", id);
        RecipeVariant recipeVariant = recipeService.getRecipe(id);
        log.info("successfully retrieved recipe: {}", recipeVariant);
        return ResponseEntity.status(HttpStatus.OK).body(recipeVariant);
    }

    /**
     * fetch all recipes
     *
     */
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeVariant>> getAllRecipes() {
        log.info("retrieve all the recipes");
        List<RecipeVariant> allRecipes = recipeService.getAllRecipes();
        log.info("number of recipes retrieved: {}", allRecipes.size());
        return ResponseEntity.status(HttpStatus.OK).body(allRecipes);
    }

    /**
     * update existing recipe
     *
     */
    @PutMapping("/recipe")
    public ResponseEntity<RecipeVariant> modifyRecipe(@RequestBody RecipeVariant recipeVO) {
        log.info("modify existing recipe: {}", recipeVO);
        RecipeVariant modifiedRecipe = recipeService.modifyExistingRecipe(recipeVO);
        log.info("successfully modified recipe: {}", modifiedRecipe);
        return ResponseEntity.status(HttpStatus.OK).body(modifiedRecipe);
    }

    /**
     * delete recipe by id
     *
     */
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
        log.info("delete existing recipe for ID: {}", id);
        recipeService.deleteRecipe(id);
        log.info("successfully deleted recipe with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body("recipe deleted from db");
    }

}
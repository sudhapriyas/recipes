package com.nl.recipes.restaurantmenuservice.service;

import com.nl.recipes.restaurantmenuservice.errorHandling.*;
import com.nl.recipes.restaurantmenuservice.model.*;
import com.nl.recipes.restaurantmenuservice.repository.RecipeRepository;
import com.nl.recipes.restaurantmenuservice.util.MappingRecipe;
import com.nl.recipes.restaurantmenuservice.util.RecipeUtility;
import com.nl.recipes.restaurantmenuservice.util.SpecificationRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeService implements RecipeServiceInterface {

    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * save given new recipe in database
     *
     */
    @Override
    public RecipeVariant saveRecipe(RecipeVariant recipeVariant) {
        if (!RecipeUtility.checkRecipeValidity(recipeVariant)) {
            log.error("provided recipe instance is not valid");
            throw new BadRequestException(ErrorText.BAD_REQUEST_MSG.name());
        }
        if (isRecipeFound(recipeVariant.getId())) {
            log.error("Provided recipe is not found in database");
            throw new RecipeAlreadyExistException(ErrorText.RECIPE_ALREADY_EXIST_MSG.name());
        }

        RecipeEntity recipeEntity = MappingRecipe.mapToRecipeEntity(recipeVariant);
        RecipeVariant savedRecipe = MappingRecipe.mapToRecipeVariant(recipeRepository.save(recipeEntity));

        if (savedRecipe == null) {
            log.error("Service failed to create recipe in DB");
            throw new RecipeNotCreatedException(ErrorText.INTERNAL_SERVER_ERR_MSG.name());
        }
        return savedRecipe;

    }

    /**
     * retrive all recipes
     *
     */
    @Override
    public List<RecipeVariant> getAllRecipes() {
        List<RecipeEntity> retrievedRecipes = recipeRepository.findAll();
        if (CollectionUtils.isEmpty(retrievedRecipes)) {
            log.info("Recipes not found in database");
            throw new RecipesNotFoundException(ErrorText.RECIPES_NOT_FOUND_MSG.name());
        }
        return retrievedRecipes.stream().map(MappingRecipe::mapToRecipeVariant).collect(Collectors.toList());
    }

    /**
     * filter recipes based on the filter criteria
     *
     */
    @Override
    public List<RecipeVariant> filterRecipes(RecipeFilterCondition filterCriteria) {
        List<RecipeEntity> filteredRecipesFromDb = recipeRepository.findAll(SpecificationRecipe.builder()
                .type(filterCriteria.getType())
                .servCapacity(filterCriteria.getServingCapacity())
                .instructions(filterCriteria.getInstructions())
                .ingredientsInclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.INCLUDE))
                .ingredientsExclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.EXCLUDE))
                .build());

        if (CollectionUtils.isEmpty(filteredRecipesFromDb)) {
            log.info("Recipes not found for the filtered criteria");
            throw new RecipesNotFoundException(ErrorText.RECIPES_NOT_FOUND_FILTER_MSG.name());
        }
        return filteredRecipesFromDb.stream().map(MappingRecipe::mapToRecipeVariant).collect(Collectors.toList());
    }

    /**
     * ingredients search and inclusion
     */
    private List<IngredientsEntity> getIngredientsList(IngredientSearchVariant ingredientSearchVO, Inclusion inclusion) {

        if (ingredientSearchVO == null)
            return null;
        if (ingredientSearchVO.getInclusion() == inclusion)
            return MappingRecipe.mapToIngredientEntityList(ingredientSearchVO.getIngredientVOList());
        return null;
    }

    /**
     * fetch all recipes
     *
     */
    @Override
    public RecipeVariant getRecipe(Integer id) {
        Optional<RecipeEntity> recipeEntityOptional = recipeRepository.findById(id);
        if (!recipeEntityOptional.isPresent())
            throw new NoSuchRecipeException(ErrorText.RECIPES_NOT_FOUND_MSG.name());
        return MappingRecipe.mapToRecipeVariant(recipeEntityOptional.get());
    }


    /**
     * Updating an existing recipe
     *
     */
    @Override
    public RecipeVariant modifyExistingRecipe(RecipeVariant recipeVO) {
        if (!RecipeUtility.checkRecipeValidity(recipeVO)) {
            log.error("unable to update, provided recipe instance is not valid");
            throw new BadRequestException(ErrorText.BAD_REQUEST_MSG.name());
        }
        if (!isRecipeFound(recipeVO.getId())) {
            log.error("unable to update, provided recipe is not found");
            throw new NoSuchRecipeException(ErrorText.NO_SUCH_RECIPE_FOUND_MSG.name());
        }

        RecipeEntity recipeEntity = MappingRecipe.mapToRecipeEntity(recipeVO);
        RecipeVariant modifiedRecipe = MappingRecipe.mapToRecipeVariant(recipeRepository.save(recipeEntity));

        if (modifiedRecipe == null) {
            log.error("Service failed to modify recipe");
            throw new RecipeNotCreatedException(ErrorText.INTERNAL_SERVER_ERR_MSG.name());
        }
        return modifiedRecipe;
    }

    /**
     * delete requested recipe based on its id
     *
     */
    @Override
    public void deleteRecipe(Integer id) {
        if (!isRecipeFound(id)) {
            log.error("unable to delete, provided recipe is not found");
            throw new NoSuchRecipeException(ErrorText.NO_SUCH_RECIPE_FOUND_MSG.name());
        }
        recipeRepository.deleteById(id);
    }

    /**
     * check if the Recipe found in Database
     *
     */
    private boolean isRecipeFound(Integer id) {
        return recipeRepository.findById(id).isPresent();
    }

}
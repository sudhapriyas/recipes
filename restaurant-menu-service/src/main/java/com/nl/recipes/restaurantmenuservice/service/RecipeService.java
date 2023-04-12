package com.nl.recipes.restaurantmenuservice.service;

import com.nl.recipes.restaurantmenuservice.model.*;
import com.nl.recipes.restaurantmenuservice.repository.RecipeRepository;
import com.nl.recipes.restaurantmenuservice.util.MappingRecipe;
import com.nl.recipes.restaurantmenuservice.util.RecipeUtility;
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
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (isRecipeFound(recipeVariant.getId())) {
            log.error("Provided recipe is not found in database");
            throw new RecipeExistsException(ErrorMessages.RECIPE_ALREADY_EXIST_MSG.name());
        }

        RecipeEntity recipeEntity = MappingRecipe.mapToRecipeEntity(recipeVariant);
        RecipeVariant savedRecipe = MappingRecipe.mapToRecipeVariant(recipeRepository.save(recipeEntity));

        if (savedRecipe == null) {
            log.error("Service failed to create recipe in DB");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return savedRecipe;

    }

    /**
     * retrive all recipes
     *
     * @return
     */
    @Override
    public List<RecipeVariant> getAllRecipes() {
        List<RecipeEntity> retrievedRecipes = recipeRepository.findAll();
        if (CollectionUtils.isEmpty(retrievedRecipes)) {
            log.info("Recipes not found in database");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.name());
        }
        return retrievedRecipes.stream().map(MappingRecipe::mapToRecipeVariant).collect(Collectors.toList());
    }

    /**
     * filter recipes based on the filter criteria
     *
     * @param filterCriteria
     * @return
     */
    @Override
    public List<RecipeVO> filterRecipes(RecipeFilterCriteria filterCriteria) {
        List<RecipeEntity> filteredRecipesFromDb = recipeRepository.findAll(RecipeSpecification.builder()
                .type(filterCriteria.getType())
                .servCapacity(filterCriteria.getServingCapacity())
                .instructions(filterCriteria.getInstructions())
                .ingredientsInclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.INCLUDE))
                .ingredientsExclude(getIngredientsList(filterCriteria.getIngredientSearchVO(), Inclusion.EXCLUDE))
                .build());

        if (CollectionUtils.isEmpty(filteredRecipesFromDb)) {
            log.info("Recipes not found for the filtered criteria");
            throw new NoRecipesFoundException(ErrorMessages.RECIPES_NOT_FOUND_FILTER_MSG.name());
        }
        return filteredRecipesFromDb.stream().map(RecipeMapperUtil::mapToRecipeVO).collect(Collectors.toList());
    }

    /**
     * @param ingredientSearchVO
     * @param inclusion
     * @return
     */
    private List<IngredientsEntity> getIngredientsList(IngredientSearchVariant ingredientSearchVO, Inclusion inclusion) {

        if (ingredientSearchVO == null)
            return null;
        if (ingredientSearchVO.getInclusion() == inclusion)
            return MappingRecipe.mapToIngredientEntityList(ingredientSearchVO.getIngredientVOList());
        return null;
    }

    /**
     * retrieve all recipes
     *
     * @param id
     * @return
     */
    @Override
    public RecipeVariant getRecipe(Integer id) {
        Optional<RecipeEntity> recipeEntityOptional = recipeRepository.findById(id);
        if (!recipeEntityOptional.isPresent())
            throw new NoSuchRecipeFoundException(ErrorMessages.RECIPES_NOT_FOUND_MSG.name());
        return MappingRecipe.mapToRecipeVariant(recipeEntityOptional.get());
    }


    /**
     * modify an existing recipe
     *
     * @param recipeVO
     * @return
     */
    @Override
    public RecipeVariant modifyExistingRecipe(RecipeVariant recipeVO) {
        if (!RecipeUtility.checkRecipeValidity(recipeVO)) {
            log.error("unable to update, provided recipe instance is not valid");
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_MSG.name());
        }
        if (!isRecipeFound(recipeVO.getId())) {
            log.error("unable to update, provided recipe is not found");
            throw new NoSuchRecipeFoundException(ErrorMessages.NO_SUCH_RECIPE_FOUND_MSG.name());
        }

        RecipeEntity recipeEntity = MappingRecipe.mapToRecipeEntity(recipeVO);
        RecipeVariant modifiedRecipe = MappingRecipe.mapToRecipeVariant(recipeRepository.save(recipeEntity));

        if (modifiedRecipe == null) {
            log.error("Service failed to modify recipe");
            throw new RecipeNotCreatedException(ErrorMessages.INTERNAL_SERVER_ERR_MSG.name());
        }
        return modifiedRecipe;
    }

    /**
     * delete requested recipe based on its id
     *
     * @param id
     */
    @Override
    public void deleteRecipe(Integer id) {
        if (!isRecipeFound(id)) {
            log.error("unable to delete, provided recipe is not found");
            throw new NoSuchRecipeFoundException(ErrorMessages.NO_SUCH_RECIPE_FOUND_MSG.name());
        }
        recipeRepository.deleteById(id);
    }

    /**
     * check is the Recipe found in Database
     *
     * @param id
     * @return
     */
    private boolean isRecipeFound(Integer id) {
        return recipeRepository.findById(id).isPresent();
    }

}
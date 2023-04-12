package com.nl.recipes.restaurantmenuservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.recipes.restaurantmenuservice.model.IngredientVariant;
import com.nl.recipes.restaurantmenuservice.model.IngredientsEntity;
import com.nl.recipes.restaurantmenuservice.model.RecipeEntity;
import com.nl.recipes.restaurantmenuservice.model.RecipeVariant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

    @Slf4j
    @Component
    public class MappingRecipe {

       /**
         * Map Variation order to entity
         */
        public static RecipeEntity mapToRecipeEntity(RecipeVariant recipeVariant) {
            RecipeEntity recipeEntity = new RecipeEntity();
            recipeEntity.setId(recipeVariant.getId());
            recipeEntity.setName(recipeVariant.getName());
            recipeEntity.setType(recipeVariant.getType());
            recipeEntity.setServingCapacity(recipeVariant.getServingCapacity());
            recipeEntity.setIngredients(mapToIngredientEntityList(recipeVariant.getIngredientsList()));
            recipeEntity.setInstructions(recipeVariant.getInstructions());
            return recipeEntity;
        }

        /**
         * map Entity to Variation order
         *
         */
        public static RecipeVariant mapToRecipeVariant(RecipeEntity recipeEntity) {
            RecipeVariant recipeVariant = new RecipeVariant();
            recipeVariant.setId(recipeEntity.getId());
            recipeVariant.setName(recipeEntity.getName());
            recipeVariant.setType(recipeEntity.getType());
            recipeVariant.setServingCapacity(recipeEntity.getServingCapacity());
            recipeVariant.setIngredientsList(mapToIngredientVOList(recipeEntity.getIngredients()));
            recipeVariant.setInstructions(recipeEntity.getInstructions());

            return recipeVariant;
        }

        /**
         * map ingredient VO to entity
         *
         */
        public static IngredientVariant mapToIngredientVO(IngredientsEntity ingredientEntity) {
            IngredientVariant ingredientVO = new IngredientVariant();
            ingredientVO.setName(ingredientEntity.getName());
            return ingredientVO;
        }

        /**
         * map ingredient VO to entity
         *
         */
        public static IngredientsEntity mapToIngredientEntity(IngredientVariant ingredientVO) {
            IngredientsEntity ingredientEntity = new IngredientsEntity();
            ingredientEntity.setName(ingredientVO.getName());
            return ingredientEntity;
        }

        /**
         * map ingredient VO list to entity list
         *
         */
        public static List<IngredientsEntity> mapToIngredientEntityList(List<IngredientVariant> ingredientVOS) {
            return ingredientVOS.stream().map(MappingRecipe::mapToIngredientEntity).collect(Collectors.toList());
        }

        /**
         * map ingredient Variation order list to entity list
         *
         */
        public static List<IngredientVariant> mapToIngredientVOList(List<IngredientsEntity> ingredientEntities) {
            return ingredientEntities.stream().map(MappingRecipe::mapToIngredientVO).collect(Collectors.toList());
        }

        /**
         * converts Ingredients List to Json string
         *
         */
        public static String convertToJSONString(List<IngredientVariant> ingredientVOList) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(ingredientVOList);
            } catch (Exception e) {
                log.error("error while converting List to JSON String");
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return jsonString;
        }

        /**
         * transform string to Ingredients List
         *
         */
        public static List<IngredientVariant> convertJSONStringToIngredientVOList(String jsonString) {
            ObjectMapper mapper = new ObjectMapper();
            List<IngredientVariant> ingredientVOList = null;
            try {
                ingredientVOList = Arrays.asList(mapper.readValue(jsonString, IngredientVariant[].class));
            } catch (Exception e) {
                log.error("error while converting JSON String to Ingredients List");
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return ingredientVOList;
        }



    }

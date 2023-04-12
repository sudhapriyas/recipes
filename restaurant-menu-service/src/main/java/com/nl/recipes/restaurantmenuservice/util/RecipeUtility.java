package com.nl.recipes.restaurantmenuservice.util;

import com.nl.recipes.restaurantmenuservice.model.RecipeVariant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility for Recipe validation & conversion
 */
@Slf4j
@Component
public class RecipeUtility {

    /**
     * verifying the mandatory attributes
     *
     */
    public static Boolean checkRecipeValidity(RecipeVariant recipeVO) {
        if (recipeVO == null) {
            log.error("recipe is null");
            return false;
        }
        if (recipeVO.getId() == null || recipeVO.getName() == null ||
                recipeVO.getType() == null || recipeVO.getServingCapacity() == null) {
            log.error("mandatory fields not filled");
            return false;
        }
        return true;
    }

}

package com.nl.recipes.restaurantmenuservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSearchVariant {

    private List<IngredientVariant> ingredientVOList;
    private Inclusion inclusion;
}
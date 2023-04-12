package com.nl.recipes.restaurantmenuservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IngredientVariant {
    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Include
    private String getNameIgnoreCase() {
        return name != null ? name.toLowerCase() : null;
    }

}
package com.nl.recipes.restaurantmenuservice.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeNotCreatedException extends ResponseStatusException {

    public RecipeNotCreatedException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    }

}

package com.nl.recipes.restaurantmenuservice.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipeAlreadyExistException extends ResponseStatusException {
    public RecipeAlreadyExistException(String message) {
        super(HttpStatus.CONFLICT,message);
    }
}

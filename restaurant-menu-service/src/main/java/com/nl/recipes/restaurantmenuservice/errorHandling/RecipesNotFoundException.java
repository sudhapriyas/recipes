package com.nl.recipes.restaurantmenuservice.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecipesNotFoundException extends ResponseStatusException {

    public RecipesNotFoundException(String message) {
        super(HttpStatus.NO_CONTENT, message);
    }
}
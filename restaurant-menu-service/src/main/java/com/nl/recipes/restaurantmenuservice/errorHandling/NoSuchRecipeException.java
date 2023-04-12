package com.nl.recipes.restaurantmenuservice.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchRecipeException extends ResponseStatusException {

    public NoSuchRecipeException(String message) {
        super(HttpStatus.NO_CONTENT, message);
    }
}

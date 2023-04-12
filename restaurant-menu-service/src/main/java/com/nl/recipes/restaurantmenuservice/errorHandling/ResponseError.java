package com.nl.recipes.restaurantmenuservice.errorHandling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseError {
    private final int status;
    private final String message;
    private final LocalDateTime dateTime;
}

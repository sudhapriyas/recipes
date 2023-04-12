package com.nl.recipes.restaurantmenuservice.errorHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


    @ControllerAdvice
    @Slf4j
    public class RecipeControllerSuggestion  extends ResponseEntityExceptionHandler {

        /**
         * error handler for NoRecipes found
         *
         * @param ex
         * @return
         */
        @ExceptionHandler(RecipesNotFoundException.class)
        public ResponseEntity<Object> handleRecipesNotFoundException(RecipesNotFoundException ex) {
            return buildErrorResponse(ex, ex.getMessage(), HttpStatus.NO_CONTENT);
        }

        /**
         * handler for data access error
         *
         * @param ex
         * @return
         */
        @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
        public ResponseEntity<Object> handleDataAccessException(InvalidDataAccessResourceUsageException ex) {
            return buildErrorResponse(ex, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        /**
         * handler for input error
         *
         * @param ex
         * @return
         */
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
            return buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        /**
         * hanlder for saving new recipe in DB
         *
         * @param ex
         * @return
         */
        @ExceptionHandler(RecipeNotCreatedException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<Object> handleResourceNotCreatedException(RecipeNotCreatedException ex) {
            return buildErrorResponse(ex, ex.getMessage(), ex.getStatus());
        }


        /**
         * handler for global exception
         *
         * @param e
         * @return
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAllUncaughtException(Exception e) {
            return buildErrorResponse(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        private ResponseEntity<Object> buildErrorResponse(Exception e, String errorCode, HttpStatus status) {
            ResponseError errorResponse = new ResponseError(status.value(), getTextMessage(errorCode), LocalDateTime.now());
            log.error("error response: {} ", e);
            return ResponseEntity.status(status).body(errorResponse);
        }

        private String getTextMessage(String errorCode) {
            ErrorText errorMessages = MappingErrorMessages.get(errorCode);
            if (errorMessages == null) {
                return ErrorText.DEFAULT_MSG.getValue();
            }
            return errorMessages.getValue();
        }
    }

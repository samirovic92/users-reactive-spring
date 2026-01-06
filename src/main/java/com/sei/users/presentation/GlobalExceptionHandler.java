package com.sei.users.presentation;

import com.sei.users.domain.UserCreationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserCreationException.class)
    public Mono<ErrorResponse> handleUserCreationException(UserCreationException exception) {
        return Mono.just(
                ErrorResponse.builder(exception, HttpStatus.CONFLICT,  exception.getMessage()).build()
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception exception) {
        return Mono.just(
                ErrorResponse.builder(exception, HttpStatus.INTERNAL_SERVER_ERROR,  exception.getMessage()).build()
        );
    }

    // handle validation/binding errors in WebFlux
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException exception) {
        var errorMessage = getValidationErrorMessage(exception);
        return Mono.just(
                ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, errorMessage).build()
        );
    }

    private String getValidationErrorMessage(WebExchangeBindException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

}

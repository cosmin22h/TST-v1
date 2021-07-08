package com.PSproject.TvShowsTracker.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@ControllerAdvice
class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ApiExceptionResponse.class)
    protected ResponseEntity<Object> handleApiExceptionResponse(ApiExceptionResponse ex){
        HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        return responseEntityBuilder(ApiExceptionResponse.builder()
                .errors(ex.getErrors())
                .status(status)
                .message(ex.getMessage()).build());
    }

    private ResponseEntity<Object> responseEntityBuilder(ApiExceptionResponse ex){
        return new ResponseEntity<>(ex,ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return responseEntityBuilder(ApiExceptionResponse.builder().errors(Collections.singletonList("Can't convert param: "+ex.getCause())).
                status(status).message("Bad Request for enum").build());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return responseEntityBuilder(ApiExceptionResponse.builder().errors(Collections.singletonList("Missing request parameter: "+ex.getParameterName())).
                status(status).message("Missing request parameter").build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors=new LinkedList<>();
        String className=ex.getParameter().getParameterType().getSimpleName();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName= ((FieldError) objectError).getField();
            String errorMessage= objectError.getDefaultMessage();
            errors.add(fieldName+ " - "+errorMessage);
        });
        return responseEntityBuilder(ApiExceptionResponse.builder().errors(errors).status(status).message("Bad request for class: "+className).build());
    }
}
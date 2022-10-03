package org.izce.mongodb_recipe.controllers;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

@ControllerAdvice
public class ControllerExceptionHandler extends DefaultErrorAttributes {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({NumberFormatException.class, WebExchangeBindException.class})
	@Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, 
      ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("status", HttpStatus.BAD_REQUEST.value());
        map.put("message", options);
        return map;
    }
}

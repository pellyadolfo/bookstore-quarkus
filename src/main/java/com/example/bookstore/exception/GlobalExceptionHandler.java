// GlobalExceptionHandler.java
package com.example.bookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
	
    @Override
    public Response toResponse(Exception exception) {
    	
        if (exception instanceof ResourceNotFoundException) {        	
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(body).build();
        }
        
        if (exception instanceof ResourceAlreadyExistsException) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }
        
        if (exception instanceof InsufficientStockException) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
        }
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "An error occurred: " + exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).build();
    }

}

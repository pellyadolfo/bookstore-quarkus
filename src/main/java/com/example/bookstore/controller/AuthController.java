package com.example.bookstore.controller;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.service.UserService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
public class AuthController {
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDTO userDTO) {
        userService.registerUser(userDTO);
        return Response.ok("User registered successfully").build();
    }
    
}

package com.example.bookstore.controller;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.example.bookstore.dto.CartItemDTO;
import com.example.bookstore.service.CartService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/carts")
public class CartController {
    private final CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartByUserId(@PathParam Long userId) {
        return Response.ok(cartService.getCartByUserId(userId)).build();
    }
        
    @POST
    @Path("/user/{userId}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToCart(@PathParam Long userId, CartItemDTO cartItemDTO) {
        return Response.ok(cartService.addItemToCart(userId, cartItemDTO)).build();
    }
    
    @DELETE
    @Path("/user/{userId}/items/{bookId}")
    public Response removeItemFromCart(@PathParam Long userId, @PathParam Long bookId) {
        cartService.removeItemFromCart(userId, bookId);
        return Response.noContent().build();
    }
    
    @DELETE
    @Path("/user/{userId}/clear")
    public Response clearCart(@PathParam Long userId) {
        cartService.clearCart(userId);
        return Response.noContent().build();
    }
}

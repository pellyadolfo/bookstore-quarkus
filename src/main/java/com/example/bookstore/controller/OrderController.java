package com.example.bookstore.controller;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.example.bookstore.service.OrderService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @POST
    @Path("/user/{userId}/checkout")
    public Response createOrderFromCart(@PathParam Long userId) {
        return Response.ok(orderService.createOrderFromCart(userId)).build();
    }
    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam Long orderId) {
        return Response.ok(orderService.getOrderById(orderId)).build();
    }
    
    @GET
    @Path("/user/{userId}")
    public Response getOrdersByUserId(@PathParam Long userId) {
        return Response.ok(orderService.getOrdersByUserId(userId)).build();
    }
}
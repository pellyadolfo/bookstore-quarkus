package com.example.bookstore.controller;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.service.BookService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/books")
public class BookController {
    private final BookService bookService;
    
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GET
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(bookService.getAllBooks()).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam Long id) {
        return Response.ok(bookService.getBookById(id)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(BookDTO bookDTO) {
        return Response.ok(bookService.createBook(bookDTO)).build();
	}
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam Long id, BookDTO bookDTO) {
        return Response.ok(bookService.updateBook(id, bookDTO)).build();
    }
    
    @DELETE
    @Path("/{id}")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam Long id) {
        bookService.deleteBook(id);
        return Response.noContent().build(); // 204 No Content
    }
}

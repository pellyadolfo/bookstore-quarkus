package com.example.bookstore.config;

import org.modelmapper.ModelMapper;

import jakarta.ws.rs.ext.Provider;

@Provider
public class ModelMapperConfig {
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
